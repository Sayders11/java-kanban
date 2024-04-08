package model;

import java.util.ArrayList;


public class Epic extends Task {

 private ArrayList<Integer> subTasksIds = new ArrayList<>();

   public Epic(String name, Status status, String description) {

       super(name, status, description);
   }

   public ArrayList<Integer> getSubTasksIds() {
       return subTasksIds;
   }

   public int getSubTaskId(int index) { return subTasksIds.get(index); }

   public int subTasksIdsSize() { return subTasksIds.size(); }

    @Override
    public String toString() {
        String result = "Epic{" +
                "name='" + getName() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", description=" + getDescription() + '\'' +
                ", id=" + getId() + '\'';

        if (subTasksIds != null) {
            return result + "subTasksIds: " + subTasksIds.toArray();
        } else {
            return result + "subTasksIds: null";
        }
    }

    }


