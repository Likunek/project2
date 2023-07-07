package service;

import Model.Epic;
import Model.Status;
import Model.SubTask;
import Model.Task;

import java.io.*;
import java.util.*;



public class FileBackedTasksManager extends InMemoryTaskManager {

    File dir = new File("csv.txt");
    HistoryManager historyManager = new InMemoryHistoryManager();
    Comparator<Integer> userComparator = new Comparator<>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return  o1 - o2;
        }
    };

    Map<Integer, String> users;


    public FileBackedTasksManager(File dir) {
       this.dir = dir;
        users = new TreeMap<>(userComparator);
    }

    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save();
        return task;
    }

    @Override
    public Epic createEpic(Epic epic){
        super.createEpic(epic);
        save();
        return epic;
    }

    @Override
    public SubTask createSubTask(SubTask subTask){
        super.createSubTask(subTask);
        save();
        return subTask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }
    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }


    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id){
        super.deleteEpic(id);
        save();
    }
    @Override
    public void deleteSubTask(int id){
        super.deleteSubTask(id);
        save();
    }

    @Override
    public void deleteAllTasks(){
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubTasks(){
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void deleteAll(){
        super.deleteAll();
        save();
    }


    @Override
    public Task getTask(int id){
        super.getTask(id);
        save();
        return super.getTask(id);
    }

    @Override
    public Epic getEpic(int id){
        super.getEpic(id);
        save();
        return super.getEpic(id);
    }

    @Override
    public SubTask getSubTask(int id){
        super.getSubTask(id);
        save();
        return super.getSubTask(id);
    }

    @Override
    public List<Task> getAllTasks(){
        super.getAllTasks();
        save();
        return super.getAllTasks();
    }

    @Override
    public List<Epic> getAllEpics(){
        super.getAllEpics();
        save();
        return super.getAllEpics();
    }

    @Override
    public List<SubTask> getAllSubTasks(){
        super.getAllSubTasks();
        save();
        return super.getAllSubTasks();
    }

    public List<Task> getHistory() {
        super.getHistory();
        save();
        return super.getHistory();
    }

    @Override
    public List<SubTask> getTheSubTasksEpic(int idEpic) throws FileNotFoundException {
        super.getTheSubTasksEpic(idEpic);
        save();
        return super.getTheSubTasksEpic(idEpic);
    }

    @Override
    public List<Object> getAll(){
        super.getAll();
        save();
        return super.getAll();
    }


    String toString(Task task){
        String taskHistory;
        if (task instanceof SubTask){
            taskHistory = String.join(Integer.toString(task.getId()),Type.SUBTASK.toString(), task.getName(),
                        task.getStatus().toString(), task.getDescription(),
                        Integer.toString(((SubTask) task).getEpicId()), "\n");
        } else if (task instanceof Epic){
            taskHistory = String.join(Integer.toString(task.getId()),Type.EPIC.toString(), task.getName(),
                    task.getStatus().toString(), task.getDescription(), "\n");
        }else {
            taskHistory = String.join(Integer.toString(task.getId()), Type.TASK.toString(), task.getName(),
                    task.getStatus().toString(), task.getDescription(), "\n");
        }return taskHistory;
    }

    Task fromString(String value){
        String[] task = value.split(",");
        Status status = null;
        switch (task[3]){
            case "NEW":
                status = Status.NEW;
                break;
            case "IN_PROGRESS":
                status = Status.IN_PROGRESS;
                break;
            case "DONE":
                status = Status.DONE;
                break;
        }
        switch (task[1]){
            case "TASK":
                tasks.put(Integer.parseInt(task[0]), new Task(task[2], task[4]));
                tasks.get(Integer.parseInt(task[0])).setStatus(status);
                return tasks.get(Integer.parseInt(task[0]));
            case "EPIC":
                epics.put(Integer.parseInt(task[0]), new Epic(task[2], task[4]));
                epics.get(Integer.parseInt(task[0])).setStatus(status);
                return epics.get(Integer.parseInt(task[0]));
            case "SUBTASK":
                subTasks.put(Integer.parseInt(task[0]), new SubTask(task[2], task[4], Integer.parseInt(task[5])));
                subTasks.get(Integer.parseInt(task[0])).setStatus(status);
                return subTasks.get(Integer.parseInt(task[0]));
            default:
                System.out.println("Что-то пошло не так");
                return null;
        }

    }

    String historyToString(HistoryManager manager) {
        String listTask = null;
        for (Task task : manager.getHistory()){
            listTask += Integer.toString(task.getId());
        }
       return listTask;
    }


    //static List<Integer> historyFromString(String value){

    //}

    private void save(){

           try (FileWriter csvWriter = new FileWriter("csv.txt")) {
               csvWriter.write("id,type,name,status,description,epic\n");

               if (!tasks.isEmpty()){
               for (Integer i : tasks.keySet()){
                   users.put(i, toString(tasks.get(i)));
               }
               }
               if (!epics.isEmpty()){
                   for (Integer i : epics.keySet()){
                       users.put(i, toString(epics.get(i)));
                   }
               }
               if (!subTasks.isEmpty()){
                   for (Integer i : subTasks.keySet()){
                       users.put(i, toString(subTasks.get(i)));
                   }
               }
               for (Integer u : users.keySet()) {
                    csvWriter.write(users.get(u));
               }

               csvWriter.write("\n" + historyToString((HistoryManager) historyManager.getHistory()));

           } catch (IOException e) {
               throw new RuntimeException(e);
           }

    }
}
