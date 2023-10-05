package service;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import model.Epic;
import model.SubTask;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    private static final int PORT = 8080;
    HttpTaskManager taskManager;
    public HttpServer httpServer;

    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks", this::handle);
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void start(){
        taskManager =new HttpTaskManager(URI.create("http://localhost:8070/register"));
        httpServer.start();
        taskManager.start();
    }

    public void stop(){
        httpServer.stop(1);
        taskManager.stop();
    }

        public void handle(HttpExchange httpExchange) throws IOException {


              Gson gson = new Gson();
              String path = httpExchange.getRequestURI().getPath();
              String[] splitStrings = path.split("/");

              switch (httpExchange.getRequestMethod()) {

                  case "POST":
                      InputStream inputStream = httpExchange.getRequestBody();
                      String body = new String(inputStream.readAllBytes(), UTF_8);

                      switch (splitStrings[2]) {

                          case "task":
                              writeResponse(httpExchange, gson.toJson(taskManager.createTask(gson.fromJson(body, Task.class))),200);
                              break;
                          case "epic":
                              writeResponse(httpExchange, gson.toJson(taskManager.createEpic(gson.fromJson(body, Epic.class))),200);
                              break;
                          case "subTask":
                              writeResponse(httpExchange, gson.toJson(taskManager.createSubTask(gson.fromJson(body, SubTask.class))),200);
                              break;
                          default:
                              System.out.println("Некорректное значение");
                              break;
                      }
                      break;

                  case "GET":

                      switch (splitStrings.length) {
                          case 2:
                              writeResponse(httpExchange, gson.toJson(taskManager.getAll()), 200);
                              break;
                          case 3:
                              String rawQuery = httpExchange.getRequestURI().getRawQuery();
                              if (rawQuery == null) {
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
                              }else {
                                  Pattern pattern = Pattern.compile("\\d");
                                  Matcher matcher = pattern.matcher(rawQuery);
                                  String id = null;
                                  while (matcher.find()) {
                                      id = rawQuery.substring(matcher.start(), matcher.end());
                                  }
                                  assert id != null;
                                  switch (splitStrings[2]) {
                                      case "task":
                                          writeResponse(httpExchange, gson.toJson(taskManager.getTask(Integer.parseInt(id))), 200);
                                          break;
                                      case "epic":
                                          writeResponse(httpExchange, gson.toJson(taskManager.getEpic(Integer.parseInt(id))), 200);
                                          break;
                                      case "subTask":
                                          writeResponse(httpExchange, gson.toJson(taskManager.getSubTask(Integer.parseInt(id))), 200);
                                          break;
                                      default:
                                          writeResponse(httpExchange, "Некорректное значение", 400);
                                          break;
                                  }
                              }
                              break;
                          default:
                              writeResponse(httpExchange, "Некорректное значение", 400);
                              break;
                      }
                      break;

                  case "DELETE":
                      String rawQuery = httpExchange.getRequestURI().getRawQuery();
                      Pattern pattern = Pattern.compile("\\d");
                      Matcher matcher = pattern.matcher(rawQuery);
                      String id = null;
                      while (matcher.find()) {
                          id = rawQuery.substring(matcher.start(), matcher.end());
                      }
                      assert id != null;
                      switch (splitStrings[2]) {
                          case "task":
                              taskManager.deleteTask(Integer.parseInt(id));
                              writeResponse(httpExchange, "Задача успешно удалена", 400);
                              break;
                          case "epic":
                              taskManager.deleteEpic(Integer.parseInt(id));
                              writeResponse(httpExchange, "Задача успешно удалена", 400);
                              break;
                          case "subTask":
                              taskManager.deleteSubTask(Integer.parseInt(id));
                              writeResponse(httpExchange, "Задача успешно удалена", 400);
                              break;
                          default:
                              writeResponse(httpExchange, "Некорректное значение", 400);
                              break;
                      }
                      break;
                  default:
                      writeResponse(httpExchange, "Некорректное значение", 400);

              }

        }

    private void writeResponse(HttpExchange exchange,
                               String responseString,
                               int responseCode) throws IOException {
        if(responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            byte[] bytes = responseString.getBytes(UTF_8);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }
}
