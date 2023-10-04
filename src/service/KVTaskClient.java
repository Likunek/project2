package service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    URI url;
    KVServer kvServer;
    private String apiToken;
    HttpClient client;
    HttpRequest request;
    HttpResponse<String> response;
    URI uri;

    public KVTaskClient(URI url) throws IOException, InterruptedException {
        kvServer =  new KVServer();
        client = HttpClient.newHttpClient();
        this.url = url;
    }

    public void start(){
        kvServer.start();
        request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Код ответа: " + response.statusCode());
        apiToken = response.body();
    }
    public void stop(){
        kvServer.stop();
    }

    public void put(String key, String json) {
        url = URI.create("http://localhost:8070/save/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String load(String key) {
        url = URI.create("http://localhost:8070/load/" + key + "?API_TOKEN=" + apiToken);
        request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response_2 = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response_2.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String key){
        url = URI.create("http://localhost:8070/save/" + key + "?API_TOKEN=" + apiToken);
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
