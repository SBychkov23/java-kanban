import service.InMemoryTaskManager;
import model.Status;
import model.SubTask;
import model.Task;
import model.EpicTask;

public class Main {
    public static void main(String[] args) {

        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task task = new Task("Первая задача", "Проверить работу таска", Status.NEW);
        EpicTask epic1 = new EpicTask("Первая эпик задача", "Проверить работу эпика", Status.NEW);
        SubTask sub1 = new SubTask("Первая подзадача", "Проверить работу саба", Status.NEW);
        SubTask sub2 = new SubTask("Вторая подзадача", "Проверить работу саба", Status.NEW);
        manager.setNewTask(task);
        manager.setNewEpicTask(epic1);
        manager.setNewSubTask(sub1, epic1.getId());
        manager.setNewSubTask(sub2, epic1.getId());
        System.out.println(manager.getTasksList());
        System.out.println(manager.getTasksList());
        System.out.println(manager.getTaskStatus(1));
        Task ii = manager.getTaskByID(4);
        Task i2 = manager.getTaskByID(4);
        Task i3 = manager.getTaskByID(3);
        System.out.println(manager.historyManager.getHistory());

    }
}