package service;

import model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TaskManager {

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private int seq = 0;

    public Task createTask(Task task) {

        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    public Epic createEpic(Epic epic) {

        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public SubTask createSubTask(SubTask subTask) {

        subTask.setId(generateId());
        subTasks.put(subTask.getId(), subTask);
        return subTask;
    }

    public Task get(int id) {

        return tasks.get(id);
    }

    public Epic getEpic(int id) {

        return epics.get(id);
    }

    public SubTask getSubTask(int id) {

        return subTasks.get(id);
    }

    public ArrayList<Task> getAllTasks() {

        return new ArrayList<>(subTasks.values());
    }

    public ArrayList<Epic> getAllEpics() {

        return new ArrayList<>(epics.values());
    }

    public ArrayList<SubTask> getAllSubTasks() {

        return new ArrayList<>(subTasks.values());
    }


    public void update(Task task) {

        if (tasks.get(task.getId()) != null) {

            tasks.put(task.getId(), task);
        }
    }

    public void epicUpdate(Epic epic) {

        Epic saved = epics.get(epic.getId());
        if (saved == null) {
            return;
        }
        saved.setName(epic.getName());
        saved.setDescription(epic.getDescription());
        updateStatus(epic);
    }

    public void updateSubTask(SubTask subTask) {

        Epic savedEpic = epics.get(subTask.getEpicId());
        if (savedEpic == null) {
            return;
        }
        updateStatus(savedEpic);
    }

    public void delete(int id) {

        tasks.remove(id);
    }

    public void deleteEpic(int id) {

        Epic removeEpic = epics.remove(id);
        for (Integer subTaskId : removeEpic.getSubTasksIds()) {
            subTasks.remove(subTaskId);
        }
    }

    public void deleteSubTask(int id) {

        Integer objId = id;

        SubTask removeSubTask = subTasks.remove(id);
        Epic savedEpic = epics.get(removeSubTask.getEpicId());
        savedEpic.getSubTasksIds().remove(objId);
        updateStatus(savedEpic);
    }

    public void deleteAllTasks() {

        tasks.clear();
    }

    public void deleteAllEpics() {

        epics.clear();
        subTasks.clear();
    }

    public void deleteAllSubTasks() {

        for (Epic epic : epics.values()) {
            epic.clearSubTasks();
        }
        subTasks.clear();
    }

    private void updateStatus(Epic epic) {

        if (epic.subTaskListIsEmpty() || isAllNew(epic)) {
            epic.setStatus(Status.NEW);
        } else if (isAllDone(epic)) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    private boolean isAllNew(Epic epic) {

        for (SubTask subTask : subTasks.values()) {
            if (subTask.getEpicId() == epic.getId() && subTask.getStatus() != (Status.NEW)) {
                return false;
            }
        }
        return true;
    }

    private boolean isAllDone(Epic epic) {

        for (SubTask subTask : subTasks.values()) {
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
