package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    private final Type type = Type.EPIC;
    private final ArrayList<Integer> subtasksIds = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description, LocalDateTime.now(), Duration.ofMinutes(0));
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }


    public int getSubTaskId(int index) {
        return subtasksIds.get(index);
    }

    public int subTasksIdsSize() {
        return subtasksIds.size();
    }

    public void clearSubTasks() {
        subtasksIds.clear();
    }

    public boolean subtaskListIsEmpty() {
        return subtasksIds.isEmpty();
    }

    @Override
    public String toString() {
        return getId() + "," + getType() + "," + getName() + "," + getStatus() + "," + getDescription() + "," +
                getStartTime() + "," + getDuration() + "," + getEndTime();
    }

}


