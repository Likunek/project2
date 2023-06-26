package service;

import Model.SubTask;
import Model.Task;

import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryHistoryManager implements HistoryManager {

    public static Node<Task> head;
    public static  Node<Task> tail;
    ArrayList<Task> history;
    Map<Integer, Node<Task>> elements;
    private int size = 0;

    public InMemoryHistoryManager() {
        this.history = new ArrayList<>();
        this.elements = new HashMap<>();
    }

    public void linkLast(Task element) {
        final Node<Task> oldHead = tail;
        final Node<Task> newNode = new Node<>(oldHead, element, null);
        tail = newNode;
        getTasks(newNode);
        if (oldHead == null)
            head =  newNode;
        else
            oldHead.next =  newNode;
        size++;
        elements.put(element.getId(), newNode);
    }

    @Override
    public void add(Task task) {
        if (!history.isEmpty()) {
            if (history.get(history.size() - 1) != task) {
                if (elements.containsKey(task.getId())) {
                    remove(task.getId());
                }
                linkLast(task);
            }
        } else {
            linkLast(task);
        }

    }

    @Override
    public void remove(int id){

        removeNode(elements.get(id));
        history.removeIf(task -> task.getId() == id);
        elements.remove(id);
    }

    @Override
    public List<Task> getHistory(){
        return history;
    }

    private void removeNode(Node<Task> node){
        if (node != null) {
            Node<Task> prev = node.prev;
            Node<Task> next = node.next;
            if (node.prev != null && node.next != null) {
                prev.next = next;
                next.prev = prev;
            } else if (node.next != null) {
                next.prev = null;
                head = next;
            } else if (node.prev != null){
                prev.next = null;
                tail = prev;
            }
            node = null;
            size--;
        }
    }
    private void getTasks(Node<Task> task){
        history.add(task.task);
    }

}
