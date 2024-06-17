package service;

import exceptions.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final String CsvPath = "C:\\Users\\Михаил\\IdeaProjects\\java-kanban\\resources\\tasks.csv";

    public FileBackedTaskManager(InMemoryHistoryManager historyManager, String savedFile) {
        super(historyManager);
    }

    public FileBackedTaskManager(InMemoryHistoryManager historyManager) {
        super(historyManager);
    }

    public static FileBackedTaskManager loadFromFile(File file) {

        FileBackedTaskManager fileManager = new FileBackedTaskManager(new InMemoryHistoryManager());

        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            reader.readLine();

            while (reader.ready()) {
                String line = reader.readLine();
                Task task = fileManager.fromString(line);

                switch (task.getType()) {
                    case Type.TASK:
                        fileManager.tasks.put(task.getId(), task);
                        break;
                    case Type.EPIC:
                        fileManager.epics.put(task.getId(), (Epic) task);
                        break;
                    case Type.SUBTASK:
                        fileManager.subtasks.put(task.getId(), (Subtask) task);
                        break;
                }

                if (fileManager.seq < task.getId()) {
                    fileManager.seq = task.getId();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileManager;
    }

    @Override
    public Task createTask(Task task) {
        Task back = super.createTask(task);
        save();
        return back;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic back = super.createEpic(epic);
        save();
        return back;
    }

    @Override
    public Subtask createSubtask(Subtask subTask) {
        Subtask back = super.createSubtask(subTask);
        save();
        return back;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(Subtask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CsvPath))) {
            writer.write("id,type,name,status,description,epicId");
            writer.newLine();

            for (Task task : tasks.values()) {
                writer.write(toString(task));
                writer.newLine();
            }
            for (Epic epic : epics.values()) {
                writer.write(toString(epic));
                writer.newLine();
            }
            for (Subtask subtask : subtasks.values()) {
                writer.write(toString(subtask));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    private String toString(Task task) {
        return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + ","
                + task.getDescription();
    }

    private String toString(Epic epic) {
        return epic.getId() + "," + epic.getType() + "," + epic.getName() + "," + epic.getStatus() + ","
                + epic.getDescription();
    }

    private String toString(Subtask subtask) {
        return subtask.getId() + "," + subtask.getType() + "," + subtask.getName() + "," + subtask.getStatus() + ","
                + subtask.getDescription() + "," + subtask.getEpicId();
    }

    private Task fromString(String value) {
        String[] parts = value.split(",");
        String taskId = parts[0];
        String taskType = parts[1];
        String taskName = parts[2];
        String taskStatus = parts[3];
        String taskDescription = parts[4];
        Task returnedTask = null;

        switch (taskType) {
            case "TASK":
                Task backTask = new Task(taskName, taskDescription);
                backTask.setId(Integer.parseInt(taskId));
                backTask.setStatus(Status.valueOf(taskStatus));
                returnedTask = backTask;
                break;
            case "EPIC":
                Epic backEpic = new Epic(taskName, taskDescription);
                backEpic.setId(Integer.parseInt(taskId));
                backEpic.setStatus(Status.valueOf(taskStatus));
                returnedTask = backEpic;
            case "SUBTASK":
                String epicId = parts[5];
                Subtask backSubtask = new Subtask(taskName, taskDescription, Integer.parseInt(epicId));
                backSubtask.setId(Integer.parseInt(taskId));
                backSubtask.setStatus(Status.valueOf(taskStatus));
                returnedTask = backSubtask;
        }
        return returnedTask;
    }
}