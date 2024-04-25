package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Epic")
class EpicTest {

    @Test
    @DisplayName("Должен совпадать со своей копией:")
    void shouldBeEqualsWithCopy() {
        Epic epic = new Epic("name", "desc");
        Epic expectedEpic = epic;


        assertEqualsEpic(expectedEpic, epic, "ID должны быть равны");
    }

    private static void assertEqualsEpic(Epic expected, Epic actual, String msg) {
        assertEquals(expected.getId(), actual.getId(), msg + "id");
        assertEquals(expected.getName(), actual.getName(), msg + "name");
        assertEquals(expected.getDescription(), actual.getDescription(), msg + "desc");
        assertEquals(expected.getSubtasksIds(), actual.getSubtasksIds(), msg + "subtasksIds");

    }
}