package AUD4;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BinaryFile {
    private List<Integer> data;

    public BinaryFile() {
        data = new ArrayList<Integer>();
    }

    public void readData(InputStream stream, OutputStream outputStream) throws IOException {
        DataInputStream dos = new DataInputStream(stream);
        PrintWriter pw = new PrintWriter(outputStream);
        while(dos.available()>0){
            int num = dos.readInt();
            pw.println(num);
            data.add(num);
        }
        pw.flush();
    }

    public static void main(String[] args) throws IOException {
        File f = new File("C:\\Users\\Nikola Iliev\\IdeaProjects\\AP\\src\\AUD4\\BinaryFile.bin");
        BinaryFile file = new BinaryFile();
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));
        for (int i = 0; i < 10; i++) {
            dos.writeInt(i);
        }

        file.readData(new FileInputStream(f), System.out);
    }
}
