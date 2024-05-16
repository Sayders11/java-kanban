package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private TaskManager Taskmanager;

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

    HashMap<Integer, Node> history = new HashMap<>();
    Node first;
    Node last;

    private void linkLast(int id) {
        Node l = last;
        Task e = Taskmanager.getTask(id);
        Node newNode = new Node(l, e, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
    }

    private void removeNode(int id) {
        Node current = history.get(id);
        Node prevNode = current.prev;
        Node nextNode = current.next;

        if (prevNode != null) {
            prevNode.next = nextNode;
        } else {
            nextNode.prev = null;
            first = nextNode;
        }

        if (nextNode != null) {
            nextNode.prev = prevNode;
        } else {
            prevNode.next = null;
            last = prevNode;
        }
        history.remove(id);
    }

    public ArrayList<Task> getTasks() {
//TO DO
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
    public void add(int id) {
        if (history.get(id) != null) {
            removeNode(id);
            linkLast(id);
        } else {
            linkLast(id);
            history.put(id, last);
        }
    }

    @Override
    public void remove(int id) {
        removeNode(id);
    }
}
