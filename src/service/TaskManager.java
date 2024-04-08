package service;

import model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TaskManager {

    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private int seq = 0;

    private int generateId() { return ++seq; }

    public TaskManager() {this.tasks = new HashMap<>(); }

    public Task create(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    public Epic create(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public SubTask create(SubTask subTask) {
        subTask.setId(generateId());
        subTasks.put(subTask.getId(), subTask);
        return subTask;
    }

    public Task get(int id) { return tasks.get(id); }

    public Epic getEpic(int id) { return epics.get(id); }

    public SubTask getSubTask(int id) { return subTasks.get(id); }

    public Collection<Task> getAllTasks() { return tasks.values(); }

    public Collection<Epic> getAllEpics() { return epics.values(); }

    public Collection<SubTask> getAllSubTasks() { return subTasks.values(); }


    public void update(Task task) {
        tasks.put(task.getId(), task);
    }    // 3

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

        //calculateStatus(savedEpic);
        updateStatus(savedEpic);
    }


    public ArrayList<Task> getAll() { return new ArrayList<>(tasks.values()); }

    public void delete(int id) { tasks.remove(id); }

    public void deleteEpic(int id) {
        Epic removeEpic = epics.remove(id);
        for (int i = 0; i < removeEpic.subTasksIdsSize(); i++ ) {
            subTasks.remove(removeEpic.getSubTaskId(i));
        }
    }

    public void deleteSubTask(int id) {
        SubTask removeSubTask = subTasks.remove(id);

        Epic savedEpic = epics.get(removeSubTask.getEpicId());

        savedEpic.getSubTasksIds().remove(id);
        calculateStatus(savedEpic);
    }

    public void deleteAllTasks() { tasks.clear(); }

    public void deleteAllEpics() { epics.clear(); subTasks.clear(); }

    public void deleteAllSubTasks() { subTasks.clear(); }


private void calculateStatus(Epic epic) {
        Status status = Status.NEW;
        epic.setStatus(status);
}

    private void updateStatus(Epic epic) {
        if (epic.getSubTasksIds() == null || isAllNew(epic)) {
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


}
