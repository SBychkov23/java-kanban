package service;

import Exceptions.NotEpicTaskException;
import Exceptions.SubAlreadyInSubListException;
import Exceptions.TimeCrossException;
import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {

    public HashMap<Integer, Task> getTasksList();

    public ArrayList<Task> getSpecifiedByTypeTaskList(Type t);

    void addTask (int id, Task newTask) throws IOException, TimeCrossException;
    public void removeTaskByID(int taskId) ;

     void setNewTaskOfType(Type t, String body) throws TimeCrossException, IOException;


    public void removeAllTasks() ;

    public void addToSubList(int EpicID, SubTask sub) throws SubAlreadyInSubListException;

    public void removeFromSubList(int EpicID, int SubID) ;

    public void checkForTaskConnectionsToDelete(int taskId); //метод для корректной очистки в случае удаления эпик или саб таска

        public static void updateEpicStatus(int EpicID){} ;

    public int getNewID() ;

    public Status getTaskStatus(int taskID) ;

    public void setNewTaskStatus(int taskId, Status status) ;

    public Task getTaskByID(int taskID) ;

    public int getLastID();// получение последнего присвоенного ID

    TreeSet<Task> getPrioritizedTasks() throws IOException;

    public void updateLastID(int ID); // обновление счётчика ID в случаях работы с сохранёнными тасками



    public void setNewTask(Task newTask);//добавление нового Таска


    public void setNewEpicTask(EpicTask newEpicTask);//добавление нового Эпик Таска


    public void setNewSubTask(SubTask newSubTask, int parentID) throws NotEpicTaskException;//добавление нового саб Таска


}