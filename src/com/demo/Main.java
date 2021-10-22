package com.demo;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {

        Path path1 = Paths.get("C:/Users/bgrap/JavaProjects/working-with-files/example-file.txt");
        Path path2 = Paths.get("C:","Users", "bgrap", "JavaProjects", "working-with-files" ,"example-file.txt");

        URI uri = URI.create("file://C:/Users/bgrap/JavaProjects/working-with-files/example-file.txt");
        Path path3 = Paths.get(uri);

        // Java 11

        Path path4 = Path.of("C:/Users/bgrap/JavaProjects/working-with-files/example-file.txt");
        Path path5 = Path.of("C:","Users", "bgrap", "JavaProjects", "working-with-files", "example-file.txt");

        URI uri2 = URI.create("file://C:/Users/bgrap/JavaProjects/working-with-files/example-file.txt");
        Path path6 = Path.of(uri2);

        System.out.println("path1 exists: " + Files.exists(path1) + ", path1 exists (without following symbolic links): " + Files.exists(path1, LinkOption.NOFOLLOW_LINKS));

        try {
            System.out.println("path1 same as path2: " + Files.isSameFile(path1, path2));
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

//            BufferedReader reader = Files.newBufferedReader(path1, StandardCharsets.ISO_8859_1); add charset

        try (FileWriter fw = new FileWriter(path1.toFile(), true);
             BufferedWriter writer = new BufferedWriter(fw);
             PrintWriter pw = new PrintWriter(writer);
             ) {
            writer.write("let's write another line");
            pw.println("\nhere's a line from the print writer");
            writer.flush();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

        try (BufferedReader reader = Files.newBufferedReader(path1, StandardCharsets.UTF_8)) {
            String line  = reader.readLine();
            while (line != null) {
                System.out.println(line);
                line  = reader.readLine();
            }
        } catch (Exception exception) {
            System.out.println("file DNE or CharSet is not UTF 8" + exception.getMessage());
        }

        Path csvPath = Paths.get("C:/Users/bgrap/JavaProjects/working-with-files/data.csv");

//        try (BufferedReader reader = Files.newBufferedReader(csvPath)) {
//            Stream<String> lines  = reader.lines();
        try (Stream<String> lines2  = Files.lines(csvPath);) {
            lines2.forEach(System.out::println);
        } catch (Exception exception) {
            System.out.println("file DNE or CharSet is not UTF 8" + exception.getMessage());
        }

        Path dir = Path.of("C:/Users/bgrap/JavaProjects");
        Path csv = Path.of("data.csv");

//        try {
//            Stream<Path> csvFile = Files.find(dir, 2, (path, attributes) -> path.endsWith(csv));
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//        }

        var visitor = new FileVisitor<Path>() {

            private long countFiles = 0L;
            private long countDirectories = 0L;

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                countDirectories++;
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                countFiles++;
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        };

        try {
            Files.walkFileTree(dir, visitor);
            System.out.println("Files: " + visitor.countFiles + ", Directories: " + visitor.countDirectories);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // stream and walk

        try {
            Stream<Path> str = Files.walk(dir);

            long dirCount = Files.walk(dir).filter(d -> Files.isDirectory(d)).count();
            long fileCount = Files.walk(dir).filter(Files::isRegularFile).count();

            System.out.println("Total: " + str.count() + ", File Count (stream): " + fileCount +  ", Dir Count (stream): " + dirCount);


        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
}
