package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import http.HttpTaskServer;
import http.adapter.DurationAdapter;
import http.adapter.LocalDateAdapter;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class HttpTaskTest {

    Task task1;
    Task task2;
    TaskManager manager;
    HttpTaskServer httpTaskServer;
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    class TaskListTypeToken extends TypeToken<List<Task>> {
    }

    @BeforeEach
    public void setUp() throws IOException {
        task1 = new Task("task1", "task1 Description", LocalDateTime.now()
                .truncatedTo(ChronoUnit.MINUTES), Duration.ofMinutes(15));
        task2 = new Task("task2", "task2 Description", LocalDateTime.now()
                .truncatedTo(ChronoUnit.MINUTES).plusMinutes(30), Duration.ofMinutes(15));

        manager = Managers.getDefaults();
        manager.createTask(task1);
        manager.createTask(task2);
        httpTaskServer = new HttpTaskServer(manager);
        httpTaskServer.start();
    }

    @AfterEach
    public void shutDown() {
        httpTaskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task3 = new Task("task3", "task3 Description", LocalDateTime.now()
                .truncatedTo(ChronoUnit.MINUTES).plusMinutes(90), Duration.ofMinutes(15));
        String taskJson = gson.toJson(task3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = manager.getAllTasks();

        assertEquals(manager.getTask(task1.getId()), task1, "НЕ РАВНЫ!!");
        assertFalse(tasksFromManager.isEmpty(), "Задачи не возвращаются");
        assertEquals(3, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("task3", tasksFromManager.get(2).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testAddTasksErrorHasInteractions() throws IOException, InterruptedException {
        Task task = new Task("Task_Test", "tasks.Task Test description",
                LocalDateTime.now()
                        .truncatedTo(ChronoUnit.MINUTES), Duration.ofMinutes(15));

        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());

        List<Task> tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(2, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Not Acceptable", response.body(), "Некорректное тело ответа");
    }

    @Test
    public void testGetTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = manager.getAllTasks();
        List<Task> tasksFromResponse = gson.fromJson(response.body(), new TaskListTypeToken().getType());

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertNotNull(tasksFromResponse, "Задачи не получены из тела ответа");
        assertEquals(tasksFromManager.size(), tasksFromResponse.size(), "Некорректное количество задач");
        assertEquals(tasksFromManager, tasksFromResponse, "Некорректный список задач в ответе");
    }

    @Test
    public void testGetTaskId() throws IOException, InterruptedException {
        Task taskFromManager = manager.getTask(1);

        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/tasks/" + 1);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Task taskFromJson = gson.fromJson(response.body(), Task.class);

        assertNotNull(taskFromJson, "Задача не возвращаются");
        assertEquals(taskFromManager, taskFromJson, "Некорректная задача из тела ответа");
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Task taskFromManager = manager.getTask(1);

        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/tasks/" + 1);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Task> managerTaskList = manager.getAllTasks();

        assertNotNull(managerTaskList, "Задачи не возвращаются");
        assertEquals(1, managerTaskList.size(), "Некорректное количество задач");
        Assertions.assertFalse(managerTaskList.contains(taskFromManager), "Задача не удалена");
    }

    @Test
    public void testDeleteTaskServerError() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/tasks/" + "asd");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(500, response.statusCode());

        List<Task> taskFromManager = manager.getAllTasks();

        assertNotNull(taskFromManager, "Задачи не возвращаются");
        assertEquals(2, taskFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void testDeleteTaskErrorDeleteTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/tasks/" + 5);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());

        List<Task> taskFromManager = manager.getAllTasks();

        assertNotNull(taskFromManager, "Задачи не возвращаются");
        assertEquals(2, taskFromManager.size(), "Некорректное количество задач");
    }
}
