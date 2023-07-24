
public class Main {
    public static void main(String[] args) {

        TaskManager manager = new TaskManager();
        Task task = new Task("Первая задача", "Проверить работу таска", Status.NEW);
        EpicTask epic1 = new EpicTask("Первая эпик задача", "Проверить работу эпика", Status.NEW);
        SubTask sub1 = new SubTask("Первая подзадача", "Проверить работу саба", Status.NEW);
        SubTask sub2 = new SubTask("Вторая подзадача", "Проверить работу саба", Status.NEW);
        manager.setNewTask(task);
        manager.setNewEpicTask(epic1);
        manager.setNewSubTask(sub1, epic1.getId());
        manager.setNewSubTask(sub2, epic1.getId());
        System.out.println(manager.getTasksList());
        manager.getTaskByID(1).setStatus(Status.DONE);
        manager.removeTaskByID(3);
        System.out.println(manager.getTasksList());
        manager.removeAllTasks();
        System.out.println(manager.getTasksList());


    }
}