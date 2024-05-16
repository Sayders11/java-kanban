package service;

import model.Task;

import java.util.List;

public interface HistoryManager {

    void add(int id);

    List<Task> getHistory();

    void remove(int id);
}
