public class SubTask extends Task{

    int parentId;
    public SubTask(String name, String description, Status status) {
        super(name, description, status);
    }


    @Override
    public void setStatus(Status status)
    {
        this.status=status;
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
}
