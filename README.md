# File Operations CLI

This command-line interface (CLI) program allows users to navigate directories, create, delete, copy, move files and folders, and perform basic file operations. It's written in Java.

## Getting Started

### Prerequisites
- Java Development Kit (JDK) installed on your machine.
- JDK 17 preferred.

### Compile the Java code:
- javac FileOperationsCLI.java

### Run the program:
- java FileOperationsCLI

### Customize the Initial Directory

By default, this program starts in the "E:/" directory. If you want to use a different drive or directory as the primary location for file operations, you can customize the initial directory by following these steps:

1. Open the `FileOperationsCLI.java` file in a text editor.

2. Locate the following line of code:
   ```java
   private static Path currentPath = Paths.get("E:/").toAbsolutePath().normalize();

3. Modify the path to match your preferred starting directory. For example, if you want the program to start in the "C:/" directory, change the line to:
   ``` java
   private static Path currentPath = Paths.get("C:/").toAbsolutePath().normalize();
   

## Commands

- `cd <directory>`: Change the current directory.
- `cdparent`: Navigate to the parent directory.
- `ls`: List files and directories in the current directory.
- `mkdir <directory>`: Create a new directory.
- `rmdir <directory>`: Delete a directory and its contents.
- `copy <source_file_path> <destination_directory_path>`: Copy a file to a destination directory.
- `delete <file>`: Delete a file.
- `createfile <filename> <extension> [content]`: Create a new file with optional content.
- `rename <current_name> <new_name>`: Rename an existing file or directory.
- `info <file_or_directory_name>`: Display information about a file or directory.
- `search <search_term>`: Search for files in the current directory and subdirectories.
- `move <source_path> <destination_directory>`: Move a file or directory to a destination directory.


## Usage Examples

- Change Directory
```
cd demofolder
```
- Navigate to the Parent Directory
```
cdparent
```
- List Files and Directories
```
ls
```
- Create a New Directory
```
mkdir newfolder
```
- Delete a Directory
```
rmdir demofolder
```
- Create a New File with Content
```
createfile notes txt "This is Content."
```
- Create a New File without Content
```
createfile notes2 txt
```
- Copy a File to Another Directory
```
copy E:\demo\folder2\notes.txt E:\demo\folder1
```
- Rename a File
```
rename notes.txt DataStructures.txt
```
- Get Information About a File or Directory
```
info notes.txt
```
```
info folder2
```
- Delete a File
```
delete DataStructures.txt
```
- Search for Files by Name or Extension
```
search .docx
```
```
search notes
```
```
search notes.txt
```
- Move a File to Another Directory
```
move E:\demo\folder2\notes.txt E:\demo\folder1
```
