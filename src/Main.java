import Model.Task;
import service.TaskManager;

public class Main {
       public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();
        Task task = taskManager.create(new Task("New task"));
        System.out.println("Create task:" + task);

        Task taskFromManager = taskManager.get(task.getId());
           System.out.println("Get task" + taskFromManager);

           taskFromManager.setName("New name");
        taskManager.update(taskFromManager);
           System.out.println("Update task:" + taskFromManager);

        taskManager.delete(taskFromManager.getId());
           System.out.println("Delete" + task);

       }
}