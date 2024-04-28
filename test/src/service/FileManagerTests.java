package service;

import Exceptions.NotEpicTaskException;
import Model.TasksModeleTests;
import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileManagerTests extends TasksModeleTests {


    @Test
    public void createAndSaveFileManagerTest() {

        try {
            FileBackedTasksManager TestFileManager =
                    new FileBackedTasksManager(new File("C:\\Users\\Sebatian Piererro\\FirstHomework\\third\\java-kanban\\data", "ManagerHistory.txt"));
            TestFileManager.save();
        } catch (IOException e) {
            System.out.println("Файл сохранения не обнаружен");
        }

    }



    @Test
    public void loadFromFileAddAndSaveTest() {

        try
        {
            FileBackedTasksManager TestFileManager =
                    new FileBackedTasksManager(new File("C:\\Users\\Sebatian Piererro\\FirstHomework\\third\\java-kanban\\data", "ManagerHistory.txt"));
            TestFileManager.removeAllTasks();
            Task testTask = new Task("Обычный таск", String.format("Должен попасть в cохранение под номером %d", 1), Status.NEW, 60);
            EpicTask testEpicTask = new EpicTask("Проверочный эпик",String.format("Должен попасть в cохранение под номером %d и статус InProgress ", 2),
                    Status.NEW);
            SubTask testSubTask1 = new SubTask("Проверочный саб",
                    String.format("Должен попасть в cохранение под номером %d", 3),
                    Status.NEW, 60);
            SubTask testSubTask2 = new SubTask("Проверочный саб 2",
                    String.format("Должен попасть в cохранение под номером %d", 4),
                    Status.NEW, 60);
            testTask.setStartTime("00:00 01.12.2013");
            testEpicTask.setStartTime("00:00 01.12.2014");
            testSubTask1.setStartTime("00:00 01.12.2015");
            TestFileManager.setNewTask(testTask);
            TestFileManager.setNewEpicTask(testEpicTask);
            TestFileManager.getTaskByID(testEpicTask.getId()).setStatus(Status.IN_PROGRESS);
            TestFileManager.setNewSubTask(testSubTask1, testEpicTask.getId());
            TestFileManager.setNewSubTask(testSubTask2, testEpicTask.getId());
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
