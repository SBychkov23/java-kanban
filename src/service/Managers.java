package service;

public class Managers {

    public static int[] history = new int[10];
    public TaskManager getDefault (){
        return new InMemoryTaskManager();
    }
    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}
