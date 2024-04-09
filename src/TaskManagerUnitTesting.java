
import org.junit.Assert;
import service.*;
import model.*;
import java.io.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.*;
public class TaskManagerUnitTesting {

    private InMemoryTaskManager TestManager = new InMemoryTaskManager();// экземпляр тестового менеджера
    ///Создание тестовых тасков:
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


}

