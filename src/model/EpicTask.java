package model;


import service.TaskManager;

import java.util.HashMap;
import java.util.Objects;

public class EpicTask extends Task {


    public HashMap<Integer, SubTask> childSubTasks = new HashMap<>();
    public EpicTask(String name, String description, Status status)
    {
        super(name, description, status);
    }

   @Override
    public Status getStatus()
    {
                    return status;

    }
    @Override
    public String toString()
    {
        return "Тип таска: Epic  Название: "+name+" Описание: "+description+ " Статус: "+status +" Sub-таски: "+ TaskManager.printSubs(id)+"\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EpicTask epicTask = (EpicTask) o;
        return Objects.equals(childSubTasks, epicTask.childSubTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), childSubTasks);
    }
}
