package service;


import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryHistoryManager implements HistoryManager {

    public Node<Task> head;
    public Node<Task> tail;
    ArrayList<Task> history;
    Map<Integer, Node<Task>> elements;


    public InMemoryHistoryManager() {
        this.history = new ArrayList<>();
        this.elements = new HashMap<>();
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


    private void linkLast(Task element) {
        final Node<Task> oldHead = tail;
        final Node<Task> newNode = new Node<>(oldHead, element, null);
        tail = newNode;
        getTasks(newNode);
        if (oldHead == null)
            head =  newNode;
        else
            oldHead.next =  newNode;

        elements.put(element.getId(), newNode);
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
        }
    }

    private static class Node<T>{
        public T task;
        public Node<T> next;
        public Node<T> prev;

        public Node(Node<T> prev, T task, Node<T> next) {
            this.task = task;
            this.next = next;
            this.prev = prev;
        }

    }
    private void getTasks(Node<Task> task){
        history.add(task.task);
    }

}
