package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Менеджер истории")
class FileManagerTest {
    FileBackedTaskManager fileManager;
    File taskFile;
    Task task1;
    Epic epic1;
    Subtask subtask1;

    @BeforeEach
    void init() throws IOException {
        this.taskFile = File.createTempFile("tasksFile.csv", null);
        this.fileManager = new FileBackedTaskManager(new InMemoryHistoryManager(), taskFile.toPath());
        this.task1 = new Task("Task1Name", "Task1Desc");
        this.epic1 = new Epic("Epic1Name", "Epic1Desc");
        this.subtask1 = new Subtask("Subtask1Name", "Subtask1Desc", epic1);
    }

    @Test
    @DisplayName("Загружаем менеджер из пустого файла")
    void LoadFromEmptyFile() throws IOException {
        FileBackedTaskManager fileManager2 = FileBackedTaskManager.loadFromFile(this.taskFile);
        assertEquals(fileManager.tasks, fileManager2.tasks, "Список задач не пустой.");
    }

    @Test
    @DisplayName("Загружаем менеджер из файла")
    void LoadFromFile() throws IOException {
        Task task1 = new Task("Task1Name", "Task1Desc");
        fileManager.createTask(task1);

        FileBackedTaskManager fileManager2 = FileBackedTaskManager.loadFromFile(taskFile);

        assertEquals(fileManager.tasks, fileManager2.tasks, "Списки задач не одинаковы");
    }

    @Test
    @DisplayName("Создать задачу")
    void shouldCreateAndReturnTask() throws IOException {
        fileManager.createTask(task1);
        assertNotNull(fileManager.getTask(task1.getId()), "Задача не создана");
    }

    @Test
    @DisplayName("Создать эпик")
    void shouldCreateAndReturnEpic() {
        fileManager.createEpic(epic1);
        assertNotNull(fileManager.getEpic(epic1.getId()), "Эпик не создан");
    }

    @Test
    @DisplayName("Создать подзадачу")
    void shouldCreateAndReturnSubtask() {
        fileManager.createSubtask(subtask1);
        assertNotNull(fileManager.getSubtask(subtask1.getId()), "Подзадача не создана");
    }

}
