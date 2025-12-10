package KOLOKVIUMSKI;


import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

interface IFile extends Comparable<IFile> {
    String getFileName();
    long getFileSize();
    String getFileInfo(int indent);
    void sortBySize();
    long findLargestFile ();
    String type();
}

class FileNameExistsException extends RuntimeException{
    public FileNameExistsException(String name, String folderName) {
        super(String.format("There is already a file named %s in the folder %s", name, folderName));
    }
}

class File implements IFile{
    String name;
    long size;

    public File(String name, long size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public String getFileName() {
        return name;
    }

    @Override
    public long getFileSize() {
        return size;
    }

    @Override
    public String getFileInfo(int indent) {
        String prefix = "    ".repeat(indent);
        return String.format("%sFile name %10s File size: %10d", prefix, name, getFileSize());
    }

    @Override
    public void sortBySize() {

    }

    @Override
    public long findLargestFile() {
        return 0;
    }

    @Override
    public String type() {
        return "file";
    }

    @Override
    public int compareTo(IFile o) {
        return Long.compare(getFileSize(), o.getFileSize());
    }
}

class Folder implements IFile{
    String name;
    List<IFile> files;

    public Folder(String name) {
        this.name = name;
        this.files = new ArrayList<>();
    }

    void addFile (IFile file){
        Optional<IFile> file2 = files.stream().filter(i -> i.getFileName().equals(file.getFileName())).findFirst();
        if (file2.isPresent()) {
            throw new FileNameExistsException(file.getFileName(), name);
        }
        files.add(file);
    }


    @Override
    public String getFileName() {
        return name;
    }

    @Override
    public long getFileSize() {
        return files.stream().mapToLong(IFile::getFileSize).sum();
    }

    @Override
    public String getFileInfo(int indent) {
        StringBuilder sb = new StringBuilder();
        String prefix = "    ".repeat(indent);

        sb.append(String.format("%sFolder name %10s Folder size: %10d", prefix, name, getFileSize()));
        if(!files.isEmpty()){
            sb.append("\n");
        }
        sb.append(files.stream().map(i -> i.getFileInfo(indent +1)).collect(Collectors.joining("\n")));
        return sb.toString();
    }

    @Override
    public void sortBySize() {
        this.files = files.stream().sorted().collect(Collectors.toList());
        for (int i = 0; i < files.size(); i++) {
            files.get(i).sortBySize();
        }
    }

    @Override
    public long findLargestFile() {
        long largest = 0;

        for (IFile f : files) {
            if (f.type().equals("file")) {
                largest = Math.max(largest, f.getFileSize());
            } else {
                largest = Math.max(largest, f.findLargestFile());
            }
        }
        return largest;
    }

    @Override
    public String type() {
        return "folder";
    }

    @Override
    public int compareTo(IFile o) {
        return Long.compare(this.getFileSize(), o.getFileSize());
    }
}

class FileSystem{
    Folder rootDirectory;

    public FileSystem() {
        this.rootDirectory = new Folder("root");
    }

    void addFile (IFile file){
        rootDirectory.addFile(file);
    }

    long findLargestFile (){
        return rootDirectory.findLargestFile();
    }

    void sortBySize(){
        rootDirectory.sortBySize();
    }

    @Override
    public String toString() {
        return rootDirectory.getFileInfo(0) + "\n";
    }
}

public class FileSystemTest {

    public static Folder readFolder (Scanner sc)  {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i=0;i<totalFiles;i++) {
            String line = sc.nextLine();

            if (line.startsWith("0")) {
                String fileInfo = sc.nextLine();
                String [] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return folder;
    }

    public static void main(String[] args)  {

        //file reading from input

        Scanner sc = new Scanner (System.in);

        System.out.println("===READING FILES FROM INPUT===");
        FileSystem fileSystem = new FileSystem();
        try {
            fileSystem.addFile(readFolder(sc));
        } catch (FileNameExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("===PRINTING FILE SYSTEM INFO===");
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
        fileSystem.sortBySize();
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
        System.out.println(fileSystem.findLargestFile());




    }
}
