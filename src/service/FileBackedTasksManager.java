package service;

import Model.Epic;
import Model.Status;
import Model.SubTask;
import Model.Task;

import java.io.*;
import java.util.*;




public class FileBackedTasksManager extends InMemoryTaskManager {

    static File dir;

    public static void main(String[] args) throws IOException {


        File dir1 = new File("file.scv");

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(dir1);

        //Создаю эпик с двумя подзадачами
        Epic epic =  fileBackedTasksManager.createEpic(new Epic("make a pie", "by 6 pm"));
        fileBackedTasksManager.createSubTask(new SubTask("dough", "knead the dough", epic.getId()));
        fileBackedTasksManager.createSubTask(new SubTask("filling", "cook the filling", epic.getId()));
        fileBackedTasksManager.createSubTask(new SubTask("bake", "put it in the oven", epic.getId()));

        System.out.println(epic + "\n" + epic.getSubTasksId());

        //Создаю эпик без под задач
        Epic epic2 =  fileBackedTasksManager.createEpic(new Epic("morning", "wash your hair"));
        System.out.println(epic2);

        //Запрашиваю задачи
        System.out.println(fileBackedTasksManager.getEpic(1));
        System.out.println(fileBackedTasksManager.getSubTask(3));
        System.out.println(fileBackedTasksManager.getSubTask(4));
        System.out.println(fileBackedTasksManager.getEpic(1));
        System.out.println(fileBackedTasksManager.getEpic(5));
        System.out.println(fileBackedTasksManager.getHistory());

        //Удаляю задачу
        fileBackedTasksManager.deleteSubTask(3);
        System.out.println(fileBackedTasksManager.getHistory());


        FileBackedTasksManager fileBacked =  fileBackedTasksManager.loadFromFile(dir1);
        System.out.println(fileBacked.getEpic(1));
        System.out.println(fileBackedTasksManager.getHistory());


    }



    public FileBackedTasksManager(File dir) {
       this.dir = dir;

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
            taskHistory = String.join(",", Integer.toString(task.getId()),Type.SUBTASK.toString(), task.getName(),
                        task.getStatus().toString(), task.getDescription(),
                        Integer.toString(((SubTask) task).getEpicId()), "\n");
        } else if (task instanceof Epic){
            taskHistory = String.join(",",Integer.toString(task.getId()),Type.EPIC.toString(),task.getName(),
                    task.getStatus().toString(), task.getDescription(),"\n");
            System.out.println(taskHistory);
        }else {
            taskHistory = String.join(",", Integer.toString(task.getId()), Type.TASK.toString(), task.getName(),
                    task.getStatus().toString(), task.getDescription(), "\n");
        }return taskHistory;
    }

    static Task fromString(String value){
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
                tasks.get(Integer.parseInt(task[0])).setId(Integer.parseInt(task[0]));
                tasks.get(Integer.parseInt(task[0])).setStatus(status);
                return tasks.get(Integer.parseInt(task[0]));
            case "EPIC":
                epics.put(Integer.parseInt(task[0]), new Epic(task[2], task[4]));
                epics.get(Integer.parseInt(task[0])).setId(Integer.parseInt(task[0]));
                epics.get(Integer.parseInt(task[0])).setStatus(status);
                return epics.get(Integer.parseInt(task[0]));
            case "SUBTASK":
                subTasks.put(Integer.parseInt(task[0]), new SubTask(task[2], task[4], Integer.parseInt(task[5])));
                subTasks.get(Integer.parseInt(task[0])).setId(Integer.parseInt(task[0]));
                subTasks.get(Integer.parseInt(task[0])).setStatus(status);
                return subTasks.get(Integer.parseInt(task[0]));
            default:
                System.out.println("Что-то пошло не так");
                return null;
        }

    }

    String historyToString(HistoryManager manager) {
        String listTask;
        List<String> tasks = new ArrayList<>();
        for (Task task : manager.getHistory()){
            tasks.add(Integer.toString(task.getId()));
        }
        listTask = String.join(",",tasks);
       return listTask;
    }


    static List<Integer> historyFromString(String value){
        List<Integer> number = new ArrayList<>();
        String[] tasks = value.split(",");
        for (String task : tasks) {
            number.add(Integer.parseInt(task));
        }

        return number;
    }

    FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        HashMap<Integer, Task> tasks = new HashMap<>();
        String line;
        int count = 0;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while ((line = bufferedReader.readLine()) != null) {
                if (count != 0) {
                    if (!line.isBlank()) {
                        Task task = fromString(line);
                        tasks.put(task.getId(), task);
                    } else {
                        String line2 = bufferedReader.readLine();
                        for (Integer i : historyFromString(line2)) {
                            if (tasks.containsKey(i)) {
                                super.historyManager.add(tasks.get(i));
                            }
                        }
                    }
                }count++;
        }  return fileBackedTasksManager;
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void save(){

         try (FileWriter csv = new FileWriter(dir)){
                csv.write("id,type,name,status,description,epic\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        try (FileWriter csvWriter = new FileWriter(dir, true)) {
               if (!tasks.isEmpty()){
               for (Integer i : tasks.keySet()){
                   csvWriter.write(toString(tasks.get(i)));
               }
               }
               if (!epics.isEmpty()){
                   for (Integer i : epics.keySet()){
                       csvWriter.write(toString(epics.get(i)));
                   }
               }
               if (!subTasks.isEmpty()){
                   for (Integer i : subTasks.keySet()){
                       csvWriter.write(toString(subTasks.get(i)));
                   }
               }


               csvWriter.write("\n" + historyToString(super.historyManager));

           } catch (IOException e) {
               throw new RuntimeException(e);
           }

    }
}
