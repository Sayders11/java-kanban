package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Менеджер задач")
abstract class TaskManagerTest<T extends TaskManager> {
    static TaskManager manager;
    static TaskManager voidManager;
    Task task1;
    Task task2;
    Epic epic1;
    Epic epic2;
    Subtask subtask1;
    Subtask subtask2;

    public void createManager() {
        voidManager = Managers.getDefaults();
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
        manager = Managers.getDefaults();
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
    }

    public void getManagerHistory() {
        ArrayList<Task> expected = new ArrayList<>();
        expected.add(manager.getTask(task1.getId()));
        expected.add(manager.getTask(task2.getId()));
        expected.add(manager.getEpic(epic1.getId()));
        expected.add(manager.getEpic(epic2.getId()));
        expected.add(manager.getSubtask(subtask1.getId()));
        expected.add(manager.getSubtask(subtask2.getId()));
        List<Task> tasksHistory = manager.getHistory();
        assertEquals(expected, tasksHistory, "Списки задач различаются.");
    }

    public void getManagerHistoryVoid() {
        List<Task> tasksHistory = voidManager.getHistory();
        List<Task> expected = new ArrayList<>();
        assertEquals(expected, tasksHistory, "Списки отличаются: входной список не пуст.");
    }

    void createTask() {
        Task task = new Task("task1", "task1 Description", LocalDateTime.now()
                .truncatedTo(ChronoUnit.MINUTES), Duration.ofMinutes(15));
        assertNotNull(task, "Задача не была создана, выброшен NPE.");
    }

    void createEpic() {
        Epic epic = new Epic("epic1", "epic1 Description");
        assertNotNull(epic, "Эпик не создан, выброшен NPE.");
    }

    void createSubtask() {
        Subtask subtask = new Subtask("subtask1", "subtask2description", epic1, LocalDateTime.now()
                .truncatedTo(ChronoUnit.MINUTES).plusMinutes(60), Duration.ofMinutes(15));
        assertNotNull(subtask, "Подзадача не создана, выброшен NPE.");
    }

    void getTask() {
        Task actual = manager.getTask(task1.getId());
        Task expected = new Task("task1", "task1 Description", LocalDateTime.now()
                .truncatedTo(ChronoUnit.MINUTES), Duration.ofMinutes(15));
        assertEquals(expected, actual, "Задачи отличаются друг от друга.");
    }

    void getEpic() {
        Epic actual = manager.getEpic(epic1.getId());
        Epic expected = new Epic("epic1", "epic1 Description");
        assertEquals(expected, actual, "Эпики отличаются друг от друга.");
    }

    void getSubtask() {
        Subtask actual = manager.getSubtask(subtask1.getId());
        Subtask excepted = new Subtask("subtask1", "subtask2description", epic1, LocalDateTime.now()
                .truncatedTo(ChronoUnit.MINUTES).plusMinutes(60), Duration.ofMinutes(15));
        assertEquals(excepted, actual, "Сабтаски отличаются друг от друга.");
    }

    void getAllTasks() {
        ArrayList<Task> actual = manager.getAllTasks();
        ArrayList<Task> expected = new ArrayList<>();
        expected.add(task1);
        expected.add(task2);
        assertEquals(expected, actual, "Списки не одинаковы.");
    }

    void getAllEpics() {
        ArrayList<Epic> actual = manager.getAllEpics();
        ArrayList<Task> expected = new ArrayList<>();
        expected.add(epic1);
        expected.add(epic2);
        assertEquals(expected, actual, "Списки не одинаковы.");
    }

    void getAllSubtasks() {
        ArrayList<Subtask> actual = manager.getAllSubtasks();
        ArrayList<Subtask> expected = new ArrayList<>();
        expected.add(subtask1);
        expected.add(subtask2);
        assertEquals(expected, actual, "Списки не одинаковы.");
    }

    void updateTask() {
        task1.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task1);
        assertEquals(task1, manager.getTask(task1.getId()), "Задача не обновилась в списке менеджера.");
    }

    void updateEpic() {
        epic1.setStatus(Status.DONE);
        manager.updateEpic(epic1);
        assertEquals(epic1, manager.getEpic(epic1.getId()));
    }

    void updateSubtask() {
        subtask1.setStatus(Status.IN_PROGRESS);
        manager.updateSubTask(subtask1);
        assertEquals(subtask1, manager.getSubtask(subtask1.getId()));
    }

    void deleteTask() {
        voidManager.createTask(task1);
        voidManager.deleteTask(task1.getId());
        assertTrue(voidManager.getAllTasks().isEmpty());
    }

    void deleteEpic() {
        voidManager.createEpic(epic1);
        voidManager.deleteEpic(epic1.getId());
        assertTrue(voidManager.getAllEpics().isEmpty());
    }


    void deleteSubtask() {
        voidManager.createSubtask(subtask1);
        voidManager.deleteSubtask(subtask1.getId());
        assertTrue(voidManager.getAllSubtasks().isEmpty());
    }

    void deleteAllTasks() {
        voidManager.createTask(task1);
        voidManager.deleteAllTasks();
        assertTrue(voidManager.getAllTasks().isEmpty());
    }

    void deleteAllEpics() {
        voidManager.createEpic(epic1);
        voidManager.deleteAllEpics();
        assertTrue(voidManager.getAllEpics().isEmpty());
    }

    void deleteAllSubtasks() {
        voidManager.createSubtask(subtask1);
        voidManager.deleteAllSubtasks();
        assertTrue(voidManager.getAllSubtasks().isEmpty());
    }

    @BeforeEach
    public abstract void createManagers();
}