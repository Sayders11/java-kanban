package model;

public class Subtask extends Task {
    private int epicId;
    private final Epic epic;

    public Subtask(String name, String description, Epic epic) {
        super(name, description);
        this.epicId = epic.getId();
        this.epic = epic;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {

        return "SubTask{" +
                "name='" + getName() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", description=" + getDescription() + '\'' +
                ", id=" + getId() + '\'' +
                ", epicId: " + getEpicId() + '\'';
    }
}
