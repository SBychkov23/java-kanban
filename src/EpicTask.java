import java.util.HashMap;

public class EpicTask extends Task{


    HashMap<Integer, SubTask> childSubTasks = new HashMap<>();
    public EpicTask(String name, String description, int id, Status status) {
        super(name, description, id, status);
    }

    @Override
    public void getStatus()
    {
        int done=0;
        ..

        for (SubTask sub: childSubTasks.values())
            if (sub.getStatus().equals(Status.DONE)


    }

    public void addToSubList (int SubId, SubTask sub)
    {
        childSubTasks.put(SubId, sub);
    }
}
