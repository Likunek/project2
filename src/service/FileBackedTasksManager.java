package service;

import model.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.time.Duration.ofMinutes;


public class FileBackedTasksManager extends InMemoryTaskManager {

    File dir;

    public static void main(String[] args) throws InterruptedException {


        File dir1 = new File("file.scv");

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(dir1);

        //Создаю эпик с двумя подзадачами
        Epic epic = fileBackedTasksManager.createEpic(new Epic("make a pie", "by 6 pm"));
        SubTask subTask1 = new SubTask("dough", "knead the dough", "15.08.2023 - 14:00", 10, epic.getId());
        fileBackedTasksManager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("filling", "cook the filling", "15.08.2023 - 14:20", 20, epic.getId());
        fileBackedTasksManager.createSubTask(subTask2);
        SubTask subTask3 = new SubTask("bake", "put it in the oven", "15.08.2023 - 14:50", 40, epic.getId());
        fileBackedTasksManager.createSubTask(subTask3);

        System.out.println(epic + "\n" + epic.getSubTasksId());

        //Создаю эпик без под задач
        Epic epic2 = fileBackedTasksManager.createEpic(new Epic("morning", "wash your hair"));
        System.out.println(epic2);

        //Запрашиваю задачи
        System.out.println(fileBackedTasksManager.getEpic(1));
        System.out.println(fileBackedTasksManager.getSubTask(3));
        System.out.println(fileBackedTasksManager.getSubTask(4));
        System.out.println(fileBackedTasksManager.getEpic(1));
        System.out.println(fileBackedTasksManager.getEpic(5));
        System.out.println(fileBackedTasksManager.getHistory());
        System.out.println(fileBackedTasksManager.getPrioritizedTasks());

        //Удаляю задачу
        fileBackedTasksManager.deleteSubTask(3);
        System.out.println(fileBackedTasksManager.getHistory());
       // System.out.println(fileBackedTasksManager.getSubTask(3));


       FileBackedTasksManager fileBacked = FileBackedTasksManager.loadFromFile(dir1);
        System.out.println(fileBacked.getEpic(1));
        System.out.println(fileBacked.getHistory());
       // Task task = fileBacked.createTask(new Task("go to the store", "make a list of products"));
        //System.out.println(fileBacked.getTask(6));

        System.out.println(fileBacked.getAll());






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
    public Epic createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
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
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        save();
    }


    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Set<Task> getPrioritizedTasks(){
        return tasksTime;
    }
    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = super.getSubTask(id);
        save();
        return subTask;
    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> tasks = super.getAllTasks();
        save();
        return tasks;
    }

    @Override
    public List<Epic> getAllEpics() {
        List<Epic> epics = super.getAllEpics();
        save();
        return epics;
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        List<SubTask> subTasks = super.getAllSubTasks();
        save();
        return subTasks;
    }


    @Override
    public List<SubTask> getTheSubTasksEpic(int idEpic) throws FileNotFoundException {
        List<SubTask> subTasks = super.getTheSubTasksEpic(idEpic);
        save();
        return subTasks;
    }

    @Override
    public List<Object> getAll() {
        List<Object> tasks = super.getAll();
        save();
        return tasks;
    }


    private static String taskToString(Task task) {

        String taskHistory;

        if (task instanceof SubTask) {
            taskHistory = String.join(",", Integer.toString(task.getId()), Type.SUBTASK.toString(), task.getName(),
                    task.getStatus().toString(), task.getDescription(), Integer.toString(((SubTask) task).getEpicId()));
            if (task.getStartTime() != null){
                taskHistory = String.join(",", taskHistory,
                        task.getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm")),
                        Long.toString(task.getDuration().toMinutes()),"\n");
            }else {
                taskHistory = String.join(",", taskHistory, "\n");
            }
        } else if (task instanceof Epic) {
            taskHistory = String.join(",", Integer.toString(task.getId()), Type.EPIC.toString(), task.getName(),
                    task.getStatus().toString(), task.getDescription(), "  ");
            if (task.getStartTime() != null){
                taskHistory = String.join(",", taskHistory,
                        task.getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm")),
                        Long.toString(task.getDuration().toMinutes()), task.getEndTime(),"\n");
            }else {
                taskHistory = String.join(",", taskHistory, "\n");
            }
        } else {
            taskHistory = String.join(",", Integer.toString(task.getId()), Type.TASK.toString(), task.getName(),
                    task.getStatus().toString(), task.getDescription(), "  ");
            if (task.getStartTime() != null){
                taskHistory = String.join(",", taskHistory,
                        task.getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm")),
                        Long.toString(task.getDuration().toMinutes()), "\n");
            }else {
                taskHistory = String.join(",", taskHistory, "\n");
            }
        }
        return taskHistory;
    }

    private Task fromString(String value) {
        String[] task = value.split(",");
        Status status = null;
        switch (task[3]) {
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
        switch (task[1]) {
            case "TASK":
                if (task.length > 6) {
                tasks.put(Integer.parseInt(task[0]), new Task(task[2], task[4], task[6], Integer.parseInt(task[7])));
                }else {
                    tasks.put(Integer.parseInt(task[0]), new Task(task[2], task[4]));
                }
                tasks.get(Integer.parseInt(task[0])).setId(Integer.parseInt(task[0]));
                tasks.get(Integer.parseInt(task[0])).setStatus(status);
                return tasks.get(Integer.parseInt(task[0]));
            case "EPIC":
                epics.put(Integer.parseInt(task[0]), new Epic(task[2], task[4]));
                epics.get(Integer.parseInt(task[0])).setId(Integer.parseInt(task[0]));
                epics.get(Integer.parseInt(task[0])).setStatus(status);
                if (task.length > 6) {
                    epics.get(Integer.parseInt(task[0])).setDuration(ofMinutes(Integer.parseInt(task[7])));
                    epics.get(Integer.parseInt(task[0])).setStartTime(task[6]);
                    epics.get(Integer.parseInt(task[0])).setEndTime(LocalDateTime.parse(task[8], DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm")));
                }
                return epics.get(Integer.parseInt(task[0]));
            case "SUBTASK":
                if (task.length > 6) {
                subTasks.put(Integer.parseInt(task[0]), new SubTask(task[2], task[4], task[6], Integer.parseInt(task[7]), Integer.parseInt(task[5])));
                }else {
                    subTasks.put(Integer.parseInt(task[0]), new SubTask(task[2], task[4], Integer.parseInt(task[5])));
                }
                subTasks.get(Integer.parseInt(task[0])).setId(Integer.parseInt(task[0]));
                subTasks.get(Integer.parseInt(task[0])).setStatus(status);
                return subTasks.get(Integer.parseInt(task[0]));
            default:
                System.out.println("Что-то пошло не так");
                return null;
        }

    }

    private String historyToString(HistoryManager manager) {
        String listTask;
        List<String> tasks = new ArrayList<>();
        for (Task task : manager.getHistory()) {
            tasks.add(Integer.toString(task.getId()));
        }
        listTask = String.join(",", tasks);
        return listTask;
    }


    private static List<Integer> historyFromString(String value) {
        List<Integer> number = new ArrayList<>();
        String[] tasks = value.split(",");
        for (String task : tasks) {
            number.add(Integer.parseInt(task));
        }
        return number;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        HashMap<Integer, Task> tasks = new HashMap<>();
        String line;
        int count = 0;
        int max = 0;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while ((line = bufferedReader.readLine()) != null) {
                if (count != 0) {
                    if (!line.isBlank()) {
                        Task task = fileBackedTasksManager.fromString(line);
                        assert task != null;
                        tasks.put(task.getId(), task);
                    } else {
                        String line2 = bufferedReader.readLine();
                        if (! line2.isBlank()) {
                            for (Integer i : historyFromString(line2)) {
                                if (tasks.containsKey(i)) {
                                    fileBackedTasksManager.historyManager.add(tasks.get(i));
                                    if (max < i) max = i;
                                }
                            }
                        }
                    }
                }
                count++;
            }
            seq = max;
            return fileBackedTasksManager;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void save() {

        try (FileWriter csv = new FileWriter(dir)) {
            csv.write("id,type,name,status,description,epic,duration,start,endTime\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try (FileWriter csvWriter = new FileWriter(dir, true)) {
            if (!tasks.isEmpty()) {
                for (Integer i : tasks.keySet()) {
                    csvWriter.write(taskToString(tasks.get(i)));
                }
            }
            if (!epics.isEmpty()) {
                for (Integer i : epics.keySet()) {
                    csvWriter.write(taskToString(epics.get(i)));
                }
            }
            if (!subTasks.isEmpty()) {
                for (Integer i : subTasks.keySet()) {
                    csvWriter.write(taskToString(subTasks.get(i)));
                }
            }


            csvWriter.write("\n" + historyToString(historyManager));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
