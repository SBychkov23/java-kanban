package service;
import java.io.*;
import model.*;

public class FileBackedTasksManager extends InMemoryTaskManager{

    private File TaskManagerFile; ;
    public FileBackedTasksManager(File DataFile) throws IOException {
        super();
        TaskManagerFile = DataFile;
        loadFromFile( TaskManagerFile);
    }

    public void loadFromFile(File savedHistory) throws IOException
    {
        Reader fileReader = new FileReader(savedHistory);

        int data = fileReader.read();
        System.out.print((char) data);
        data = fileReader.read();
        System.out.print((char) data);
        System.out.print((char) data);
        while (data != -1) {
            System.out.print((char) data);
            data = fileReader.read();
        }
        fileReader.close();
    }
    public void save ()
    {

    }
}
