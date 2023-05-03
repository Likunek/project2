package service;

import Model.Epic;
import Model.SubTask;
import Model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    HashMap<Integer,Task> tasks;
    HashMap<Integer, Epic> epics;
    int seq = 0;

    public TaskManager() {
        this.tasks = new HashMap<>();
    }

    public Task create(Task task) {
        task.setId(++seq);
          tasks.put(task.getId(), task);
          return  task;
    }

    public Task get(int id){
       return tasks.get(id);
    }

    public void update(Task task) {
       tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        Epic saved = epics.get(epic.getId());
        saved.setName(epic.getName());
    }

    public void updateSubTask(SubTask subTask) {
        Epic epic = subTask.getEpic();
        Epic savedEpic = epics.get(epic.getId());
    }

    public List<Task> getAll(){
        return new ArrayList<>(tasks.values());
    }

    public void delete(int id) {
      tasks.remove(id);
    }
}
