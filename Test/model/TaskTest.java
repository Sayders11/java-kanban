package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Task")
class TaskTest {

    @Test
    @DisplayName("Должен совпадать со своей копией")
    void shouldEqualsWithCopy() {
        Task task = new Task("name", "desc");
        Task taskExpected = task;
        taskExpected.setId(task.getId());

        assertEqualsTask(taskExpected, task, "ID должны быть равны:");
    }

    private static void assertEqualsTask(Task expected, Task actual, String msg) {
        assertEquals(expected.getId(), actual.getId(), msg + "id");
        assertEquals(expected.getName(), actual.getName(), msg + "name");
        assertEquals(expected.getDescription(), actual.getDescription(), msg + "desc");
        assertEquals(expected.getStatus(), actual.getStatus(), msg + "status");

    }
}