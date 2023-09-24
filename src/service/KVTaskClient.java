package service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    URI url;
    KVServer kvServer;
    private final String apiToken;
    HttpClient client;
    HttpRequest request;
    HttpResponse<String> response;

    public KVTaskClient(URI url) throws IOException, InterruptedException {
        kvServer =  new KVServer();
        kvServer.start();
        client = HttpClient.newHttpClient();
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Код ответа: " + response.statusCode());
        apiToken = response.body();
    }

    public void put(String key, String json){
        url = URI.create("http://localhost:8070/save/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();
    }

    public String load(String key) {
        url = URI.create("http://localhost:8070/load/" + key + "?API_TOKEN=" + apiToken);
        request = HttpRequest.newBuilder().uri(url).GET().build();
        System.out.println("Код ответа: " + response.statusCode());
        return response.body();
    }
}
