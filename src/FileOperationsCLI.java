import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Scanner;
import java.util.stream.Stream;

public class FileOperationsCLI {
    private static Scanner scanner = new Scanner(System.in);
    private static Path currentPath = Paths.get("E:/").toAbsolutePath().normalize();

    public static void main(String[] args) {

        while (true) {
            System.out.print(currentPath + "> ");
            String command = scanner.nextLine();

            if (command.equalsIgnoreCase("exit")) {
                System.out.println("Exiting program.");
                break;
            }

            executeCommand(command);
        }

        scanner.close();
    }

    private static void executeCommand(String command) {
        String[] tokens = command.split("\\s+");
        String action = tokens[0].toLowerCase();

        try {
            switch (action) {
                case "cd":
                    changeDirectory(tokens);
                    break;
                case "cdparent":
                    navigateUp();
                    break;
                case "ls":
                    listFilesAndDirectories();
                    break;
                case "mkdir":
                    createDirectory(tokens);
                    break;
                case "rmdir":
                    deleteDirectory(tokens);
                    break;
                case "copy":
                    copyFile(tokens);
                    break;
                case "delete":
                    deleteFile(tokens);
                    break;
                case "createfile":
                    createFile(tokens);
                    break;
                case "rename":
                    rename(tokens);
                    break;
                case "info":
                    fileInfo(tokens);
                    break;
                case "search":
                    searchFiles(tokens);
                    break;
                case "move":
                    move(tokens);
                    break;
                default:
                    System.out.println("Invalid command. Try again.");
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void changeDirectory(String[] tokens) {
        if (tokens.length != 2) {
            System.out.println("Usage: cd <directory>");
            return;
        }

        Path newDir = currentPath.resolve(tokens[1]);
        if (Files.isDirectory(newDir)) {
            currentPath = newDir.toAbsolutePath().normalize();
        } else {
            System.out.println("Directory not found: " + tokens[1]);
        }
    }

    private static void navigateUp() {
        Path parent = currentPath.getParent();
        if (parent != null) {
            currentPath = parent;
        } else {
            System.out.println("Already at the root directory.");
        }
    }

    private static void listFilesAndDirectories() throws IOException {
        Files.list(currentPath).forEach(path -> System.out.println(path.getFileName()));
    }

    private static void createDirectory(String[] tokens) throws IOException {
        if (tokens.length != 2) {
            System.out.println("Usage: mkdir <directory>");
            return;
        }

        Path newDir = currentPath.resolve(tokens[1]);
        Files.createDirectory(newDir);
    }

    private static void deleteDirectory(String[] tokens) throws IOException {
        if (tokens.length != 2) {
            System.out.println("Usage: rmdir <directory>");
            return;
        }

        Path dirToDelete = currentPath.resolve(tokens[1]);
        if (Files.isDirectory(dirToDelete)) {
            Files.walk(dirToDelete)
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                            System.out.println("Directory deleted successfully.");
                        } catch (IOException e) {
                            System.err.println("Error deleting file: " + e.getMessage());
                        }
                    });
        } else {
            System.out.println("Directory not found: " + tokens[1]);
        }
    }

