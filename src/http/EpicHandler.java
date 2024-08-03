package http;

import com.sun.net.httpserver.HttpExchange;
import exceptions.ManagerSaveException;
import model.Epic;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class EpicHandler extends BaseHttpHandler {

    public EpicHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_EPICS -> handleGetAllEpics(exchange);
            case GET_EPICS_ID -> handleGetEpic(exchange);
            case POST_EPICS -> handleCreateEpic(exchange);
            case DELETE_EPICS -> handleDeleteAllEpics(exchange);
            case DELETE_EPICS_ID -> handleDeleteEpic(exchange);
            case GET_EPICS_ID_SUBTASKS -> handleGetAllSubtaskFromEpic(exchange);
            default -> sendNotFound(exchange);
        }
    }

    private void handleGetAllEpics(HttpExchange exchange) {
        try {
            sendText(exchange, gson.toJson(manager.getAllEpics()));
        } catch (IOException e) {
            sendServerError(exchange);
        }
    }

    private void handleGetEpic(HttpExchange exchange) throws IOException {
        Optional<Integer> optId = getId(exchange);
        if (optId.isEmpty()) {
            sendServerError(exchange);
            return;
        }

        Epic epic;
        try {
            epic = manager.getEpic(optId.get());
            if (epic == null) throw new NullPointerException();

            String text = gson.toJson(epic);
            sendText(exchange, text);
        } catch (NullPointerException exception) {
            sendNotFound(exchange);
        } catch (IOException e) {
            sendServerError(exchange);
        }
    }

    private void handleCreateEpic(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        Epic epic = gson.fromJson(new String(inputStream.readAllBytes(), DEFAULT_CHARSET), Epic.class);

        try {
            Epic newEpic = manager.createEpic(epic);
            if (epic == null) throw new ManagerSaveException();
            sendPostSuccess(exchange);
        } catch (ManagerSaveException e) {
            sendHasInteractions(exchange);
        }
    }

    private void handleDeleteAllEpics(HttpExchange exchange) {
        try {
            manager.deleteAllEpics();
            sendText(exchange, "Все эпики успешно удалены.");
        } catch (IOException e) {
            sendServerError(exchange);
        }
    }

    private void handleDeleteEpic(HttpExchange exchange) throws IOException {
        Optional<Integer> optId = getId(exchange);
        if (optId.isEmpty()) {
            sendServerError(exchange);
            return;
        }

        try {
            Epic epic = manager.getEpic(optId.get());
            if (epic == null) throw new NullPointerException();

            manager.deleteEpic(optId.get());
            sendText(exchange, "Эпик успешно удален.");
        } catch (NullPointerException e) {
            sendNotFound(exchange);
        } catch (IOException e) {
            sendServerError(exchange);
        }
    }

    private void handleGetAllSubtaskFromEpic(HttpExchange exchange) throws IOException {
        Optional<Integer> optId = getId(exchange);
        if (optId.isEmpty()) {
            sendServerError(exchange);
            return;
        }

        Epic epic;
        try {
            epic = manager.getEpic(optId.get());
            if (epic == null) throw new NullPointerException();

            String text = gson.toJson(manager.getSubtasksFromEpic(optId.get()));
            sendText(exchange, text);
        } catch (NullPointerException exception) {
            sendNotFound(exchange);
        } catch (IOException e) {
            sendServerError(exchange);
        }
    }
}
