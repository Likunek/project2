package service;

import Model.Epic;
import Model.Status;
import Model.SubTask;
import Model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    HashMap<Integer, Task> tasks;
    HashMap<Integer, Epic> epics;
    HashMap<Integer, SubTask> subTasks;
    public InMemoryHistoryManager historyManager;
    int seq = 0;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.historyManager = new InMemoryHistoryManager();
    }



    @Override
    public Task createTask(Task task) {
        task.setId(++seq);
          tasks.put(task.getId(), task);
          return  task;
    }

    @Override
    public Epic createEpic(Epic epic){
        epic.setId(++seq);
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public SubTask createSubTask(SubTask subTask){
        subTask.setId(++seq);
        subTasks.put(subTask.getId(), subTask);
        updateEpicStatus(epics.get(subTask.getEpicId()));
        return subTask;
    }



    @Override
    public void updateTask(Task task) {
       tasks.put(task.getId(), task);
    }
    @Override

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        updateEpicStatus(epics.get(subTask.getEpicId()));
    }



    @Override
    public void deleteTask(int id) { tasks.remove(id);}

    @Override
    public void deleteEpic(int id){
        for (int i = 0; i < epics.get(id).getSubTasksId().size(); i++) {
            subTasks.remove(epics.get(id).getSubTasksId().get(i));
        }
        epics.remove(id);
    }
    @Override
    public void deleteSubTask(int id){
        epics.get(subTasks.get(id).getEpicId()).getSubTasksId().remove(Integer.valueOf(id));
        updateEpicStatus(epics.get(subTasks.get(id).getEpicId()));
    }

    @Override
    public void deleteAllTask(){
        tasks.clear();
    }

    @Override
    public void deleteAllEpic(){
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteAllSubTask(){
        subTasks.clear();
        for (Epic epic : epics.values()){
            epic.setStatus(Status.NEW);
            epic.getSubTasksId().clear();
        }
    }

    @Override
    public void deleteAll(){
        tasks.clear();
        epics.clear();
        subTasks.clear();
    }



    @Override
    public Task getTask(int id){
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        } else {
            System.out.println("Задача с данным id не найдена");
            return null;
        }
    }

    @Override
    public Epic getEpic(int id){
        if (epics.containsKey(id)) {
            historyManager.add(epics.get(id));
            return epics.get(id);
        } else {
            System.out.println("Epic с данным id не найден");
            return null;
        }

    }

    @Override
    public SubTask getSubTask(int id){
        if (subTasks.containsKey(id)) {
            historyManager.add(subTasks.get(id));
            return subTasks.get(id);
        } else {
            System.out.println("Подзадача с данным id не найдена");
            return null;
        }
    }

    @Override
    public List<Task> getAllTask(){
        return new ArrayList<>(tasks.values());
   }

    @Override
    public List<Epic> getAllEpic(){
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTask> getAllSubTask(){
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public  List<SubTask> getTheSubTasksEpic(int idEpic){
        ArrayList<SubTask> allTask = new ArrayList<>();
        for (Integer idSubTask : epics.get(idEpic).getSubTasksId()){
            allTask.add(subTasks.get(idSubTask));
        }
        return allTask;
    }

    @Override
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
                if (subTasks.get(subTaskId).getStatus().equals(Status.DONE)) {
                    countDone++;
                } else if (subTasks.get(subTaskId).getStatus().equals(Status.NEW)) {
                    countNew++;
                }
            }
            if (countDone == epic.getSubTasksId().size()) {
                epic.setStatus(Status.DONE);
            } else if (countNew == epic.getSubTasksId().size()) {
                epic.setStatus(Status.NEW);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }

}