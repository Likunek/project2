package service;

import Model.Epic;
import Model.SubTask;
import Model.Task;

import java.util.ArrayList;
import java.util.List;

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
    public void deleteAllTask();
    public void deleteAllEpic();
    public void deleteAllSubTask();
    public void deleteAll();
    public Task getTask(int id);
    public Epic getEpic(int id);
    public SubTask getSubTask(int id);
    public List<Task> getAllTask();
    public List<Epic> getAllEpic();
    public List<SubTask> getAllSubTask();
    public  List<SubTask> getTheSubTasksEpic(int idEpic);
    public List<Object> getAll();

}
