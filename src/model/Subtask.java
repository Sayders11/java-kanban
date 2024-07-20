package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId;
    private final Type type = Type.SUBTASK;

    public Subtask(String name, String description, Epic epic) {
        super(name, description);
        this.epicId = epic.getId();
    }

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Epic epic, LocalDateTime startTime, Duration duration) {
        super(name, description, startTime, duration);
        this.epicId = epic.getId();
    }

    public Subtask(String name, String description, LocalDateTime startTime, Duration duration, int epicId) {
        super(name, description, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return getId() + "," + getType() + "," + getName() + "," + getStatus() + "," + getDescription() + "," +
                getStartTime() + "," + getDuration() + "," + getEndTime() + "," + getEpicId();
    }
}
