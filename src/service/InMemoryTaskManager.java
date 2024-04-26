package service;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int seq = 0;
    private HistoryManager historyManager;

    public InMemoryTaskManager(InMemoryHistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public Task createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subTask) {
        subTask.setId(generateId());
        subtasks.put(subTask.getId(), subTask);
        return subTask;
    }

    @Override
    public Task getTask(int id) {
        historyManager.add(id);
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        historyManager.add(id);
        return epics.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        historyManager.add(id);
        return subtasks.get(id);
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }


    @Override
    public void updateTask(Task task) {
        if (tasks.get(task.getId()) != null) {

            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic saved = epics.get(epic.getId());
        if (saved == null) {

            return;
        }
        saved.setName(epic.getName());
        saved.setDescription(epic.getDescription());
        updateStatus(epic);
    }

    @Override
    public void updateSubTask(Subtask subTask) {
        Epic savedEpic = epics.get(subTask.getEpicId());
        if (savedEpic == null) {

            return;
        }
        updateStatus(savedEpic);
    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        Epic removeEpic = epics.remove(id);
        for (Integer subTaskId : removeEpic.getSubtasksIds()) {
            subtasks.remove(subTaskId);
        }
    }

    @Override
    public void deleteSubtask(int id) {
        Integer objId = id;

        Subtask removeSubtask = subtasks.remove(id);
        Epic savedEpic = epics.get(removeSubtask.getEpicId());
        savedEpic.getSubtasksIds().remove(objId);
        updateStatus(savedEpic);
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.clearSubTasks();
        }
        subtasks.clear();
    }

    @Override
    public ArrayList<Subtask> getSubtasksFromEpic(int epicId) {
        ArrayList<Subtask> list = new ArrayList<>();

        for (Subtask subTask : subtasks.values()) {
            if (subTask.getEpicId() == epicId) {
                list.add(subTask);
            }
        }
        return list;
    }

    @Override
    public List<Task> getHistory() {
        List<Task> historyObjects = new ArrayList<>();
        List<Integer> historyIds = historyManager.getHistory();

        // Вот тут не уверен: нашел инфу, что копировать список можно посредством добавления старого в новый addAll()
        // В случае ниже сохранятся сами ссылки на объекты или это будут новые объекты?
        // p.s. Знаю, что это решение не самое лучшее, однако сложность должна быть O(1)
        // плюс не хотелось бы уходить от плоской модели с айдишками
        for (int i = 0; i < historyIds.size(); i++) {
            int objId = historyIds.get(i);
            if(tasks.get(objId) != null) {
                historyObjects.add(tasks.get(objId));
            } else if (epics.get(objId) != null) {
                historyObjects.add(epics.get(objId));
            } else if (subtasks.get(objId) != null) {
                historyObjects.add(subtasks.get(objId));
            }
        }
        return historyObjects;
    }

    private void updateStatus(Epic epic) {
        if (isAllNew(epic)) {
            epic.setStatus(Status.NEW);
        } else if (isAllDone(epic)) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    private boolean isAllNew(Epic epic) {
        for (Subtask subTask : subtasks.values()) {
            if (subTask.getEpicId() == epic.getId() && subTask.getStatus() != (Status.NEW)) {
                return false;
            }
        }
        return true;
    }

    private boolean isAllDone(Epic epic) {
        for (Subtask subTask : subtasks.values()) {
            if (subTask.getEpicId() == epic.getId() && subTask.getStatus() != (Status.DONE)) {
                return false;
            }
        }
        return true;
    }

    private int generateId() {
        return ++seq;
    }
}
