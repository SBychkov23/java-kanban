package service;

import Exceptions.NotEpicTaskException;
import Exceptions.TimeCrossException;
import model.Status;
import model.SubTask;
import model.Task;
import model.EpicTask;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    public void addTask(int id, Task newTask) throws IOException, TimeCrossException {
        try{
            //фильтр стрима по типу таска и по пересечению времени
            Optional<Task> problem= getPrioritizedTasks().stream().filter(task -> task.getClass().getName().equals(newTask.getClass().getName())).filter(task -> {
            return (!((task.getStartTime().isAfter(newTask.getEndTime()) || newTask.getStartTime().isAfter(task.getEndTime()))));
       }).findFirst();
            if(problem.isEmpty())
        tasksMap.put(id, newTask);
            else throw new TimeCrossException(Integer.toString(problem.get().getId()));
    }
        catch (Exception e)
        {
            if (e.getMessage().equals("Список Task-ов пуст"))
                tasksMap.put(id, newTask);
            else
            System.out.println(e.getMessage());
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
        ((EpicTask) tasksMap.get(EpicID)).updateStartEndMomentsOfSubList();
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

    @Override
    public TreeSet<Task> getPrioritizedTasks() throws IOException {
        if (tasksMap.isEmpty())
            throw new IOException("Список Task-ов пуст");
        else{
            TreeSet<Task> prioritizedTasksList = new TreeSet<>((task1, task2) -> {
                if (task1.getStartTime().isAfter(task2.getStartTime()))
                {
                    return 1;
                }
                else {
                    return -1;
                }
            });
            prioritizedTasksList.addAll(tasksMap.values());
            return prioritizedTasksList;
            }

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
        try {
            addTask(id, newTask);
        }
        catch (Exception e)
        {
            System.out.println("Ошибка при добавлении Task-а типа "+newTask.getClass().getSimpleName()+"\n"+e.getMessage());
        }
    }

    @Override
    public void setNewEpicTask(EpicTask newEpicTask)//добавление нового Эпик Таска
    {
        int id = getNewID();
        newEpicTask.setId(id);
        try {
        addTask(id, newEpicTask);
        }
        catch (Exception e)
        {
            System.out.println("Ошибка при добавлении Task-а типа "+newEpicTask.getClass().getSimpleName()+"\n"+e.getMessage());
        }
    }

    @Override
    public void setNewSubTask(SubTask newSubTask, int parentID) throws NotEpicTaskException//добавление нового саб Таска
    {
        if (tasksMap.get(parentID) instanceof EpicTask) {
            int id = getNewID();
            newSubTask.setId(id);
            newSubTask.setParentId(parentID);
            ((EpicTask) tasksMap.get(parentID)).updateStartEndMomentsOfSubList();
            try {
                addTask(id, newSubTask);
                addToSubList(parentID, newSubTask);
            }
            catch (Exception e)
            {
                System.out.println("Ошибка при добавлении Task-а типа "+newSubTask.getClass().getSimpleName()+"\n"+e.getMessage());
            }
            //приводим элемент мапы взятый по parentID
            // к типу EpicTask т.е от него ожидается что он будет Эпиком
        } else throw new NotEpicTaskException();
    }

}
