package service;

import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.HashMap;

public interface TaskManager {

    public HashMap<Integer, Task> getTasksList();
    public void removeTaskByID(int taskId) ;


    public void removeAllTasks() ;

    public static String printSubs(int EpicID){
        return new String();
    } ;

    public void addToSubList(int EpicID, SubTask sub) ;

    public void removeFromSubList(int EpicID, int SubID) ;

    public void checkForTaskConnectionsToDelete(int taskId); //метод для корректной очистки в случае удаления эпик или саб таска

        public static void updateEpicStatus(int EpicID){} ;

    public int getNewID() ;

    public Status getTaskStatus(int taskID) ;

    public void setNewTaskStatus(int taskId, Status status) ;

    public Task getTaskByID(int taskID) ;

    public int getLastID();// получение последнего присвоенного ID

    public void updateLastID(int ID); // обновление счётчика ID в случаях работы с сохранёнными тасками



    public void setNewTask(Task newTask);//добавление нового Таска


    public void setNewEpicTask(EpicTask newEpicTask);//добавление нового Эпик Таска


    public void setNewSubTask(SubTask newSubTask, int parentID);//добавление нового саб Таска


}