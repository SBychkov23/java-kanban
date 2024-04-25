
import Exceptions.NotEpicTaskException;
import Exceptions.TimeCrossException;
import org.junit.Assert;
import service.*;
import model.*;
import java.io.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;
import java.nio.file.*;

public class TaskManagerUnitTesting {

    private InMemoryTaskManager TestManager = new InMemoryTaskManager();// экземпляр тестового менеджера
    ///Создание тестовых тасков:
    Task task1 = new Task("Обычная задача1", "Проверить создание задачи", Status.NEW, 60);
    Task task2 = new Task("Обычная задача2", "Проверить создание задачи", Status.NEW, 60);
    EpicTask epic1 = new EpicTask("Первая эпик задача", "Проверить работу эпика", Status.NEW);
    EpicTask epic2 = new EpicTask("Вторая эпик задача", "Проверить работу эпика", Status.NEW);
    EpicTask epic3 = new EpicTask("Третья эпик задача", "Проверить работу эпика", Status.NEW);
    EpicTask epic4 = new EpicTask("4 эпик задача", "Проверить работу эпика", Status.NEW);
    SubTask sub1 = new SubTask("Первая подзадача", "Проверить работу саба", Status.NEW, 60);
    SubTask sub2 = new SubTask("Вторая подзадача", "Проверить работу саба", Status.NEW, 60);
    SubTask sub3 = new SubTask("Третья подзадача", "Проверить работу саба", Status.NEW, 65);



    @Test
    public void ManagerFillTest() throws NotEpicTaskException, IOException {
        //заполнение тестового менджера:
        task1.setStartTime("00:00 12.12.2011");
        TestManager.setNewTask(task1);
        epic1.setStartTime("00:00 12.12.2012");
        sub1.setStartTime("00:00 12.12.2014");
        epic2.setStartTime("00:00 12.12.2015");
        sub2.setStartTime("00:00 12.12.2016");
        TestManager.setNewEpicTask(epic1);
        TestManager.setNewSubTask(sub1, epic1.getId());
        TestManager.setNewEpicTask(epic2);
        TestManager.setNewSubTask(sub2, epic2.getId());
        sub3.setStartTime("00:00 12.12.2017");
        TestManager.setNewSubTask(sub3, epic2.getId());

    }
    @Test
    public void TaskToStringTests() {
        System.out.println("id,type,name,status,description,start time, duration, epic");
        for (Task task: TestManager.getTasksList().values())
       System.out.println(task);
    }

    @Test
    public void HistoryAddRemoveTest() {
        TestManager.removeAllTasks();
        TestManager.historyManager.clear();
        task1.setStartTime("00:00 12.12.2011");
        task2.setStartTime("00:00 12.12.2012");
        epic1.setStartTime("00:00 12.12.2013");
        TestManager.setNewTask(task1);
        TestManager.setNewTask(task2);
        TestManager.setNewTask(epic1);
        TestManager.getTaskByID(epic1.getId()).toString();
        TestManager.getTaskByID(task1.getId()).toString();
        TestManager.getTaskByID(task2.getId()).toString();
        Assert.assertTrue(TestManager.historyManager.getHistory().contains(epic1));// проверка добавления
        Assert.assertTrue(TestManager.historyManager.getHistory().contains(task1));//
        Assert.assertTrue(TestManager.historyManager.getHistory().contains(task2));//
        TestManager.historyManager.remove(epic1.getId());
        Assert.assertFalse(TestManager.historyManager.getHistory().contains(epic1));//удаление из начала
        TestManager.getTaskByID(epic1.getId()).toString();
        TestManager.historyManager.remove(epic1.getId());
        Assert.assertFalse(TestManager.historyManager.getHistory().contains(epic1));//удаление из конца
        TestManager.getTaskByID(epic1.getId()).toString();
        TestManager.historyManager.remove(task2.getId());
        Assert.assertFalse(TestManager.historyManager.getHistory().contains(task2));//удаление из середины
        TestManager.historyManager.remove(epic1.getId());
        TestManager.historyManager.remove(task1.getId());
        Assert.assertTrue(TestManager.historyManager.getHistory().isEmpty());// пустой список после удаления

    }



    @Test
    public void TaskPriorityListGetterCheck() throws IOException, NotEpicTaskException {


        task1.setStartTime("00:00 12.12.2009");
        sub1.setStartTime("00:00 12.12.2011");
        sub2.setStartTime("00:00 12.12.2010");
        Assert.assertTrue(task1.getStartTime().isBefore(epic1.getStartTime()));
        TestManager.getTasksList().clear();
        task1.setStartTime("00:00 12.12.2009");
        TestManager.setNewTask(task1);
        task2.setStartTime("00:00 12.12.2010");
        TestManager.setNewTask(task2);
        Assert.assertTrue(TestManager.getPrioritizedTasks().first().equals(task1)||TestManager.getPrioritizedTasks().last().equals(task2));
        sub1.setStartTime("00:00 12.12.2005");
        TestManager.setNewEpicTask(epic1);
        TestManager.setNewSubTask(sub1, epic1.getId());
        TestManager.setNewSubTask(sub2, epic1.getId());
        Assert.assertTrue(TestManager.getPrioritizedTasks().first().equals(epic1)||TestManager.getPrioritizedTasks().last().equals(task2));
//        for (Task task: TestManager.getTasksList().values())
//            System.out.println(task);
//        System.out.println("\n\n_______________________________\n\n");
//        for (Task task: TestManager.getPrioritizedTasks())
//        System.out.println(task);

    }

