package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    Task createTask(Task task);

    Epic createEpic(Epic epic);

    Subtask createSubtask(Subtask subTask);

    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);

    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<Subtask> getAllSubtasks();

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(Subtask subTask);

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubtask(int id);

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    ArrayList<Subtask> getSubtasksFromEpic(int epicId);

    List<Task> getHistory();
}
