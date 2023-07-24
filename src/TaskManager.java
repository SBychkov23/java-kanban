import java.util.HashMap;

public class TaskManager {

   public static HashMap<Integer, Task> tasksList = new HashMap<>();
     private int IdCount;
    public TaskManager()//запуск нового кейса менеджера
    {
        IdCount=0;
    }

    public int getNewId()
    {
        IdCount++;
        return IdCount;
    }
    public void setNewTaskStatus(int taskId, Status status)
    {
        tasksList.get(taskId).setStatus(status);
    }
    public void removeTask(int taskId)
    {
        tasksList.remove(taskId);
    }
    public Task setNewTask (String name, String description, Status status)
    {
        int id =getNewId();
        return new Task(name, description, id, status);
    }
}
