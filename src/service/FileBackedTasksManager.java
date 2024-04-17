package service;
import java.io.*;
import model.*;
import Exceptions.*;
import java.nio.charset.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.nio.file.*;
public class FileBackedTasksManager extends InMemoryTaskManager{

    private static final String TITTLE_LINE = "id,type,name,status,description,epic";

    private File TaskManagerFile; ;
    public FileBackedTasksManager(File DataFile) throws IOException {
        super();
        TaskManagerFile = DataFile;
        loadFromFile( TaskManagerFile);
    }

    public void loadFromFile(File savedHistory) throws IOException //функционал readTasksFromFile намеренно отделён от метода loadFromFile для будущего переиспользования метода
    {
        tasksMap.clear();
        List<String> TasksFromFile =readTasksFromFile(savedHistory);
        if(TasksFromFile.size()<=1)
        {
            System.out.println("В файле записи отсуствуют Таски");
        }
        else {
            for (int i = 1; i < TasksFromFile.size(); i++) {
                String TaskLine = TasksFromFile.get(i);
                String[] tasksAttributes = TaskLine.split(",");
                try {
                    switch (tasksAttributes[1])//id,type,name,status,description,epic
                    {
                        case ("SubTask"):
                            setNewSubTask(new SubTask(tasksAttributes[2], tasksAttributes[4], Status.valueOf(tasksAttributes[3])),
                                    Integer.parseInt(tasksAttributes[0]),
                                            Integer.parseInt(tasksAttributes[5]));
                            break;
                        case ("Task"):
                            setNewTask(new Task(tasksAttributes[2], tasksAttributes[4], Status.valueOf(tasksAttributes[3])), Integer.parseInt(tasksAttributes[0]));
                            break;
                        case ("EpicTask"):
                            setNewEpicTask(new EpicTask(tasksAttributes[2], tasksAttributes[4], Status.valueOf(tasksAttributes[3])), Integer.parseInt(tasksAttributes[0]));

                    }
                } catch (Exception e) {
                    System.out.println("Записи не обнаружены или нарушен формат записи: "+e.getMessage());
                }

            }
        }
    }
    private List<String> readTasksFromRAM()
    {
        List <String> ListOFTasks = new ArrayList<String>();
        for (Task task: getTasksList().values())
            ListOFTasks.add(task.toString());

        return ListOFTasks;
    }
    private static List<String> readTasksFromFile(File filename) { //функционал readTasksFromFile намеренно отделён от метода loadFromFile для будущего переиспользования метода
        List <String> ListOFTasks = new ArrayList<String>();
        try (BufferedReader br =  Files.newBufferedReader(Paths.get(filename.toURI()), StandardCharsets.UTF_8))
        {
            while (br.ready()) {
                String line = br.readLine();
                ListOFTasks.add(line);
            }

        }
        catch (Exception e)
        {
            System.out.println("Произошла ошибка во время чтения файла.");
            System.exit(0);
        }

        return ListOFTasks;
    }
    public void save () throws IOException
    {
        try {
            Files.deleteIfExists(Paths.get(TaskManagerFile.toURI()));
            Files.createFile(Paths.get(TaskManagerFile.toURI()));
        }
        catch (Throwable e) {
            System.out.println("Произошла ошибка при перезаписи файла сохранения");
            e.printStackTrace();
        }

        try ( BufferedWriter fl = Files.newBufferedWriter(Paths.get(TaskManagerFile.toURI()), StandardCharsets.UTF_8))
        {
            fl.write(TITTLE_LINE+"\n");
            for (String line: readTasksFromRAM())
                fl.write(line+"\n");
        }
        catch (Throwable e)
        {
            System.out.println("Произошла ошибка во время записи файла.");
        }
    }


    public void setNewTask(Task newTask, int memoryOwnID ) {
        updateLastID(memoryOwnID);
        newTask.setId(memoryOwnID);
        tasksMap.put(memoryOwnID, newTask);
    }


    public void setNewEpicTask(EpicTask newEpicTask, int memoryOwnID) {
        updateLastID(memoryOwnID);
        newEpicTask.setId(memoryOwnID);
        tasksMap.put(memoryOwnID, newEpicTask);
    }


    public void setNewSubTask(SubTask newSubTask, int memoryOwnID, int memoryParentID ) throws ParentTaskMissingException {
    try {
        if (tasksMap.get(memoryParentID) instanceof EpicTask) {
            updateLastID(memoryOwnID);
            newSubTask.setId(memoryOwnID);
            newSubTask.setParentId(memoryParentID);
            tasksMap.put(memoryOwnID, newSubTask);
            addToSubList(memoryParentID, newSubTask);
            //приводим элемент мапы взятый по memoryParentID
            // к типу EpicTask т.е от него ожидается что он будет Эпиком
        } else {
            throw new ParentTaskMissingException();
        }
    }
    catch (Throwable e)
        {
            System.out.println(e.getMessage());
        }

    }
}

