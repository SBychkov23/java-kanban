import service.InMemoryTaskManager;
import model.Status;
import model.SubTask;
import model.Task;
import model.EpicTask;

public class Main {
    public static void main(String[] args) {

        InMemoryTaskManager manager = new InMemoryTaskManager();
        EpicTask epic1 = new EpicTask("Первая эпик задача", "Проверить работу эпика", Status.NEW);
        EpicTask epic2 = new EpicTask("Вторая эпик задача", "Проверить работу эпика", Status.NEW);
        EpicTask epic3 = new EpicTask("Третья эпик задача", "Проверить работу эпика", Status.NEW);
        EpicTask epic4 = new EpicTask("4 эпик задача", "Проверить работу эпика", Status.NEW);
        SubTask sub1 = new SubTask("Первая подзадача", "Проверить работу саба", Status.NEW);
        SubTask sub2 = new SubTask("Вторая подзадача", "Проверить работу саба", Status.NEW);
        SubTask sub3 = new SubTask("Третья подзадача", "Проверить работу саба", Status.NEW);
        manager.setNewEpicTask(epic1);
       manager.setNewEpicTask(epic2);
        manager.setNewEpicTask(epic3);
        manager.setNewSubTask(sub1, epic1.getId());
        manager.setNewSubTask(sub2, epic1.getId());
        manager.setNewSubTask(sub3, epic1.getId());
        manager.setNewEpicTask(epic4);
    manager.getTaskByID(epic1.getId());
     manager.getTaskByID(epic2.getId());
        manager.getTaskByID(epic3.getId());
        manager.getTaskByID(epic4.getId());
        manager.getTaskByID(sub1.getId());
        manager.getTaskByID(sub2.getId());
       manager.getTaskByID(epic2.getId());
        manager.historyManager.remove(epic1.getId());
        System.out.println(manager.historyManager.getHistory());

    }
}