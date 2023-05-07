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



    public void updateTask(Task task) {
        task.setStatus(task.getStatus());
       tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getEpicId(), subTask);
        updateEpicStatus(epics.get(subTask.getEpicId()));
    }


    public void deleteTask(int id) { tasks.remove(id);}

    public void deleteEpic(int id){
        for (int i = 0; i < epics.get(id).getSubTasksId().size(); i++) {
            subTasks.remove(epics.get(id).getSubTasksId().get(i));
        }
        epics.remove(id);
    }

    public void deleteSubTask(int id){
        epics.get(subTasks.get(id).getEpicId()).getSubTasksId().remove(Integer.valueOf(id));
        updateEpicStatus(epics.get(subTasks.get(id).getEpicId()));
    }

    public void deleteAllTask(){
        tasks.clear();
    }

    public void deleteAllEpic(){
        epics.clear();
        subTasks.clear();
    }

    public void deleteAllSubTask(){
        subTasks.clear();
        for (Epic epic : epics.values()){
            epic.setStatus("new");
            epic.getSubTasksId().clear();
        }
    }
    public void deleteAll(){
        tasks.clear();
        epics.clear();
        subTasks.clear();
    }


   public List<Task> getAllTask(){
        return new ArrayList<>(tasks.values());
   }

    public List<Epic> getAllEpic(){
        return new ArrayList<>(epics.values());
    }

    public List<SubTask> getAllSubTask(){
        return new ArrayList<>(subTasks.values());
    }

    public  List<SubTask> getTheSubTasksEpic(int idEpic){
        ArrayList<SubTask> allTask = new ArrayList<>();
        for (Integer idSubTask : epics.get(idEpic).getSubTasksId()){
            allTask.add(subTasks.get(idSubTask));
        }
        return allTask;
    }
    public List<Object> getAll(){
        ArrayList<Object> allTask = new ArrayList<>();
        allTask.add(new ArrayList<>(tasks.values()));
        allTask.add(new ArrayList<>(epics.values()));
        allTask.add(new ArrayList<>(subTasks.values()));
        return allTask;
    }


    private void updateEpicStatus(Epic epic){
        int countDone = 0;
        int countNew = 0;
        if (!epic.getSubTasksId().isEmpty()) {
            for (Integer subTaskId : epic.getSubTasksId()) {
                if (subTasks.get(subTaskId).getStatus().equals("Done")) {
                    countDone++;
                } else if (subTasks.get(subTaskId).getStatus().equals("new")) {
                    countNew++;
                }
            }
            if (countDone == epic.getSubTasksId().size()) {
                epic.setStatus("Done");
            } else if (countNew == epic.getSubTasksId().size()) {
                epic.setStatus("new");
            } else {
                epic.setStatus("In_progress");
            }
        }
    }

}
