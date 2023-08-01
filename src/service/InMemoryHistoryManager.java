package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    private ArrayList<Task> taskHistory = new ArrayList<>();

    @Override
    public ArrayList<Task> getHistory() {
            return taskHistory;
    }

    @Override
    public void addToHistory(Task task) {
        taskHistory.add(task);
        if (taskHistory.size() == HISTORY_LEN)
            taskHistory.remove(0);

    }
}
