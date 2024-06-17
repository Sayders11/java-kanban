package service;

import model.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Менеджер истории")
class FileManagerTest {

    static File taskFile;
    FileBackedTaskManager fileManager = new FileBackedTaskManager(new InMemoryHistoryManager());

    @BeforeAll
    static void beforeAll() throws IOException {
        File taskFile = File.createTempFile("resources\\tasksFile.csv", null);
    }

    @Test
    @DisplayName("Загружаем менеджер из пустого файла")
    void LoadFromEmptyFile() {
        Task task = new Task("Title", "Desc");

        FileBackedTaskManager.loadFromFile(taskFile);
        assertEquals(fileManager.tasks, new HashMap<>(), "Список задач не пустой.");
    }

   /* @Test
    @DisplayName("Загружаем менеджер из файла")
    void LoadFromFile() {
        Task task1 = new Task("task1", "taskDescription");
        Epic epic1 = new Epic("epic1", "epicDescription");
        Subtask subtask1 = new Subtask("subtask1", "subtaskDescription", epic1);

        fileManager.createTask(task1);
        fileManager.createEpic(epic1);
        fileManager.createSubtask(subtask1);
    }*/

}
