package service;

import com.google.gson.Gson;
import model.Epic;
import model.SubTask;
import model.Task;

import java.io.IOException;
import java.net.URI;

public class HttpTaskManager extends InMemoryTaskManager{

     KVTaskClient kvTaskClient;
     Gson gson;


    public HttpTaskManager(URI url) {
        gson = new Gson();
        try {
            kvTaskClient = new KVTaskClient(url);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void start(){
        kvTaskClient.start();
    }
    public void stop(){
        kvTaskClient.stop();
    }

    @Override
    public Task createTask(Task task) {
        Task task1 = super.createTask(task);
        kvTaskClient.put(Integer.toString(task1.getId()), gson.toJson(task1));
        return task1;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic epic1 = super.createEpic(epic);
        kvTaskClient.put(Integer.toString(epic1.getId()), gson.toJson(epic1));
        return epic1;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        SubTask subTask1 = super.createSubTask(subTask);
        kvTaskClient.put(Integer.toString(subTask1.getId()), gson.toJson(subTask1));
        return subTask1;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        kvTaskClient.put(Integer.toString(task.getId()), gson.toJson(task));

    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        kvTaskClient.put(Integer.toString(epic.getId()), gson.toJson(epic));
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        kvTaskClient.put(Integer.toString(subTask.getId()), gson.toJson(subTask));
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
    }

    @Override
    public Task getTask(int id) {
        return gson.fromJson(kvTaskClient.load(Integer.toString(id)), Task.class);
    }

    @Override
    public Epic getEpic(int id) {
        return gson.fromJson(kvTaskClient.load(Integer.toString(id)), Epic.class);
    }

    @Override
    public SubTask getSubTask(int id) {
        return gson.fromJson(kvTaskClient.load(Integer.toString(id)), SubTask.class);
    }


}
