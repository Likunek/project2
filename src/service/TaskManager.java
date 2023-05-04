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
        this.epics = new HashMap<>();
    }

    public Task create(Task task) {
        task.setStatus("new");
        task.setId(++seq);
          tasks.put(task.getId(), task);
          return  task;
    }

    public Epic createEpic(Epic epic){
        epic.setStatus("new");
        epic.setId(++seq);
        epics.put(epic.getId(), epic);
        for (int i = 0; i < epic.getSubTasks().size(); i++) {
            SubTask subTask = epic.getSubTasks().get(i);
            subTask.setEpic(epic);
            subTask.setId(++seq);
            subTask.setStatus("new");
        }
        return  epic;
    }

    public SubTask creatSubTask(SubTask subTask, int idEpic){
        Epic epic = subTask.getEpic();
        Epic saved = epics.get(epic.getId());
        subTask.setStatus("new");
        return subTask;
    }

    public Task get(int id){
       return tasks.get(id);
    }

    public void update(Task task) {
       tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        Epic saved = epics.get(epic.getId());
    }

    public void updateSubTask(SubTask subTask) {
        Epic epic = subTask.getEpic();
        Epic savedEpic = epics.get(epic.getId());
        for (int i = 0; i < savedEpic.getSubTasks().size(); i++) {
            if (subTask.getId() == epic.getSubTasks().get(i).getId()){
                epic.getSubTasks().add(subTask.getId(), subTask);
            } else {
                epic.getSubTasks().add(subTask);
            }
        }
        StatusEpic(subTask);
    }

    private void StatusEpic(SubTask subTask){
        Epic epic = subTask.getEpic();
        Epic saved = epics.get(epic.getId());
        int number = 0;
        int clean = 0;
            for (int i = 0; i < saved.getSubTasks().size(); i++) {
                if (saved.getSubTasks().get(subTask.getId()).getStatus().equals("Done")) {
                    number++;
                } else if (saved.getSubTasks().get(subTask.getId()).getStatus().equals("new")) {
                    clean++;
                }
            }
            if (number == saved.getSubTasks().size()) {
                saved.setStatus("Done");
            }else if (clean == saved.getSubTasks().size()){
                saved.setStatus("new");
            } else {
                saved.setStatus("In_progress");
            }

    }

    public void deleteEpic(int id){
        epics.get(id).getSubTasks().clear();
        epics.remove(id);
    }

    public void deleteSubTask(int id){

        for (Epic i : epics.values()) {
            for (int j = 0; j < i.getSubTasks().size(); j++) {
                i.getSubTasks().remove(i);
            }
        }
    }

    public List<Task> getAll(){
        return new ArrayList<>(tasks.values());
    }

    public void geleteAll(){
        tasks.clear();
        epics.clear();
    }

    public void delete(int id) { tasks.remove(id);}
}
