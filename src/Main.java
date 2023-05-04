import java.util.ArrayList;
import java.util.Scanner;

import Model.Epic;
import Model.SubTask;
import Model.Task;
import service.TaskManager;

public class Main {
       public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        // Создаю задачу и пишу ее описание
        Task task = taskManager.create(new Task("New task", "home work"));
        System.out.println("Create task:" + task);

        // Меняю содержание задачи и ее статус
           task.setDescription("Clean home");
           task.setStatus("in_progress");
        taskManager.update(task);
           System.out.println("Update task:" + task);

        //Удаляю задачу
        taskManager.delete(task.getId());
           System.out.println("Delete:" + task);


        //Создаю эпик с двумя подзадачами
           Epic epic = new Epic("make a pie", "by 6 pm");
           ArrayList<SubTask> subTasks = new ArrayList<>();
           subTasks.add(new SubTask("dough", "knead the dough", epic));
           subTasks.add(new SubTask("filling", "cook the filling", epic));
           epic.setSubTasks(subTasks);
           taskManager.createEpic(epic);
           System.out.println(epic);

        //Меняю одну подзадачу эпика
        taskManager.updateSubTask(new SubTask("bake", "put it in the oven", epic));

       }
}