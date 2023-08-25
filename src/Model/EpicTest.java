package Model;

import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;

import static Model.Status.*;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    InMemoryTaskManager tasksManager = new InMemoryTaskManager();
    @Test
    void getStatusEpic() {

        final Epic epic = tasksManager.createEpic(new Epic("a", "b"));

        assertNotNull(epic.getStatus(), "Статус неопределен");
        assertEquals(NEW, epic.getStatus(), "Неверный статус Epic");

       final SubTask subTask1 = tasksManager.createSubTask(new SubTask("1", "2", epic.getId()));
       final SubTask subTask2 = tasksManager.createSubTask(new SubTask("3", "4", epic.getId()));

        assertNotNull(epic.getStatus(), "Статус неопределен");
        assertEquals(NEW, epic.getStatus(), "Неверный статус Epic");

        subTask1.setStatus(DONE);
        subTask2.setStatus(DONE);
        tasksManager.updateSubTask(subTask1);
        tasksManager.updateSubTask(subTask2);

        assertNotNull(epic.getStatus(), "Статус неопределен");
        assertEquals(DONE, epic.getStatus(), "Неверный статус Epic");

        subTask1.setStatus(NEW);

        assertNotNull(epic.getStatus(), "Статус неопределен");
        assertEquals(DONE, epic.getStatus(), "Неверный статус Epic");

        subTask1.setStatus(IN_PROGRESS);
        subTask2.setStatus(IN_PROGRESS);
        tasksManager.updateSubTask(subTask1);
        tasksManager.updateSubTask(subTask2);

        assertNotNull(epic.getStatus(), "Статус неопределен");
        assertEquals(IN_PROGRESS, epic.getStatus(), "Неверный статус Epic");

    }

}