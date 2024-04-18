

import java.io.File;


public class Main {
    public static void main(String[] args) {

       // TaskManagerUnitTesting Test1 = new TaskManagerUnitTesting();


    }

    public static boolean isPalindromeLine(String str) {
        StringBuilder a = new StringBuilder(str.toLowerCase());
        while (a.indexOf(" ")>=0)
            a.deleteCharAt(a.indexOf(" "));
        StringBuilder b = new StringBuilder(a.reverse());
        b.reverse();
        System.out.println(a+"   "+ b);

        return a.toString().equals(b.toString());

    }
}




/* испытание работы файлового менеджера оставшееся от предыдущих версий ТЗ (появилисось из-за смены когорты)
try {
            FileBackedTasksManager manager = new FileBackedTasksManager(new File("C:/Users/Sebatian Piererro/FirstHomework/third/java-kanban/data/ManagerHistory.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // manager.setNewEpicTask(epic1);
//       manager.setNewEpicTask(epic2);
//        manager.setNewEpicTask(epic3);
//        manager.setNewSubTask(sub1, epic1.getId());
//        manager.setNewSubTask(sub2, epic1.getId());
//        manager.setNewSubTask(sub3, epic1.getId());
//        manager.setNewEpicTask(epic4);
//    manager.getTaskByID(epic1.getId());
//     manager.getTaskByID(epic2.getId());
//        manager.getTaskByID(epic3.getId());
//        manager.getTaskByID(epic4.getId());
//        manager.getTaskByID(sub1.getId());
//        manager.getTaskByID(sub2.getId());
//       manager.getTaskByID(epic2.getId());

 */