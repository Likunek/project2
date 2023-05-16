import java.util.ArrayList;

import Model.Epic;
import Model.Status;
import Model.SubTask;
import Model.Task;
import service.Managers;
import service.TaskManager;

public class Main {
       public static void main(String[] args) {
           TaskManager taskManager = Managers.getDefaultTaskManager();
        // Создаю задачу и пишу ее описание
        Task task = taskManager.createTask(new Task("New task", "home work"));
        System.out.println("Create task:" + task);

        // Меняю содержание задачи и ее статус
           task.setDescription("Clean home");
           task.setStatus(Status.IN_PROGRESS);
           taskManager.updateTask(task);
           System.out.println("Update task:" + task);
           System.out.println(taskManager.getTask(1));


        //Удаляю задачу
           taskManager.deleteTask(task.getId());
           System.out.println("Delete:" + task);


        //Создаю эпик с двумя подзадачами
           Epic epic =  taskManager.createEpic(new Epic("make a pie", "by 6 pm"));
           ArrayList<Integer> subTasks = new ArrayList<>();
           SubTask subTask1 = taskManager.createSubTask(new SubTask("dough", "knead the dough", epic.getId()));
           SubTask subTask2 = taskManager.createSubTask(new SubTask("filling", "cook the filling", epic.getId()));
           subTasks.add(subTask1.getId());
           subTasks.add(subTask2.getId());
           epic.setSubTasksId(subTasks);
           System.out.println(epic + "\n" + epic.getSubTasksId());

        //Меняю одну подзадачу эпика

        taskManager.updateSubTask(new SubTask("bake", "put it in the oven", epic.getId()));

           System.out.println(taskManager.getEpic(2));
           System.out.println(taskManager.getAll());
           System.out.println(taskManager.getHistory());
           taskManager.deleteHistory(2);
           System.out.println(taskManager.getHistory());

       }
}