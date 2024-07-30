package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Менеджер истории")
class FileManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    FileBackedTaskManager manager;
    File taskFile;
    File emptyFile;

    @BeforeEach
    @Override
    public void createManagers() {
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

        try {
            taskFile = File.createTempFile("tasksFile.csv", null);
            manager = new FileBackedTaskManager(new InMemoryHistoryManager(), taskFile.toPath());
            manager.createTask(task1);
            manager.createTask(task2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Загружаем менеджер из пустого файла")
    void loadFromEmptyFile() throws IOException {
        emptyFile = File.createTempFile("emptyFile.csv", null);
        FileBackedTaskManager fileManager = FileBackedTaskManager.loadFromFile(this.emptyFile);
        assertTrue(fileManager.tasks.isEmpty(), "Список задач не пустой.");
    }

    @Test
    @DisplayName("Загружаем менеджер из файла")
    void loadFromFile() throws IOException {
        FileBackedTaskManager fileManager = FileBackedTaskManager.loadFromFile(taskFile);
        assertEquals(manager.tasks, fileManager.tasks, "Списки задач не одинаковы");
    }
}