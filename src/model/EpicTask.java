package model;


import Service.TaskManager;

import java.util.HashMap;

public class EpicTask extends Task {


    public HashMap<Integer, SubTask> childSubTasks = new HashMap<>();
    public EpicTask(String name, String description, Status status)
    {
        super(name, description, status);
    }

   @Override
    public Status getStatus()
    {
        int statusDone=0;
        int statusNew=0;
        for (SubTask sub: childSubTasks.values()) {
            if (sub.getStatus().equals(Status.DONE))
                statusDone++;
            else if (sub.getStatus().equals(Status.NEW))
                statusNew++;
        }
        if (statusDone==childSubTasks.size()){
            setStatus(Status.DONE);
            return status;
        } else if (statusNew==childSubTasks.size()) {
            setStatus(Status.NEW);
            return status;
        }
        else {
            setStatus(Status.IN_PROGRESS);
            return status;
        }
    }
    @Override
    public String toString()
    {
        return "Тип таска: Epic  Название: "+name+" Описание: "+description+ " Статус: "+status +" Sub-таски: "+ TaskManager.printSubs(id)+"\n";
    }

}