    private static void copyFile(String[] tokens) throws IOException {
        if (tokens.length != 3) {
            System.out.println("Usage: copy <source_file_path> <destination_directory_path>");
            return;
        }

        String sourcePathString = tokens[1];
        String destinationPathString = tokens[2];

        Path sourcePath = Paths.get(sourcePathString).toAbsolutePath().normalize();
        Path destinationPath = Paths.get(destinationPathString).toAbsolutePath().normalize();

        if (!Files.exists(sourcePath)) {
            System.out.println("Source file not found: " + sourcePathString);
            return;
        }

        if (!Files.isDirectory(destinationPath)) {
            System.out.println("Destination directory not found: " + destinationPathString);
            return;
        }

        Path destinationFile = destinationPath.resolve(sourcePath.getFileName());

        Files.copy(sourcePath, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("File copied successfully to: " + destinationFile);
    }

    private static void deleteFile(String[] tokens) throws IOException {
        if (tokens.length != 2) {
            System.out.println("Usage: delete <file>");
            return;
        }

        Path fileToDelete = currentPath.resolve(tokens[1]);
        if (Files.exists(fileToDelete)) {
            Files.delete(fileToDelete);
            System.out.println("File deleted successfully.");
        } else {
            System.out.println("File not found: " + tokens[1]);
        }
    }

    private static void createFile(String[] tokens) throws IOException {
        if (tokens.length < 3) {
            System.out.println("Usage: createfile <filename> <extension> [content]");
            return;
        }

        String fileName = tokens[1] + "." + tokens[2];
        Path filePath = currentPath.resolve(fileName);

        if (!Files.exists(filePath)) {
            Files.createFile(filePath);

            if (tokens.length > 3) {

                String content = String.join(" ", Arrays.copyOfRange(tokens, 3, tokens.length));
                Files.writeString(filePath, content);
                System.out.println("File created with content: " + fileName);
            } else {
                System.out.println("File created: " + fileName);
            }
        } else {
            System.out.println("File already exists: " + fileName);
        }
    }

    private static void rename(String[] tokens) throws IOException {
        if (tokens.length != 3) {
            System.out.println("Usage: rename <current_name> <new_name>");
            return;
        }

        String currentName = tokens[1];
        String newName = tokens[2];

        Path currentPathResolved = currentPath.resolve(currentName);
        Path newPathResolved = currentPath.resolve(newName);

        if (Files.exists(currentPathResolved)) {
            Files.move(currentPathResolved, newPathResolved, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Renamed successfully: " + currentName + " to " + newName);
        } else {
            System.out.println("File or directory not found: " + currentName);
        }
    }

    private static void fileInfo(String[] tokens) throws IOException {
        if (tokens.length != 2) {
            System.out.println("Usage: info <file_or_directory_name>");
            return;
        }

        String name = tokens[1];
        Path path = currentPath.resolve(name);

        if (Files.exists(path)) {
            BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            System.out.println("File/Directory: " + name);
            System.out.println("Type: " + (Files.isDirectory(path) ? "Directory" : "File"));
            System.out.println("Size: " + attributes.size() + " bytes");
            System.out.println("Creation Time: " + sdf.format(new Date(attributes.creationTime().toMillis())));
            System.out.println("Last Modified Time: " + sdf.format(new Date(attributes.lastModifiedTime().toMillis())));
        } else {
            System.out.println("File or directory not found: " + name);
        }
    }

    private static void searchFiles(String[] tokens) throws IOException {
        if (tokens.length != 2) {
            System.out.println("Usage: search <search_term>");
            return;
        }

        String searchTerm = tokens[1];
        try (Stream<Path> stream = Files.walk(currentPath)) {
            stream
                    .filter(path -> Files.isRegularFile(path))
                    .filter(path -> path.getFileName().toString().toLowerCase().contains(searchTerm.toLowerCase()))
                    .forEach(path -> System.out.println(path));
        }
    }

    private static void move(String[] tokens) throws IOException {
        if (tokens.length != 3) {
            System.out.println("Usage: move <source_path> <destination_directory>");
            return;
        }

        String sourcePathString = tokens[1];
        String destinationPathString = tokens[2];

        Path sourcePath = Paths.get(sourcePathString).toAbsolutePath().normalize();
        Path destinationPath = Paths.get(destinationPathString).toAbsolutePath().normalize();

        if (!Files.exists(sourcePath)) {
            System.out.println("Source file or directory not found: " + sourcePathString);
            return;
        }

        if (!Files.isDirectory(destinationPath)) {
            System.out.println("Destination directory not found: " + destinationPathString);
            return;
        }

        Path destinationFile = destinationPath.resolve(sourcePath.getFileName());

        Files.move(sourcePath, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("File or directory moved successfully to: " + destinationFile);
    }
}
