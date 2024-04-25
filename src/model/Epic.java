package model;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subtasksIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
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

        return "Epic{" +
                "name='" + getName() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", description=" + getDescription() + '\'' +
                ", id=" + getId() + '\'' +
                "subtasksIds: " + subtasksIds.toArray();
    }

}


