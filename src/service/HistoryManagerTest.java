package service;

import Model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    InMemoryHistoryManager historyManager;
    InMemoryTaskManager taskManager;
    Task task;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager();
        task = taskManager.createTask(new Task("1", "2"));
        historyManager.add(task);
    }

    @Test
    void add() {
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void remove() {
        historyManager.remove(task.getId());
        List<Task> history = List.of();
        assertEquals(history, historyManager.getHistory(), "История пустая.");
    }

    @Test
    void getHistory(){
        Task task1 =taskManager.createTask(new Task("3", "4"));
        Task task2 =taskManager.createTask(new Task("5", "6"));
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        tasks.add(task1);
        tasks.add(task2);
        historyManager.add(task2);
        historyManager.add(task1);
        historyManager.add(task2);

        assertEquals(tasks, historyManager.getHistory(), "Неверное дублирование задач");

        historyManager.remove(task.getId());
        tasks.remove(task);

        assertEquals(tasks, historyManager.getHistory(), "Неверное удаление задачи из начала");

        tasks.add(task);
        historyManager.add(task);
        historyManager.remove(task2.getId());
        tasks.remove(task2);

        assertEquals(tasks, historyManager.getHistory(), "Неверное удаление задачи из середины");

        tasks.add(task2);
        historyManager.add(task2);
        historyManager.remove(task2.getId());
        tasks.remove(task2);

        assertEquals(tasks, historyManager.getHistory(), "Неверное удаление задачи с конца");
    }

}