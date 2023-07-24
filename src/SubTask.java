public class SubTask extends Task{

    int parentId;
    public SubTask(String name, String description, int id, Status status, int parentId) {
        super(name, description, id, status);
        setParentId(parentId);
    }

    @Override
    public void setStatus(Status status)
    {
        this.status=status;
        if (TaskManager.tasksList.get(parentId).getStatus()
    }

    private void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
