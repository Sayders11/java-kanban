package model;

public class SubTask extends Task {
    private int epicId;

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public SubTask(String name, Status status, String description) {

        super(name, status, description);
    }

    @Override
    public String toString() {
        String result = "SubTask{" +
                "name='" + getName() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", description=" + getDescription() + '\'' +
                ", id=" + getId() + '\'' +
                ", epicId: " + getEpicId() + '\'';

        return result;
    }
}
