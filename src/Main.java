import com.google.gson.Gson;
import model.Epic;
import service.HttpTaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        Gson gson = new Gson();
        HttpResponse<String> response;
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8070/register");

//       /HttpTaskServer httpTaskServer = new HttpTaskServer();
//        httpTaskServer.start();

        HttpTaskManager w = new HttpTaskManager(url);
        w.start();
        w.createEpic(new Epic("make a pie", "by 6 pm"));
        w.deleteEpic(1);


////        Создаю эпик с двумя подзадачами
//        HttpRequest request_1 = HttpRequest.newBuilder()
//                .uri(URI.create("http://localhost:8080/tasks/epic"))
//                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(new Epic("make a pie", "by 6 pm"))))
//                .build();
//        response = client.send(request_1, HttpResponse.BodyHandlers.ofString());
//        if (response.statusCode() == 200) {
//            JsonElement jsonElement = JsonParser.parseString(response.body());
//            if (!jsonElement.isJsonObject()) { // проверяем, точно ли мы получили JSON-объект
//                System.out.println("Ответ от сервера не соответствует ожидаемому.");
//                return;
//            }
//            JsonObject jsonObject = jsonElement.getAsJsonObject();
//            int epicId = jsonObject.get("id").getAsInt();
//
//
//            client.send(HttpRequest.newBuilder()
//                    .uri(URI.create("http://localhost:8080/tasks/subTask"))
//                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(
//                            new SubTask("dough", "knead the dough", "15.08.2023 - 14:00", 10, epicId))))
//                    .build(), HttpResponse.BodyHandlers.ofString());
//
//
//            client.send(HttpRequest.newBuilder()
//                    .uri(URI.create("http://localhost:8080/tasks/subTask"))
//                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(
//                            new SubTask("filling", "cook the filling", "15.08.2023 - 14:20", 20, epicId))))
//                    .build(), HttpResponse.BodyHandlers.ofString());
//
//            client.send(HttpRequest.newBuilder()
//                    .uri(URI.create("http://localhost:8080/tasks/subTask"))
//                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(
//                            new SubTask("bake", "put it in the oven", "15.08.2023 - 14:50", 40, epicId))))
//                    .build(), HttpResponse.BodyHandlers.ofString());
//
//            //Создаю эпик без под задач
//            client.send(HttpRequest.newBuilder()
//                    .uri(URI.create("http://localhost:8080/tasks/epic"))
//                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(new Epic("morning", "wash your hair"))))
//                    .build(), HttpResponse.BodyHandlers.ofString());


////           Запрашиваю задачи
//        HttpRequest request_2 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/epic?id=1")).GET().build();
//        response = client.send(request_2, HttpResponse.BodyHandlers.ofString());
//        if (response.statusCode() == 200) {
//            JsonElement jsonElement = JsonParser.parseString(response.body());
//            System.out.println(jsonElement);
//            if (!jsonElement.isJsonObject()) { // проверяем, точно ли мы получили JSON-объект
//                System.out.println("Ответ от сервера не соответствует ожидаемому.");
//                return;
//            }
            // преобразуем результат разбора текста в JSON-объект
//            JsonObject jsonObject = jsonElement.getAsJsonObject();
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


        }
    }

