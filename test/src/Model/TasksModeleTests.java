package Model;


import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;

public class TasksModeleTests {

    ///Создание тестовых тасков:
    public Task task1 = new Task("Обычная задача1", "Проверить создание задачи", Status.NEW, 60);
    public  Task task2 = new Task("Обычная задача2", "Проверить создание задачи", Status.NEW, 60);
    public EpicTask epic1 = new EpicTask("Первая эпик задача", "Проверить работу эпика", Status.NEW);
    public EpicTask epic2 = new EpicTask("Вторая эпик задача", "Проверить работу эпика", Status.NEW);
    public EpicTask epic3 = new EpicTask("Третья эпик задача", "Проверить работу эпика", Status.NEW);
    public  EpicTask epic4 = new EpicTask("4 эпик задача", "Проверить работу эпика", Status.NEW);
    public SubTask sub1 = new SubTask("Первая подзадача", "Проверить работу саба", Status.NEW, 60);
    public  SubTask sub2 = new SubTask("Вторая подзадача", "Проверить работу саба", Status.NEW, 60);
    public  SubTask sub3 = new SubTask("Третья подзадача", "Проверить работу саба", Status.NEW, 65);


}
