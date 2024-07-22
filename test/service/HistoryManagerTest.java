package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Менеджер истории")
class HistoryManagerTest {
    InMemoryHistoryManager hManager = new InMemoryHistoryManager();
    InMemoryTaskManager tManager = new InMemoryTaskManager(hManager);
    Task task1;
    Task task2;
    Epic epic1;
    Epic epic2;
    Subtask subtask1;
    Subtask subtask2;

    @BeforeEach
    void init() {
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
