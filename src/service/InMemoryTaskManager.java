package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.time.Duration.ofMinutes;

public class InMemoryTaskManager implements TaskManager {
    public HashMap<Integer, Task> tasks;
    public HashMap<Integer, Epic> epics;
    public HashMap<Integer, SubTask> subTasks;
    final HistoryManager historyManager;
    static int seq = 0;
    protected Set<Task> tasksTime;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subTasks = new HashMap<>();
        this.historyManager = Managers.getDefaultHistoryManager();
        tasksTime = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    }

    @Override
    public Task createTask(Task task) {
        task.setId(++seq);
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
        if (prioritizedTasks(task)){
            task.setStartTime(null);
        } tasksTime.add(task);
        }
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
        if (subTask.getStartTime() != null) {
            if (prioritizedTasks(subTask)){
                subTask.setStartTime(null);
            }
            tasksTime.add(subTask);
            getEndTimeEpic(subTask);
        }
        return subTask;
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
        if (prioritizedTasks(task)){
            task.setStartTime(null);
        }tasksTime.add(task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        updateEpicStatus(epics.get(subTask.getEpicId()));
        if (subTask.getStartTime() != null) {
            if (prioritizedTasks(subTask)){
                subTask.setStartTime(null);
            }
            tasksTime.add(subTask);
            getEndTimeEpic(subTask);
        }
    }

    @Override
    public void deleteTask(int id) {
        tasksTime.remove(tasks.get(id));
        historyManager.remove(id);
        tasks.remove(id);
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
        tasksTime.remove(epics.get(id));
        historyManager.remove(id);
        epics.remove(id);
    }

    @Override
    public void deleteSubTask(int id){
        epics.get(subTasks.get(id).getEpicId()).getSubTasksId().remove(Integer.valueOf(id));
        updateEpicStatus(epics.get(subTasks.get(id).getEpicId()));
        historyManager.remove(id);
        if (subTasks.get(id).getStartTime() != null) {
            getEndTimeEpic(subTasks.get(id));
        }
        tasksTime.remove(subTasks.get(id));
        subTasks.remove(id);
    }

    @Override
    public void deleteAllTasks(){
        tasks.clear();
        tasksTime.removeIf(task -> !(task instanceof Epic || task instanceof SubTask));
        historyManager.getHistory().removeIf(task -> !(task instanceof Epic || task instanceof SubTask));
    }

    @Override
    public void deleteAllEpics(){
        epics.clear();
        subTasks.clear();
        tasksTime.removeIf(task -> task instanceof Epic);
        historyManager.getHistory().removeIf(task -> task instanceof Epic || task instanceof SubTask);
    }

    @Override
    public void deleteAllSubTasks(){
        subTasks.clear();
        for (Epic epic : epics.values()){
            epic.setStatus(Status.NEW);
            epic.getSubTasksId().clear();
        }
        tasksTime.removeIf(task -> task instanceof SubTask);
        historyManager.getHistory().removeIf(task -> task instanceof SubTask);
    }

    @Override
    public void deleteAll(){
        tasks.clear();
        epics.clear();
        subTasks.clear();
        historyManager.getHistory().clear();
        tasksTime.clear();
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
    @Override
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
        List<Object> allTask = new ArrayList<>();
        allTask.add(new ArrayList<>(tasks.values()));
        allTask.add(new ArrayList<>(epics.values()));
        allTask.add(new ArrayList<>(subTasks.values()));
        return allTask;
    }

    @Override
    public Set<Task> getPrioritizedTasks(){
        return tasksTime;
    }

    private boolean prioritizedTasks(Task task){
        if (!tasksTime.isEmpty()){
        for (Task task2 : tasksTime){
            if (task2.getStartTime().equals(task.getStartTime()) && (task2.getId() != task.getId())){
                System.out.println("Данное время в вашем расписании уже есть, поменяйте время старта\n" + task);
                return true;
            }
        }}return false;
    }

    private void getEndTimeEpic(SubTask subTask){
        LocalDateTime startTime = subTask.getStartTime();
        LocalDateTime endTime = subTask.getStartTime();
        int duration = 0;
        for (SubTask task : subTasks.values()){
            if (task.getEpicId() == subTask.getEpicId()){
                duration += task.getDuration().toMinutesPart();
                LocalDateTime time = task.getStartTime().plus(task.getDuration());
                if (time.isAfter(endTime)){
                    endTime = time;
                }
                if (task.getStartTime().isBefore(startTime)){
                    startTime = task.getStartTime();
                }
            }
        }
        epics.get(subTask.getEpicId()).setDuration(ofMinutes(duration));
        epics.get(subTask.getEpicId()).setStartTime(startTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm")));
        epics.get(subTask.getEpicId()).setEndTime(endTime);
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
