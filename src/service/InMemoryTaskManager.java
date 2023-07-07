package service;

import Model.Epic;
import Model.Status;
import Model.SubTask;
import Model.Task;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class InMemoryTaskManager implements TaskManager {
    HashMap<Integer, Task> tasks;
    HashMap<Integer, Epic> epics;
    HashMap<Integer, SubTask> subTasks;
    private final HistoryManager historyManager;
    int seq = 0;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.historyManager = Managers.getDefaultHistoryManager();
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
        epics.get(subTask.getEpicId()).addSubtaskId(subTask.getId());
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
    public void deleteTask(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpic(int id){
        HashMap<Integer, SubTask> cope = new HashMap<>(subTasks);
        for (SubTask subTask : cope.values()){
           if (subTask.getEpicId() == id){
               historyManager.remove(subTask.getId());
               subTasks.remove(subTask.getId());
           }
        }
        historyManager.remove(id);
        epics.remove(id);
    }
    @Override
    public void deleteSubTask(int id){
        epics.get(subTasks.get(id).getEpicId()).getSubTasksId().remove(Integer.valueOf(id));
        updateEpicStatus(epics.get(subTasks.get(id).getEpicId()));
        historyManager.remove(id);
        subTasks.remove(id);
    }

    @Override
    public void deleteAllTasks(){
        tasks.clear();
        historyManager.getHistory().removeIf(task -> !(task instanceof Epic || task instanceof SubTask));
    }

    @Override
    public void deleteAllEpics(){
        epics.clear();
        subTasks.clear();
        historyManager.getHistory().removeIf(task -> task instanceof Epic || task instanceof SubTask);
    }

    @Override
    public void deleteAllSubTasks(){
        subTasks.clear();
        for (Epic epic : epics.values()){
            epic.setStatus(Status.NEW);
            epic.getSubTasksId().clear();
        }
        historyManager.getHistory().removeIf(task -> task instanceof SubTask);
    }

    @Override
    public void deleteAll(){
        tasks.clear();
        epics.clear();
        subTasks.clear();
        historyManager.getHistory().clear();
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
    public List<Task> getAllTasks(){
        return new ArrayList<>(tasks.values());
   }

    @Override
    public List<Epic> getAllEpics(){
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTask> getAllSubTasks(){
        return new ArrayList<>(subTasks.values());
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public  List<SubTask> getTheSubTasksEpic(int idEpic) throws FileNotFoundException {
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
