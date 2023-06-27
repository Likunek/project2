import java.util.ArrayList;

import Model.Epic;
import Model.SubTask;
import Model.Task;
import service.Managers;
import service.TaskManager;

public class Main {
       public static void main(String[] args) {
           TaskManager taskManager = Managers.getDefaultTaskManager();

          //Создаю эпик с двумя подзадачами
           Epic epic =  taskManager.createEpic(new Epic("make a pie", "by 6 pm"));
           taskManager.createSubTask(new SubTask("dough", "knead the dough", epic.getId()));
           taskManager.createSubTask(new SubTask("filling", "cook the filling", epic.getId()));
           taskManager.createSubTask(new SubTask("bake", "put it in the oven", epic.getId()));

           System.out.println(epic + "\n" + epic.getSubTasksId());

           //Создаю эпик без под задач
           Epic epic2 =  taskManager.createEpic(new Epic("morning", "wash your hair"));
           System.out.println(epic2);

           //Запрашиваю задачи
           System.out.println(taskManager.getEpic(1));
           System.out.println(taskManager.getSubTask(3));
           System.out.println(taskManager.getSubTask(4));
           System.out.println(taskManager.getEpic(1));
           System.out.println(taskManager.getEpic(5));
           System.out.println(taskManager.getHistory());

           //Удаляю задачу
           taskManager.deleteSubTask(3);
           System.out.println(taskManager.getHistory());

           // Удаляю эпик с 3 подзадачами
           taskManager.deleteEpic(1);
           System.out.println(taskManager.getHistory());

       }
}