package service;

public final class Managers {

    public TaskManager getDefault (){
        return new InMemoryTaskManager();
    }

}
