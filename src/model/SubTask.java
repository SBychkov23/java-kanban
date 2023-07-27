package model;
import service.TaskManager;

import java.util.Objects;

public class SubTask extends Task{

    int parentId;
    public SubTask(String name, String description, Status status) {
        super(name, description, status);
    }


    @Override
    public void setStatus(Status status)
    {
        this.status=status;
        TaskManager.updateEpicStatus(parentId);
    }
    @Override
    public String toString()
    {
        return "Тип таска: Sub  Название: "+name+" Описание: "+description+ " Статус: "+status +" Является частью Epic-таска "+
                TaskManager.tasksList.get(parentId).getName()+ "\n";
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId)
    {
        this.parentId = parentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return parentId == subTask.parentId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), parentId);
    }
}