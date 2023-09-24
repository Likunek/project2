package service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import model.Epic;
import model.SubTask;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;

import static jdk.internal.util.xml.XMLStreamWriter.DEFAULT_CHARSET;

public class HttpTaskServer {
    private static final int PORT = 8080;
    TaskManager taskManager;
    public HttpServer httpServer;

    public HttpTaskServer(URI url) throws IOException {
        taskManager = Managers.getDefaultHttpTaskManager(url);
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks", new Handler());
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    class Handler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {

            Gson gson = new Gson();
            String path = httpExchange.getRequestURI().getPath();
            String[] splitStrings = path.split("/");


            switch(httpExchange.getRequestMethod()) {

                case "POST":
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = gson.toJson(new String(inputStream.readAllBytes(), DEFAULT_CHARSET));

                    switch (splitStrings[2]) {

                        case "task":
                            System.out.println(taskManager.createTask(gson.fromJson(body, Task.class)).getId());
                        break;
                        case "epic":
                            System.out.println(taskManager.createEpic(gson.fromJson(body, Epic.class)).getId());
                        break;
                        case "subTask":
                            System.out.println(taskManager.createSubTask(gson.fromJson(body, SubTask.class)).getId());
                        break;
                        default:
                            System.out.println("Некорректное значение");
                            break;
                    }
                case "GET":

                    switch (splitStrings.length){
                        case 2:
                            writeResponse(httpExchange, gson.toJson(taskManager.getAll()), 200);
                        case 3:
                        switch (splitStrings[2]) {
                            case "task":
                                writeResponse(httpExchange, gson.toJson(taskManager.getAllTasks()), 200);
                                break;
                            case "epic":
                                writeResponse(httpExchange, gson.toJson(taskManager.getAllEpics()), 200);
                                break;
                            case "subTask":
                                writeResponse(httpExchange, gson.toJson(taskManager.getAllSubTasks()), 200);
                                break;
                            case "history":
                                writeResponse(httpExchange, gson.toJson(taskManager.getHistory()), 200);
                                break;
                            default:
                                writeResponse(httpExchange, "Некорректное значение", 400);
                                break;
                        }
                        case 4:
                            JsonElement jsonElement = JsonParser.parseString(splitStrings[3]);
                            if(!jsonElement.isJsonObject()) {
                                System.out.println("Ответ от сервера не соответствует ожидаемому.");
                                return;
                            }

                            JsonObject jsonObject = jsonElement.getAsJsonObject();
                            int id = jsonObject.get("id").getAsInt();

                        switch (splitStrings[2]) {
                            case "task":
                                writeResponse(httpExchange, gson.toJson(taskManager.getTask(id)), 200);
                                break;
                            case "epic":
                                writeResponse(httpExchange, gson.toJson(taskManager.getEpic(id)), 200);
                                break;
                            case "subTask":
                                writeResponse(httpExchange, gson.toJson(taskManager.getSubTask(id)), 200);
                                break;
                            default:
                                writeResponse(httpExchange, "Некорректное значение", 400);
                                break;
                        }
                        default:
                        writeResponse(httpExchange, "Некорректное значение", 400);
                        break;
                    }

                case "DELETE":
                    switch (splitStrings[2]) {
                        case "task":
                            taskManager.deleteTask(Integer.parseInt(splitStrings[3]));
                            break;
                        case "epic":
                            taskManager.deleteEpic(Integer.parseInt(splitStrings[3]));
                            break;
                        case "subTask":
                            taskManager.deleteSubTask(Integer.parseInt(splitStrings[3]));
                            break;
                        default:
                            writeResponse(httpExchange, "Некорректное значение", 400);
                            break;
                    }
                default:
                    writeResponse(httpExchange, "Некорректное значение", 400);
                    break;

            }



        }
    }

    private void writeResponse(HttpExchange exchange,
                               String responseString,
                               int responseCode) throws IOException {
        if(responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }
}
