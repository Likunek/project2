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
    HashMap<Integer, SubTask> subTasks;
    int seq = 0;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
    }



    public Task createTask(Task task) {
        task.setId(++seq);
          tasks.put(task.getId(), task);
          return  task;
    }

    public Epic createEpic(Epic epic){
        epic.setId(++seq);
        epics.put(epic.getId(), epic);
        return epic;
    }

    public SubTask createSubTask(SubTask subTask){
        subTask.setId(++seq);
        subTasks.put(subTask.getId(), subTask);
        updateEpicStatus(epics.get(subTask.getEpicId()));
        return subTask;
    }



    public void update(Task task, String status) {
        task.setStatus(status);
       tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic, String status) {
        epic.setStatus(status);
        epics.put(epic.getId(), epic);
    }

    public void updateSubTask(SubTask subTask, String status) {
        subTask.setStatus(status);
        subTasks.put(subTask.getId(), subTask);
        updateEpicStatus(epics.get(subTask.getEpicId()));
    }


    private void updateEpicStatus(Epic epic){
        int countDone = 0;
        int countNew = 0;
            for (int i = 0; i < epic.getSubTasksId().size(); i++) {
                if (subTasks.get(epic.getSubTasksId().get(i)).getStatus().equals("Done")) {
                    countDone++;
                } else if (subTasks.get(epic.getSubTasksId().get(i)).getStatus().equals("new")) {
                    countNew++;
                }
            }
            if (countDone == epic.getSubTasksId().size()) {
                epic.setStatus("Done");
            }else if (countNew == epic.getSubTasksId().size()){
                epic.setStatus("new");
            }else {
              epic.setStatus("In_progress");
            }
    }


    public void deleteTask(int id) { tasks.remove(id);}

    public void deleteEpic(int id){
        for (int i = 0; i < epics.get(id).getSubTasksId().size(); i++) {
            subTasks.remove(epics.get(id).getSubTasksId().get(i));
        }
        epics.remove(id);
    }

    public void deleteSubTask(int id){
        for (Epic i : epics.values()) {
            i.getSubTasksId().remove(id);
        }
        subTasks.remove(id);
    }


    public List<Object> getAllTasks(){
        ArrayList<Object> allTask = new ArrayList<>();
        allTask.add(new ArrayList<>(tasks.values()));
        allTask.add(new ArrayList<>(epics.values()));
        allTask.add(new ArrayList<>(subTasks.values()));
        return allTask;
    }

    public void geleteAllTask(){
        tasks.clear();
        epics.clear();
        subTasks.clear();
    }
}
