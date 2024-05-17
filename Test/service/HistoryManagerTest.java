package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Менеджер истории")
class HistoryManagerTest {
    InMemoryHistoryManager hManager = new InMemoryHistoryManager();
    InMemoryTaskManager tManager = new InMemoryTaskManager(hManager);
    private Task task1;
    private Task task2;
    private Epic epic1;
    private Epic epic2;
    private Subtask subtask1;
    private Subtask subtask2;

    @BeforeEach
    void init() {
        Task task1 = new Task("task1", "taskDescription");
        this.task1 = task1;
        Task task2 = new Task("task2", "taskDescription");
        this.task2 = task2;
        Epic epic1 = new Epic("epic1", "epicDescription");
        this.epic1 = epic1;
        Epic epic2 = new Epic("epic2", "epicDescription");
        this.epic2 = epic2;
        Subtask subtask1 = new Subtask("subtask1", "subtaskDescription", epic1);
        this.subtask1 = subtask1;
        Subtask subtask2 = new Subtask("subtask2", "subtaskDescription", epic2);
        this.subtask2 = subtask2;

        tManager.createTask(task1);
        tManager.createTask(task2);
        tManager.createEpic(epic1);
        tManager.createEpic(epic2);
        tManager.createSubtask(subtask1);
        tManager.createSubtask(subtask2);
    }

    @Test
    @DisplayName("Добавляем таск в историю")
    void shouldAddTask() {
        hManager.add(task1);
        hManager.add(task2);
        System.out.println(hManager.getHistory());
        assertNotNull(hManager.getHistory(), "Задача не добавлена");
    }

    @Test
    @DisplayName("Очищаем список задач")
    void shouldBeNullAfterRemove() {
        hManager.add(task1);
        hManager.add(task2);
        hManager.remove(task1.getId());
        hManager.remove(task2.getId());

        boolean historyIsEmpty = hManager.getHistory().isEmpty();
        assertTrue(historyIsEmpty, "Задачи не удалены");
    }

    @Test
    @DisplayName("Выводим историю списком")
    void arrayListShouldNotBeEmpty() {
        hManager.add(task1);
        hManager.add(epic1);
        hManager.add(subtask1);
        assertNotNull(hManager.getHistory(), "Список пуст");
    }
}
