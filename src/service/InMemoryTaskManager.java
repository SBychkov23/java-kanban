package service;

import model.Status;
import model.SubTask;
import model.Task;
import model.EpicTask;

import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {


    public static   HashMap<Integer, Task> tasksMap = new HashMap<>();
    private static int IdCount;
    public HistoryManager historyManager = getDefaultHistory();

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }


    ///////////////////////////////////////////////////////////////////////////////FUNCTIONAL METHODS GROUP
    public InMemoryTaskManager(){//запуск нового кейса менеджера
        IdCount = 0;
    }

    public static String printSubs(int EpicID) {
        String line = "";
        for (SubTask sub : ((EpicTask) tasksMap.get(EpicID)).getSubtasksMap().values())
            line += "\n" + sub.toString();
        return line;
    }

    public static void updateEpicStatus(int EpicID) {
        int statusDone = 0;
        int statusNew = 0;
        for (SubTask sub : ((EpicTask) tasksMap.get(EpicID)).getSubtasksMap().values()) {
            if (sub.getStatus().equals(Status.DONE)) statusDone++;
            else if (sub.getStatus().equals(Status.NEW)) statusNew++;
        }
        if (statusDone == ((EpicTask) tasksMap.get(EpicID)).getSubtasksMap().size()) {
            tasksMap.get(EpicID).setStatus(Status.DONE);
        } else if (statusNew == ((EpicTask) tasksMap.get(EpicID)).getSubtasksMap().size()) {
            tasksMap.get(EpicID).setStatus(Status.NEW);
        } else {
            tasksMap.get(EpicID).setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public int getLastID() {
        return IdCount;
    }

    @Override
    public void updateLastID(int ID) {
        if (IdCount<ID)
            IdCount=ID;
    }

    @Override
    public void removeTaskByID(int taskId) {
        checkForTaskConnectionsToDelete(taskId);
        tasksMap.remove(taskId);
    }

    @Override
    public void removeAllTasks() {
        tasksMap.clear();
    }

    @Override
    public void addToSubList(int EpicID, SubTask sub) {
        ((EpicTask) tasksMap.get(EpicID)).getSubtasksMap().put(sub.getId(), sub);
    }

    @Override
    public void removeFromSubList(int EpicID, int SubID) {
        ((EpicTask) tasksMap.get(EpicID)).getSubtasksMap().remove(SubID);
    }

    @Override
    public void checkForTaskConnectionsToDelete(int taskId) //метод для корректной очистки в случае удаления эпик или саб таска
    {
        if (tasksMap.get(taskId) instanceof EpicTask) {
            for (Task task : tasksMap.values()) {
                for (SubTask sub : ((EpicTask) tasksMap.get(taskId)).getSubtasksMap().values()) {
                    if (task.equals(sub)) tasksMap.remove(sub.getId());
                }
            }
        } else if (tasksMap.get(taskId) instanceof SubTask)
            ((EpicTask) tasksMap.get((((SubTask) tasksMap.get(taskId)).getParentId()))).getSubtasksMap().remove(taskId);// Здесь удаляем сабтакс из эпика

    }
    ///////////////////////////////////////////////////////////////////////////////GET METHODS GROUP

    @Override
    public HashMap<Integer, Task> getTasksList() {
        return tasksMap;
    }

    @Override
    public int getNewID() {
        IdCount++;
        return IdCount;
    }

    @Override
    public Status getTaskStatus(int taskID) {
        historyManager.addToHistory(tasksMap.get(taskID));
        return tasksMap.get(taskID).getStatus();
    }

    @Override
    public Task getTaskByID(int taskID) {
        historyManager.addToHistory(tasksMap.get(taskID));
        return tasksMap.get(taskID);
    }

    ///////////////////////////////////////////////////////////////////////////////SET METHODS GROUP
    @Override
    public void setNewTaskStatus(int taskId, Status status) {
        tasksMap.get(taskId).setStatus(status);
    }

    @Override
    public void setNewTask(Task newTask) //добавление нового Таска
    {
        int id = getNewID();
        newTask.setId(id);
        tasksMap.put(id, newTask);
    }

    @Override
    public void setNewEpicTask(EpicTask newEpicTask)//добавление нового Эпик Таска
    {
        int id = getNewID();
        newEpicTask.setId(id);
        tasksMap.put(id, newEpicTask);
    }

    @Override
    public void setNewSubTask(SubTask newSubTask, int parentID)//добавление нового саб Таска
    {
        if (tasksMap.get(parentID) instanceof EpicTask) {
            int id = getNewID();
            newSubTask.setId(id);
            newSubTask.setParentId(parentID);
            tasksMap.put(id, newSubTask);
            addToSubList(parentID, newSubTask);
            //приводим элемент мапы взятый по parentID
            // к типу EpicTask т.е от него ожидается что он будет Эпиком
        } else System.out.println("Передан не верный тип родителя, ожидался EpicTask или родитель не объявлен");
    }
}
