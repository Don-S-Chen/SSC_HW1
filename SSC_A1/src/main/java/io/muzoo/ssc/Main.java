package io.muzoo.ssc;

import org.apache.commons.cli.*;
import java.io.IOException;
import java.nio.file.*;

public class Main {

    public static void main(String[] args) {
        Options options = new Options();

        options.addOption("a", "total-num-files", false, "Print the total number of files");
        options.addOption("b", "total-num-dirs", false, "Print the total number of directories");
        options.addOption("c", "total-unique-exts", false, "Print the total number of unique file extensions");
        options.addOption("d", "list-exts", false, "Print the list of all unique file extensions");
        Option numExtOption = Option.builder().longOpt("num-ext").hasArg().argName("EXT").desc("Print the total number of files for the specified extension EXT").build();
        options.addOption(numExtOption);
        Option folderOption = Option.builder("f").longOpt("folder").hasArg().argName("path-to-folder").required().desc("Specify the path to the documentation folder").build();
        options.addOption(folderOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);

            Path folderPath = Paths.get(cmd.getOptionValue("f"));

            if (!Files.exists(folderPath) || !Files.isDirectory(folderPath)) {
                System.out.println("Error: The specified folder path is invalid.");
                formatter.printHelp("FileAnalyzer", options);
                System.exit(1);
            }

            FileVisitor<Path> fileVisitor = new MyFileVisitor();

            Files.walkFileTree(folderPath, fileVisitor);

            if (cmd.hasOption("a")) {
                System.out.println("Total number of files: " + ((MyFileVisitor) fileVisitor).getTotalFiles());
            }

            if (cmd.hasOption("b")) {
                System.out.println("Total number of directories: " + ((MyFileVisitor) fileVisitor).getTotalDirs());
            }

            if (cmd.hasOption("c")) {
                System.out.println("Total number of unique file extensions: " + ((MyFileVisitor) fileVisitor).getUniqueExtensions().size());
            }

            if (cmd.hasOption("d")) {
                System.out.println("List of unique file extensions: " + ((MyFileVisitor) fileVisitor).getUniqueExtensions());
            }

            if (cmd.hasOption("num-ext")) {
                String ext = cmd.getOptionValue("num-ext").toLowerCase();
                System.out.println("Total number of files with extension " + ext + ": " + ((MyFileVisitor) fileVisitor).getExtensionCount().getOrDefault(ext, 0));
            }

            System.out.println("Total letter 'a' in HTML files: " + ((MyFileVisitor) fileVisitor).getTotalLetterAInHtmlFiles());
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("FileAnalyzer", options);
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
