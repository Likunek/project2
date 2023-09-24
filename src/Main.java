import com.google.gson.Gson;
import model.Epic;
import model.SubTask;
import service.HttpTaskServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
       public static void main(String[] args) throws IOException, InterruptedException {

           Gson gson = new Gson();
           HttpClient client = HttpClient.newHttpClient();
           HttpResponse<String> response;
           URI url = URI.create("http://localhost:8070/register");
           new HttpTaskServer(url);

           // Не работает взаимодейсвие создания и получения задач, не могу разобраться из-за чего

          //Создаю эпик с двумя подзадачами
           HttpRequest.newBuilder()
                   .uri(URI.create("http://localhost:8080/tasks/epic"))
                   .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(new Epic("make a pie", "by 6 pm"))))
                   .build();

           HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/epic?id=1")).GET().build();
           response = client.send(request, HttpResponse.BodyHandlers.ofString());
           int epicId = gson.fromJson(response.body(), Epic.class).getId();


           SubTask subTask_1 = new SubTask("dough", "knead the dough", epicId);
           subTask_1.settingTheTime(10, "15.08.2023, 14:00");
           HttpRequest.newBuilder()
                   .uri(URI.create("http://localhost:8080/tasks/subTask"))
                   .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subTask_1)))
                   .build();

           SubTask subTask_2 = new SubTask("filling", "cook the filling", epicId);
           subTask_2.settingTheTime(20, "15.08.2023, 14:20");
           HttpRequest.newBuilder()
                   .uri(URI.create("http://localhost:8080/tasks/subTask"))
                   .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subTask_2)))
                   .build();

           SubTask subTask_3 = new SubTask("bake", "put it in the oven", epicId);
           subTask_3.settingTheTime(40, "15.08.2023, 14:50");
           HttpRequest.newBuilder()
                   .uri(URI.create("http://localhost:8080/tasks/subTask"))
                   .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subTask_3)))
                   .build();



           //Создаю эпик без под задач
           HttpRequest.newBuilder()
                   .uri(URI.create("http://localhost:8080/tasks/epic"))
                   .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(new Epic("morning", "wash your hair"))))
                   .build();


//           Запрашиваю задачи
//           request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/epic?id=1")).GET().build();
//           response = client.send(request, HttpResponse.BodyHandlers.ofString());
//           System.out.println(taskManager.getEpic(1));
//           System.out.println(taskManager.getSubTask(3));
//           System.out.println(taskManager.getSubTask(4));
//           System.out.println(taskManager.getEpic(1));
//           System.out.println(taskManager.getEpic(5));
//           System.out.println(taskManager.getHistory());
//
//           //Удаляю задачу
//           taskManager.deleteSubTask(3);
//           System.out.println(taskManager.getHistory());
//
//           // Удаляю эпик с 3 подзадачами
//           taskManager.deleteEpic(1);
//           System.out.println(taskManager.getHistory());
//
//
//           taskManager.getAll();
//




       }
}