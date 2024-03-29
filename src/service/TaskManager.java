package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;

public interface TaskManager {

    public Task createTask(Task task);
    public Epic createEpic(Epic epic);
    public SubTask createSubTask(SubTask subTask);
    public void updateTask(Task task);
    public void updateEpic(Epic epic);
    public void updateSubTask(SubTask subTask);
    public void deleteTask(int id);
    public void deleteEpic(int id);
    public void deleteSubTask(int id);
    public void deleteAllTasks();
    public void deleteAllEpics();
    public void deleteAllSubTasks();
    public void deleteAll();
    public Task getTask(int id);
    public Epic getEpic(int id);
    public SubTask getSubTask(int id);
    public List<Task> getAllTasks();
    public List<Epic> getAllEpics();
    public List<SubTask> getAllSubTasks();
    public List<Task> getHistory();
    public Set<Task> getPrioritizedTasks();
    public  List<SubTask> getTheSubTasksEpic(int idEpic) throws FileNotFoundException;
    public List<Object> getAll();

}
