package model;

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

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public Type getType() {
        return type;
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
