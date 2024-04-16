
import org.junit.Assert;
import service.*;
import model.*;
import java.io.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.*;
import java.nio.file.*;

public class TaskManagerUnitTesting {

    private InMemoryTaskManager TestManager = new InMemoryTaskManager();// экземпляр тестового менеджера
    ///Создание тестовых тасков:
    Task task1 = new Task("Обычная задача", "Проверить создание задачи", Status.NEW);
    EpicTask epic1 = new EpicTask("Первая эпик задача", "Проверить работу эпика", Status.NEW);
    EpicTask epic2 = new EpicTask("Вторая эпик задача", "Проверить работу эпика", Status.NEW);
    EpicTask epic3 = new EpicTask("Третья эпик задача", "Проверить работу эпика", Status.NEW);
    EpicTask epic4 = new EpicTask("4 эпик задача", "Проверить работу эпика", Status.NEW);
    SubTask sub1 = new SubTask("Первая подзадача", "Проверить работу саба", Status.NEW);
    SubTask sub2 = new SubTask("Вторая подзадача", "Проверить работу саба", Status.NEW);
    SubTask sub3 = new SubTask("Третья подзадача", "Проверить работу саба", Status.NEW);

   public TaskManagerUnitTesting()
    {
        ManagerFillTest();
        HistoryAddRemoveTest();
    }
   @Test
    public void ManagerFillTest() {
        //заполнение тестового менджера:
       TestManager.setNewTask(task1);
        TestManager.setNewEpicTask(epic1);
        TestManager.setNewEpicTask(epic2);
        TestManager.setNewSubTask(sub1, epic1.getId());
        TestManager.setNewSubTask(sub2, epic2.getId());
        TestManager.setNewSubTask(sub3, epic2.getId());

    }
    @Test
    public void HistoryAddRemoveTest()
    {
        TestManager.getTaskByID(epic2.getId()).toString();

        Assert.assertTrue(TestManager.historyManager.getHistory().contains(epic2));
        TestManager.historyManager.remove(epic2.getId());

        Assert.assertFalse(TestManager.historyManager.getHistory().contains(epic2));

    }
    @Test
    public void TaskToStringTests()
    {
        System.out.println(task1);
        System.out.println(epic1);
        System.out.println(sub1);
    }
    @Test
    public void FileManagerCreateAndSaveTest()
    {

        try {
            FileBackedTasksManager TestFileManager =
                    new FileBackedTasksManager(new File("C:\\Users\\Sebatian Piererro\\FirstHomework\\third\\java-kanban\\data", "ManagerHistory.txt"));
            TestFileManager.save();
        }
        catch (IOException e)
        {
            System.out.println("Файл сохранения не обнаружен");
        }

    }
    @Test
    public void FileCreateAddAndSaveTest()
    {
        try {
            FileBackedTasksManager TestFileManager =
                    new FileBackedTasksManager(new File("C:\\Users\\Sebatian Piererro\\FirstHomework\\third\\java-kanban\\data", "ManagerHistory.txt"));
            TestFileManager.setNewTask(new Task("Обычный таск",String.format("Должен попасть в cохранение под номером %d", TestFileManager.getLastID()+1), Status.NEW) );

            TestFileManager.setNewEpicTask(new EpicTask("Проверочный эпик",
                    String.format("Должен попасть в cохранение под номером %d и статус InProgress ", TestFileManager.getLastID()+1),
                    Status.NEW));
            TestFileManager.getTaskByID(TestFileManager.getLastID()).setStatus(Status.IN_PROGRESS);
            TestFileManager.setNewSubTask(new SubTask("Проверочный саб",
                    String.format("Должен попасть в cохранение под номером %d", TestFileManager.getLastID()+1),
                    Status.NEW), TestFileManager.getLastID());
            TestFileManager.setNewSubTask(new SubTask("Проверочный саб",
                    new String ("Не должен попасть в cохранение под номером" + TestFileManager.getLastID()+1),
                    Status.NEW), TestFileManager.getLastID());
            TestFileManager.save();
        }
        catch (IOException e)
        {
            System.out.println("Файл сохранения не обнаружен");
        }
    }


}

