package tests;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.HttpTaskServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HttpTaskServerTest {
    Gson gson = new Gson();
    static HttpTaskServer httpServer;
    HttpClient client;


    @BeforeEach
    void startServer() throws IOException {
        client = HttpClient.newHttpClient();
        httpServer = new HttpTaskServer();
        httpServer.start();
    }
    @AfterEach
    void finishServer() {
        httpServer.stop();
    }


    @Test
    void createTask() throws IOException, InterruptedException {

        HttpRequest requestTask = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(new Task("make a pie", "by 6 pm","15.11.2023 - 14:20", 20))))
                .build();
        HttpResponse<String> responseTask = client.send(requestTask, HttpResponse.BodyHandlers.ofString());

        if (responseTask.statusCode() == 200) {
            JsonElement jsonElement = JsonParser.parseString(responseTask.body());
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String epicId = Integer.toString(jsonObject.get("id").getAsInt());


            assertNotNull(responseTask, "Задача не создается.");

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/task?id=" + epicId)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertNotNull(response, "Задача на возвращается.");
            assertEquals(responseTask.body(), response.body(), "Проблема взаимодействия клиент/сервер");

            HttpRequest requestDelete = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/task?id=" + epicId)).DELETE().build();
            HttpResponse<String> responseDelete = client.send(requestDelete, HttpResponse.BodyHandlers.ofString());
            assertEquals("Задача успешно удалена", responseDelete.body(), "Проблема взаимодействия клиент/сервер");
        }
    }


    @Test
    void createEpic () throws IOException, InterruptedException {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/tasks/epic"))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(new Epic("wash your hair", "by 7 pm"))))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonElement jsonElement = JsonParser.parseString(response.body());
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String epicId = Integer.toString(jsonObject.get("id").getAsInt());

            assertNotNull(response, "Задача не создается.");

            HttpRequest requestEpic = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/epic?id=" + epicId)).GET().build();
            HttpResponse<String> responseEpic = client.send(requestEpic, HttpResponse.BodyHandlers.ofString());

            assertNotNull(responseEpic, "Задача на возвращается.");
            assertEquals(response.body(), responseEpic.body(), "Проблема взаимодействия клиент/сервер");

            HttpRequest requestDelete = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/epic?id=" + epicId)).DELETE().build();
            HttpResponse<String> responseDelete = client.send(requestDelete, HttpResponse.BodyHandlers.ofString());
            assertEquals("Задача успешно удалена", responseDelete.body(), "Проблема взаимодействия клиент/сервер");
        }

    @Test
    void createSubTask() throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/epic"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(new Epic("wash your hair", "by 7 pm"))))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String epicId = Integer.toString(jsonObject.get("id").getAsInt());

        HttpRequest requestSubTask = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/subTask"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(new SubTask("wash your hair", "by 7 pm", Integer.parseInt(epicId)))))
                .build();
        HttpResponse<String> responseSubTask = client.send(requestSubTask, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement_2 = JsonParser.parseString(responseSubTask.body());
        JsonObject jsonObject_2 = jsonElement_2.getAsJsonObject();
        String subTaskId = Integer.toString(jsonObject_2.get("id").getAsInt());

         assertNotNull(responseSubTask, "Задача не создается.");

        HttpRequest request_3 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/subTask?id=" + subTaskId)).GET().build();
        HttpResponse<String> response_3 = client.send(request_3, HttpResponse.BodyHandlers.ofString());

        assertNotNull(response_3, "Задача на возвращается.");
        assertEquals(responseSubTask.body(), response_3.body(), "Проблема взаимодействия клиент/сервер");

        HttpRequest request_5 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/history")).GET().build();
        HttpResponse<String> response_5 = client.send(request_5, HttpResponse.BodyHandlers.ofString());

        assertNotNull(response_5, "Задача на возвращается.");

        HttpRequest requestDelete = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/subTask?id=" + subTaskId)).DELETE().build();
        HttpResponse<String> responseDelete = client.send(requestDelete, HttpResponse.BodyHandlers.ofString());
        assertEquals("Задача успешно удалена", responseDelete.body(), "Проблема взаимодействия клиент/сервер");
    }

}


