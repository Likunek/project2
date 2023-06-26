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
           ArrayList<Integer> subTasks = new ArrayList<>();
           SubTask subTask1 = taskManager.createSubTask(new SubTask("dough", "knead the dough", epic.getId()));
           SubTask subTask2 = taskManager.createSubTask(new SubTask("filling", "cook the filling", epic.getId()));
           SubTask subTask3 = taskManager.createSubTask(new SubTask("bake", "put it in the oven", epic.getId()));
           subTasks.add(subTask1.getId());
           subTasks.add(subTask2.getId());
           subTasks.add(subTask3.getId());
           epic.setSubTasksId(subTasks);
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