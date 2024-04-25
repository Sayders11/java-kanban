package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private List<Integer> history = new ArrayList<>();

    @Override
    public void add(int id) {

        if (history.size() < 10) {
            history.add(id);
        } else {
            history.remove(0);
            history.add(id);
        }
    }

    @Override
    public List<Integer> getHistory() {
        return history;
    }
}
