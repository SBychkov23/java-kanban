package service;

import Exceptions.NotEpicTaskException;
import Exceptions.TimeCrossException;
import Model.TasksModeleTests;
import model.Status;
import model.Task;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class MemoryManagerTests extends TasksModeleTests {

    InMemoryTaskManager TestManager;

    public MemoryManagerTests() throws TimeCrossException, NotEpicTaskException, IOException {
        setNewInMemoryManager();
    }

    @Test
    public void setNewInMemoryManager(){
        TestManager = new InMemoryTaskManager();// экземпляр тестового менеджера
    }

    @Test
    public void setTimeAndFillAndRemoveTaskManagerTest() throws NotEpicTaskException, IOException, TimeCrossException {

        //заполнение тестового менджера:
        InMemoryTaskManager TestManager1 = new InMemoryTaskManager();// экземпляр тестового менеджера
        TestManager.removeAllTasks();
        task1.setStartTime("00:00 12.12.2011");
        TestManager1.setNewTask(task1);
        task2.setStartTime("00:00 12.06.2012");
        TestManager1.addTask(TestManager1.getNewID(),task2);
        epic1.setStartTime("00:00 12.12.2012");
        sub1.setStartTime("00:00 12.12.2014");
        epic2.setStartTime("00:00 12.12.2015");
        sub2.setStartTime("00:00 12.12.2016");
        TestManager1.setNewEpicTask(epic1);
        TestManager1.setNewSubTask(sub1, epic1.getId());
        TestManager1.setNewEpicTask(epic2);
        TestManager1.setNewSubTask(sub2, epic2.getId());
        TestManager1.removeTaskByID(epic1.getId());
        Assert.assertFalse(TestManager1.getTasksList().containsKey(epic1.getId()));
        Assert.assertFalse(TestManager1.getTasksList().containsKey(sub1.getId()));
        TestManager1.removeTaskByID(sub2.getId());
        Assert.assertFalse(TestManager1.getTasksList().containsKey(sub2.getId()));
        sub3.setStartTime("00:00 12.12.2017");
        TestManager1.setNewSubTask(sub3, epic2.getId());
        TestManager1.removeAllTasks();
        Assert.assertTrue(TestManager1.getTasksList().isEmpty());
    }
    @Test
    public void taskToStringPrintTest() {
        System.out.println("id,type,name,status,description,start time, duration, epic");
        for (Task task: TestManager.getTasksList().values())
            System.out.println(task);
    }
    @Test
    public void addToHistoryAddRemoveTest() {
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
    public void getTaskPriorityTest() throws IOException, NotEpicTaskException {


        task1.setStartTime("00:00 12.12.2009");
        sub1.setStartTime("00:00 12.12.2011");
        sub2.setStartTime("00:00 12.12.2010");
        Assert.assertTrue(task1.getStartTime().isBefore(epic1.getStartTime()));
        TestManager.removeAllTasks();
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
    public void tasksStatusGetAndSwitchTests() throws NotEpicTaskException {
        TestManager.removeAllTasks();
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
        Assert.assertEquals(Status.IN_PROGRESS, TestManager.getTaskStatus(task1.getId()));
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
        TestManager.setNewTaskStatus(sub3.getId(), Status.DONE);
        Assert.assertEquals(Status.DONE, epic2.getStatus());
    }

    @Test
    public void taskCrossErrorTest() throws IOException {

        TestManager.removeAllTasks();
        task1.setStartTime("00:00 12.12.2009");
        task2.setStartTime(task1.getStartTime().format(Task.timeFormat));
        TestManager.setNewTask(task1);

        TestManager.setNewTask(task2);
        Assert.assertTrue(TestManager.getTasksList().size()==1);
//        for (Task task: TestManager.getPrioritizedTasks())
//       System.out.println(task);
    }
}
