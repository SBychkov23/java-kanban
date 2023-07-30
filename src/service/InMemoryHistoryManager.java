package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    ArrayList<Task> taskHistory = new ArrayList<>();

    @Override
    public ArrayList<Task> getHistory() {
        {
            return taskHistory;
        }


    }

    @Override
    public void addToHistory(Task task) {
        {

            taskHistory.add(0,task);
            if (taskHistory.size()==HISTORY_LEN)
            taskHistory.remove(10);
        }
    }
}
