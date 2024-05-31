package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Менеджер задач")
class TaskManagerTest {
    InMemoryTaskManager manager;
    Task task;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    void init() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        manager = new InMemoryTaskManager(historyManager);
        Task task = new Task("taskName", "taskDescription");
        this.task = task;
        Epic epic = new Epic("epicName", "epicDescription");
        this.epic = epic;
        Subtask subtask = new Subtask("subtaskName", "subtaskDescription", epic);
        this.subtask = subtask;
    }

    @Test
    @DisplayName("Создать задачу")
    void shouldCreateAndReturnTask() {
        manager.createTask(task);
        assertNotNull(manager.getTask(task.getId()), "Задача не создана");
    }

    @Test
    @DisplayName("Создать эпик")
    void shouldCreateAndReturnEpic() {
        manager.createEpic(epic);
        assertNotNull(manager.getEpic(epic.getId()), "Эпик не создан");
    }

    @Test
    @DisplayName("Создать подзадачу")
    void shouldCreateAndReturnSubtask() {
        manager.createSubtask(subtask);
        assertNotNull(manager.getSubtask(subtask.getId()), "Подзадача не создана");
    }

    @Test
    @DisplayName("Возврат списка задач")
    void shouldReturnTasksList() {
        manager.createTask(task);
        assertNotNull(manager.getAllTasks(), "Список пуст");
    }

    @Test
    @DisplayName("Возврат списка эпиков")
    void shouldReturnEpicsList() {
        manager.createEpic(epic);
        assertNotNull(manager.getAllEpics(), "Список пуст");
    }

    @Test
    @DisplayName("Возврат списка подзадач")
    void shouldReturnSubtasksList() {
        manager.createSubtask(subtask);
        assertNotNull(manager.getAllSubtasks(), "Список пуст");
    }

    @Test
    @DisplayName("Обновление статуса задачи")
    void taskStatusShouldBeEquals() {
        manager.createTask(task);
        task.setStatus(Status.DONE);
        manager.updateTask(task);

        Task expectedTask = manager.getTask(task.getId());
        assertEquals(expectedTask.getStatus(), task.getStatus(), "Статусы не равны");
    }

    @Test
    @DisplayName("Обновление статуса эпика")
    void epicStatusShouldBeEquals() {
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        subtask.setStatus(Status.DONE);
        subtask.setEpicId(epic.getId());
        manager.updateEpic(epic);

        Epic actual = manager.getEpic(epic.getId());

        assertEquals(Status.DONE, actual.getStatus(), "Статус эпика не обновлен");
    }
}