package service;

import model.Task;

import java.util.List;

public interface HistoryManager {

    public void add(Task task);
    public void remove(int id);
    public List<Task> getHistory();
}
