package Service;

import model.Status;
import model.SubTask;
import model.Task;
import model.EpicTask;

import java.util.HashMap;

public class TaskManager {

    public static HashMap<Integer, Task> tasksList = new HashMap<>();
    private static int IdCount;

    public TaskManager()//запуск нового кейса менеджера
    {
        IdCount = 0;
    }

    public HashMap<Integer, Task> getTasksList() {
        return tasksList;
    }
    public void removeTaskByID(int taskId) {

        checkForTaskConnectionsToDelete(taskId);
        tasksList.remove(taskId);

    }
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

    public void addToSubList (int EpicID,  SubTask sub)
    {

        ((EpicTask) tasksList.get(EpicID)).childSubTasks.put(sub.getId(), sub);
    }
    public void removeFromSubList (int EpicID, int SubID)
    {
        ((EpicTask) tasksList.get(EpicID)).childSubTasks.remove(SubID);
    }
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

    public int getNewID() {
        IdCount++;
        return IdCount;
    }
    public void getTaskStatus (int taskID)
    {
        tasksList.get(taskID).getStatus();
    }

    public void setNewTaskStatus(int taskId, Status status) {

        tasksList.get(taskId).setStatus(status);
    }
    public Task getTaskByID(int taskID)
    {
        return tasksList.get(taskID);
    }



    public void setNewTask(Task newTask) //добавление нового Таска
    {
        int id = getNewID();
        newTask.setId(id);
        tasksList.put(id, newTask);
    }

    public void setNewEpicTask(EpicTask newEpicTask)//добавление нового Эпик Таска
    {
        int id = getNewID();
        newEpicTask.setId(id);
        tasksList.put(id, newEpicTask);
    }
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
