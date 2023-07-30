package service;

import model.Status;
import model.SubTask;
import model.Task;
import model.EpicTask;

import java.util.HashMap;

public class InMemoryTaskManager extends Managers implements TaskManager {


    public static HashMap<Integer, Task> tasksList = new HashMap<>();
    private static int IdCount;
    public HistoryManager historyManager = getDefaultHistory();



    ///////////////////////////////////////////////////////////////////////////////FUNCTIONAL METHODS GROUP
    public InMemoryTaskManager()//запуск нового кейса менеджера
    {

        IdCount = 0;
    }
    @Override
    public void removeTaskByID(int taskId) {

        checkForTaskConnectionsToDelete(taskId);
        tasksList.remove(taskId);

    }
    @Override
    public void removeAllTasks() {
        tasksList.clear();
    }
    public static String printSubs(int EpicID)
    {
        String line = "";
        for(SubTask sub: ((EpicTask) tasksList.get(EpicID)).childSubTasks.values())
            line+="\n"+sub.toString();
        return line;
    }
    @Override
    public void addToSubList (int EpicID,  SubTask sub)
    {

        ((EpicTask) tasksList.get(EpicID)).childSubTasks.put(sub.getId(), sub);
    }
    @Override
    public void removeFromSubList (int EpicID, int SubID)
    {
        ((EpicTask) tasksList.get(EpicID)).childSubTasks.remove(SubID);
    }
    @Override
    public void checkForTaskConnectionsToDelete(int taskId) //метод для корректной очистки в случае удаления эпик или саб таска
    {
        if(tasksList.get(taskId) instanceof EpicTask)
        {
            for (Task task: tasksList.values())
            {
                for (SubTask sub:((EpicTask) tasksList.get(taskId)).childSubTasks.values())
                {
                    if (task.equals(sub))
                        tasksList.remove(sub.getId());
                }
            }
        }
        else if(tasksList.get(taskId) instanceof SubTask)
            ((EpicTask) tasksList.get((((SubTask) tasksList.get(taskId)).getParentId()))).childSubTasks.remove(taskId);// Здесь удаляем сабтакс из эпика

    }
    public static void updateEpicStatus ( int EpicID) {
        int statusDone = 0;
        int statusNew = 0;
        for (SubTask sub : ((EpicTask) tasksList.get(EpicID)).childSubTasks.values()) {
            if (sub.getStatus().equals(Status.DONE))
                statusDone++;
            else if (sub.getStatus().equals(Status.NEW))
                statusNew++;
        }
        if (statusDone == ((EpicTask) tasksList.get(EpicID)).childSubTasks.size()) {
            ((EpicTask) tasksList.get(EpicID)).setStatus(Status.DONE);
        } else if (statusNew == ((EpicTask) tasksList.get(EpicID)).childSubTasks.size()) {
            ((EpicTask) tasksList.get(EpicID)).setStatus(Status.NEW);
        } else {
            ((EpicTask) tasksList.get(EpicID)).setStatus(Status.IN_PROGRESS);
        }
    }
    ///////////////////////////////////////////////////////////////////////////////GET METHODS GROUP

    @Override
    public HashMap<Integer, Task> getTasksList() {
        return tasksList;
    }
    @Override
    public int getNewID() {
        IdCount++;
        return IdCount;
    }
    @Override
    public Status getTaskStatus (int taskID)
    {
        historyManager.addToHistory(tasksList.get(taskID));
        return tasksList.get(taskID).getStatus();
    }
    @Override
    public Task getTaskByID(int taskID)
    {
        historyManager.addToHistory(tasksList.get(taskID));
        return tasksList.get(taskID);
    }
    ///////////////////////////////////////////////////////////////////////////////SET METHODS GROUP
    @Override
    public void setNewTaskStatus(int taskId, Status status) {

        tasksList.get(taskId).setStatus(status);
    }

    @Override
    public void setNewTask(Task newTask) //добавление нового Таска
    {
        int id = getNewID();
        newTask.setId(id);
        tasksList.put(id, newTask);
    }
    @Override
    public void setNewEpicTask(EpicTask newEpicTask)//добавление нового Эпик Таска
    {
        int id = getNewID();
        newEpicTask.setId(id);
        tasksList.put(id, newEpicTask);
    }
    @Override
    public void setNewSubTask(SubTask newSubTask, int parentID)//добавление нового саб Таска
    {
        if(tasksList.get(parentID) instanceof EpicTask) {
            int id = getNewID();
            newSubTask.setId(id);
            newSubTask.setParentId(parentID);
            tasksList.put(id, newSubTask);
             addToSubList(parentID, newSubTask);
            //приводим элемент мапы взятый по parentID
            // к типу EpicTask т.е от него ожидается что он будет Эпиком
        }
        else
            System.out.println("Передан не верный тип родителя, ожидался EpicTask");

    }
}
