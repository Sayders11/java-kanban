package service;

import exceptions.ManagerSaveException;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    protected int seq = 0;
    private final HistoryManager historyManager;

    public InMemoryTaskManager(InMemoryHistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }


    @Override
    public Task createTask(Task task) {
        boolean taskCrosses = getPrioritizedTasks().stream().anyMatch(streamingTask ->
                taskTimeCrosses(streamingTask, task));

        if (taskCrosses) {
            throw new ManagerSaveException("Задача пересекается по времени с другими!");
        } else {
            task.setId(generateId());
            tasks.put(task.getId(), task);
            if (task.getStartTime() != null) {
                prioritizedTasks.add(task);
            }
        }
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        boolean taskCrosses = getPrioritizedTasks().stream().anyMatch(streamingSubtask ->
                taskTimeCrosses(streamingSubtask, subtask));

        if (taskCrosses) {
            throw new ManagerSaveException("Задача пересекается по времени с другими!");
        } else {
            subtask.setId(generateId());
            subtasks.put(subtask.getId(), subtask);

            if (epics.get(subtask.getEpicId()) != null) {
                updateEpicTime(epics.get(subtask.getEpicId()));
            }
            if (subtask.getStartTime() != null) {
                prioritizedTasks.add(subtask);
            }
            return subtask;
        }
    }

    @Override
    public Task getTask(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        historyManager.add(subtasks.get(id));
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
        return historyManager.getHistory();
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

    private boolean taskTimeCrosses(Task taskInStream, Task task) {
        if (task.getStartTime().isBefore(taskInStream.getStartTime())
                && task.getEndTime().isBefore(taskInStream.getStartTime())) {
            return false;
        } else return !task.getStartTime().isAfter(taskInStream.getEndTime());
    }

    private void updateEpicTime(Epic epic) {
        ArrayList<Integer> subtasksIds = epic.getSubtasksIds();

        try {
            Optional<LocalDateTime> minStartTime = subtasksIds
                    .stream()
                    .map(subtasks::get)
                    .min(Comparator.comparing(Subtask::getStartTime))
                    .map(Subtask::getStartTime);

            Optional<LocalDateTime> maxEndTime = subtasksIds
                    .stream()
                    .map(subtasks::get)
                    .max(Comparator.comparing(Subtask::getEndTime))
                    .map(Subtask::getEndTime);

            long sum = 0L;
            for (Integer subtasksId : subtasksIds) {
                Subtask subtask = subtasks.get(subtasksId);
                long duration = subtask.getDuration().toMinutes();
                sum += duration;
            }
            Optional<Duration> sumDuration = Optional.ofNullable(Duration.ofMinutes(sum));

            minStartTime.ifPresent(epic::setStartTime);
            maxEndTime.ifPresent(epic::setEndTime);
            sumDuration.ifPresent(epic::setDuration);
        } catch (NullPointerException e) {
            throw new ManagerSaveException();
        }
    }
}
