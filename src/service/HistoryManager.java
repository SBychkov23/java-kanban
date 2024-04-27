package service;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public interface HistoryManager {



    ArrayList<Task> getHistory();

      void addToHistory(Task task);

    void remove(int id);

    void clear();

}
