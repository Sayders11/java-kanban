package model;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description, Epic epic) {
        super(name, description);
        this.epicId = epic.getId();
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
