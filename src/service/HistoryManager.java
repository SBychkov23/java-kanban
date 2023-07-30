package service;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public interface HistoryManager {

    int HISTORY_LEN =10;

    ArrayList<Task> getHistory();

      void addToHistory(Task task);

}
