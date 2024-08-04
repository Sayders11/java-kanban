package http;

import com.sun.net.httpserver.HttpExchange;
import exceptions.ManagerSaveException;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class TaskHandler extends BaseHttpHandler {

    public TaskHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_TASKS -> handleGetAllTasks(exchange);
            case GET_TASKS_ID -> handleGetTask(exchange);
            case POST_TASKS -> handleCreateTask(exchange);
            case DELETE_TASKS -> handleDeleteAllTasks(exchange);
            case DELETE_TASKS_ID -> handleDeleteTask(exchange);
            default -> sendNotFound(exchange);
        }
    }

    private void handleGetAllTasks(HttpExchange exchange) {
        try {
            sendText(exchange, gson.toJson(manager.getAllTasks()));
        } catch (IOException e) {
            sendServerError(exchange);
        }
    }

    private void handleGetTask(HttpExchange exchange) throws IOException {
        Optional<Integer> optId = getId(exchange);
        if (optId.isEmpty()) {
            sendServerError(exchange);
            return;
        }

        Task task;
        try {
            task = manager.getTask(optId.get());
            String text = gson.toJson(task);
            sendText(exchange, text);
        } catch (NullPointerException exception) {
            sendNotFound(exchange);
        } catch (IOException e) {
            sendServerError(exchange);
        }
    }

    private void handleCreateTask(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        Task task = gson.fromJson(new String(inputStream.readAllBytes(), DEFAULT_CHARSET), Task.class);

        try {
            manager.createTask(task);
            sendPostSuccess(exchange);
        } catch (NullPointerException e) {
            sendNotFound(exchange);
        } catch (ManagerSaveException e) {
            sendHasInteractions(exchange);
        }
    }

    private void handleDeleteAllTasks(HttpExchange exchange) {
        try {
            manager.deleteAllTasks();
            sendText(exchange, "Все задачи успешно удалены.");
        } catch (IOException e) {
            sendServerError(exchange);
        }
    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        Optional<Integer> optId = getId(exchange);
        if (optId.isEmpty()) {
            sendServerError(exchange);
            return;
        }
        // Если так сделать ОК, то после ревью в остальных хендлерах тоже такой код поправлю.
        try {
            manager.deleteTask(optId.get());
            sendText(exchange, "Задача успешно удалена.");
        } catch (NullPointerException e) {
            sendNotFound(exchange);
        } catch (IOException e) {
            sendServerError(exchange);
        }
    }
}
