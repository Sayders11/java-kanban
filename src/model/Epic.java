package model;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTasksIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public ArrayList<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    public int getSubTaskId(int index) {
        return subTasksIds.get(index);
    }

    public int subTasksIdsSize() {
        return subTasksIds.size();
    }

    public void clearSubTasks() {
        subTasksIds.clear();
    }

    public boolean subTaskListIsEmpty() {
        return subTasksIds.isEmpty();
    }

    @Override
    public String toString() {

        return "Epic{" +
                "name='" + getName() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", description=" + getDescription() + '\'' +
                ", id=" + getId() + '\'' +
                "subTasksIds: " + subTasksIds.toArray();
    }

}


