package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Subtask")
class SubtaskTest {

    @Test
    @DisplayName("Должен совпадать со своей копией")
    void shouldEqualsWithCopy() {
        Epic epic = new Epic("name", "desc");
        Subtask subtask = new Subtask("name", "desc", epic);
        Subtask expectedSubtask = subtask;
        expectedSubtask.setId(subtask.getId());

        assertEqualsSubtask(expectedSubtask, subtask, "ID должны быть равны:");
    }

    private static void assertEqualsSubtask(Subtask expected, Subtask actual, String msg) {
        assertEquals(expected.getId(), actual.getId(), msg + "id");
        assertEquals(expected.getName(), actual.getName(), msg + "name");
        assertEquals(expected.getDescription(), actual.getDescription(), msg + "desc");
        assertEquals(expected.getEpicId(), actual.getEpicId(), msg + "epicId");
    }
}