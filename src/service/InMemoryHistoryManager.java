package service;

import Model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    ArrayList<Task> history;

    public InMemoryHistoryManager() {
        this.history = new ArrayList<>();
    }

    @Override
    public void add(Task task) {
        history.add(task);
        lenHistory();
    }

    @Override
    public List<Task> getHistory(){
        return history;
    }
    private void lenHistory(){
        if (history.size() > 10){
            history.remove(0);
        }
    }
}
