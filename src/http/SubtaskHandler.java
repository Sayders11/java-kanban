package http;

import com.sun.net.httpserver.HttpExchange;
import exceptions.ManagerSaveException;
import model.Subtask;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class SubtaskHandler extends BaseHttpHandler {

    public SubtaskHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_SUBTASKS -> handleGetAllSubtasks(exchange);
            case GET_SUBTASKS_ID -> handleGetSubtask(exchange);
            case POST_SUBTASKS -> handleCreateSubtask(exchange);
            case DELETE_SUBTASKS -> handleDeleteAllSubtasks(exchange);
            case DELETE_SUBTASKS_ID -> handleDeleteSubtask(exchange);
            default -> sendNotFound(exchange);
        }
    }

    private void handleGetAllSubtasks(HttpExchange exchange) {
        try {
            sendText(exchange, gson.toJson(manager.getAllSubtasks()));
        } catch (IOException e) {
            sendServerError(exchange);
        }
    }

    private void handleGetSubtask(HttpExchange exchange) throws IOException {
        Optional<Integer> optId = getId(exchange);
        if (optId.isEmpty()) {
            sendServerError(exchange);
            return;
        }
        Subtask subtask;
        try {
            subtask = manager.getSubtask(optId.get());
            if (subtask == null) {
                throw new NullPointerException();
            }

            String text = gson.toJson(subtask);
            sendText(exchange, text);
        } catch (NullPointerException exception) {
            sendNotFound(exchange);
        } catch (IOException e) {
            sendServerError(exchange);
        }
    }

    private void handleCreateSubtask(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        Subtask subtask = gson.fromJson(new String(inputStream.readAllBytes(), DEFAULT_CHARSET), Subtask.class);

        try {
            Subtask newSubtask = manager.createSubtask(subtask);
            if (newSubtask == null) {
                throw new ManagerSaveException();
            }
            sendPostSuccess(exchange);
        } catch (ManagerSaveException e) {
            sendHasInteractions(exchange);
        }
    }

    private void handleDeleteAllSubtasks(HttpExchange exchange) {
        try {
            manager.deleteAllSubtasks();
            sendText(exchange, "Все подзадачи успешно удалены.");
        } catch (IOException e) {
            sendServerError(exchange);
        }
    }

    private void handleDeleteSubtask(HttpExchange exchange) throws IOException {
        Optional<Integer> optId = getId(exchange);
        if (optId.isEmpty()) {
            sendServerError(exchange);
            return;
        }

        try {
            Subtask subtask = manager.getSubtask(optId.get());
            if (subtask == null) {
                throw new NullPointerException();
            }

            manager.deleteSubtask(optId.get());
            sendText(exchange, "Подзадача успешно удалена.");
        } catch (NullPointerException e) {
            sendNotFound(exchange);
        } catch (IOException e) {
            sendServerError(exchange);
        }
    }
}
