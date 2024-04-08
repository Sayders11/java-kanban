package model;

import java.util.Objects;

public class Task {

    private String name;
    protected Status status;
    private String description;
    private int id;

    public Task(String name, Status status, String description, int id) {
        this.name = name;
        this.status = status;
        this.description = description;
        this.id = id;
    }

    public Task(String name, Status status, String description) {
        this.name = name;
        this.status = status;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() { return this.id; }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        String result = "Task{" +
                "name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", description=" + description + '\'' +
                ", id=" + id + '\'';
        return result;
    }
}


