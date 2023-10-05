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

        HttpTaskManager w = new HttpTaskManager(url);
        w.start();
        w.createEpic(new Epic("make a pie", "by 6 pm"));
        w.deleteEpic(1);

        }
    }