    @Test
    public void TasksStatusSwitchTests() throws NotEpicTaskException {
        task1.setStartTime("00:00 12.12.2011");
        epic1.setStartTime("00:00 12.12.2012");
        sub1.setStartTime("00:00 12.12.2014");
        epic2.setStartTime("00:00 12.12.2015");
        sub2.setStartTime("00:00 12.12.2016");
        sub3.setStartTime("00:00 12.12.2017");
        TestManager.setNewTask(task1);
        TestManager.setNewEpicTask(epic1);
        TestManager.setNewSubTask(sub1, epic1.getId());
        TestManager.setNewEpicTask(epic2);
        TestManager.setNewSubTask(sub2, epic2.getId());
        TestManager.setNewSubTask(sub3, epic2.getId());
        task1.setStatus(Status.IN_PROGRESS);
        Assert.assertEquals(Status.IN_PROGRESS, task1.getStatus());
        Assert.assertEquals(Status.NEW, epic1.getStatus());
        Assert.assertEquals(Status.NEW, epic2.getStatus());
        sub1.setStatus(Status.IN_PROGRESS);
        Assert.assertEquals(Status.IN_PROGRESS, epic1.getStatus());
        sub1.setStatus(Status.DONE);
        Assert.assertEquals(Status.DONE, epic1.getStatus());
        sub2.setStatus(Status.IN_PROGRESS);
        Assert.assertEquals(Status.IN_PROGRESS, epic2.getStatus());
        sub2.setStatus(Status.DONE);
        Assert.assertEquals(Status.IN_PROGRESS, epic2.getStatus());
        sub3.setStatus(Status.DONE);
        Assert.assertEquals(Status.DONE, epic2.getStatus());
    }

    @Test
    public void TaskCrossErrorTest() throws IOException {

        TestManager.getTasksList().clear();
        task1.setStartTime("00:00 12.12.2009");
        task2.setStartTime(task1.getStartTime().format(Task.timeFormat));
        TestManager.setNewTask(task1);

        TestManager.setNewTask(task2);
       Assert.assertTrue(TestManager.getTasksList().size()==1);
//        for (Task task: TestManager.getPrioritizedTasks())
//       System.out.println(task);
    }

    @Test
    public void FileManagerCreateAndSaveTest() {

        try {
            FileBackedTasksManager TestFileManager =
                    new FileBackedTasksManager(new File("C:\\Users\\Sebatian Piererro\\FirstHomework\\third\\java-kanban\\data", "ManagerHistory.txt"));
            TestFileManager.save();
        } catch (IOException e) {
            System.out.println("Файл сохранения не обнаружен");
        }

    }



    @Test
    public void FileCreateAddAndSaveTest() {


        try
        {

            FileBackedTasksManager TestFileManager =
                    new FileBackedTasksManager(new File("C:\\Users\\Sebatian Piererro\\FirstHomework\\third\\java-kanban\\data", "ManagerHistory.txt")); //файл не пуст при первом запуске
            Task testTask = new Task("Обычный таск", String.format("Должен попасть в cохранение под номером %d", TestFileManager.getLastID()+1), Status.NEW, 60);
            EpicTask testEpicTask = new EpicTask("Проверочный эпик",String.format("Должен попасть в cохранение под номером %d и статус InProgress ", TestFileManager.getLastID()+2),
                    Status.NEW);
            SubTask testSubTask1 = new SubTask("Проверочный саб",
                    String.format("Должен попасть в cохранение под номером %d", TestFileManager.getLastID()+3),
            Status.NEW, 60);
            testTask.setStartTime("00:00 01.12.2013");
            testEpicTask.setStartTime("00:00 01.12.2014");
            testSubTask1.setStartTime("00:00 01.12.2015");
            TestFileManager.setNewTask(testTask);
            TestFileManager.setNewEpicTask(testEpicTask);
            TestFileManager.getTaskByID(testEpicTask.getId()).setStatus(Status.IN_PROGRESS);
            TestFileManager.setNewSubTask(testSubTask1, testEpicTask.getId());
                Assert.assertThrows("Заданный таск не относится к типу Epic", NotEpicTaskException.class, ()-> TestFileManager.setNewSubTask(new SubTask("Проверочный саб",
                        new String("Не должен попасть в cохранение под номером" + TestFileManager.getLastID() + 4),
                        Status.NEW, 60), TestFileManager.getLastID()));


            TestFileManager.save();
        } catch ( FileNotFoundException e) {
            System.out.println(e.getMessage() + "Файл сохранения не обнаружен");
        }
        catch ( Exception e)
        {
            e.printStackTrace();
        }


    }
}

