package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static class Node {
        Task item;
        Node next;
        Node prev;

        Node(Node prev, Task element, Node next) {
            this.item = element;
            this.prev = prev;
            this.next = next;
        }
    }

    private final HashMap<Integer, Node> history = new HashMap<>();
    private Node first;
    private Node last;

    private void linkLast(Task task) {
        Node lastNode = last;
        Node newNode = new Node(lastNode, task, null);
        last = newNode;
        if (lastNode == null) {
            first = newNode;
        } else {
            lastNode.next = newNode;
        }
    }

    private void removeNode(int id) {
        try {
            Node current = history.get(id);
            Node prev = current.prev;
            Node next = current.next;
            if (prev == null) {
                first = next;
            } else {
                prev.next = next;
            }
            if (next == null) {
                last = prev;
            } else {
                next.prev = prev;
            }
            history.remove(id);
        } catch (NullPointerException ignored) {
        }
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> listOfTasks = new ArrayList<>();
        Node current = first;
        while (current != null) {
            listOfTasks.add(current.item);
            current = current.next;
        }
        return listOfTasks;
    }

    @Override
    public List<Task> getHistory() {
        List<Task> historyCopy = new ArrayList<>();
        historyCopy.addAll(getTasks());
        return historyCopy;
    }

    @Override
    public void add(Task task) {
        if (history.get(task.getId()) != null) {
            removeNode(task.getId());
        }
        linkLast(task);
        history.put(task.getId(), last);
    }

    @Override
    public void remove(int id) {
        removeNode(id);
    }
}
