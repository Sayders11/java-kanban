package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import http.HttpTaskServer;
import http.adapter.DurationAdapter;
import http.adapter.LocalDateAdapter;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FileBackedTaskManager;
import service.InMemoryHistoryManager;
import service.TaskManager;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class HttpHistoryAndPriorityTest {

    Task task1;
    Task task2;
    Epic epic1;
    Epic epic2;
    Subtask subtask1;
    Subtask subtask2;
    File taskFile;
    TaskManager manager;
    HttpTaskServer httpTaskServer;
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    class PrioritizedSetTypeToken extends TypeToken<Set<Task>> {
    }

    class HistoryListTypeToken extends TypeToken<List<Task>> {
    }

    @BeforeEach
    public void createManagersAndStartServer() throws IOException {
        task1 = new Task("task1", "task1 Description", LocalDateTime.now()
                .truncatedTo(ChronoUnit.MINUTES), Duration.ofMinutes(15));
        task2 = new Task("task2", "task2 Description", LocalDateTime.now()
                .truncatedTo(ChronoUnit.MINUTES).plusMinutes(30), Duration.ofMinutes(15));
        epic1 = new Epic("epic1", "epic1 Description");
        epic2 = new Epic("epic2", "epic2 Description");
        subtask1 = new Subtask("subtask1", "subtask2description", epic1, LocalDateTime.now()
                .truncatedTo(ChronoUnit.MINUTES).plusMinutes(60), Duration.ofMinutes(15));
        subtask2 = new Subtask("subtask2", "subtask2 Description", epic1, LocalDateTime.now()
                .truncatedTo(ChronoUnit.MINUTES).plusMinutes(90), Duration.ofMinutes(15));

        try {
            taskFile = File.createTempFile("tasksFile.csv", null);
            manager = new FileBackedTaskManager(new InMemoryHistoryManager(), taskFile.toPath());
            manager.createTask(task1);
            manager.createTask(task2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        httpTaskServer = new HttpTaskServer(manager);
        httpTaskServer.start();
    }

    @AfterEach
    public void shutDown() {
        httpTaskServer.stop();
    }

    @Test
    public void testGetHistory() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Task> historyFromManager = manager.getHistory();
        List<Task> historyFromResponse = gson.fromJson(response.body(), new HistoryListTypeToken().getType());

        assertNotNull(historyFromManager, "История не возвращается");
        assertNotNull(historyFromResponse, "История не получена из тела ответа");
        assertEquals(historyFromManager.size(), historyFromResponse.size(), "Некорректное количество задач");
    }

    @Test
    public void testGetPrioritized() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Set<Task> prioritizedFromManager = manager.getPrioritizedTasks();
        Set<Task> prioritizedFromResponse = gson.fromJson(response.body(), new PrioritizedSetTypeToken().getType());

        assertNotNull(prioritizedFromManager, "Задачи не возвращаются");
        assertNotNull(prioritizedFromResponse, "Задачи не получены из тела ответа");
        assertEquals(prioritizedFromManager.size(), prioritizedFromResponse.size(),
                "Некорректное количество задач");
        assertEquals(prioritizedFromManager, prioritizedFromResponse,
                "Задачи из тела запроса не совпадают с задачами из менеджера");
    }

}
