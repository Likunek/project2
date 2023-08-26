package tests;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.TaskManagerTest;


import static org.junit.jupiter.api.Assertions.assertNull;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {


    @BeforeEach
    protected void setUp() {
        tasksManager = new InMemoryTaskManager();
        super.setUp();
    }

    @Test
    void createTask() {

        assertNull(tasksManager.tasks.get(0), "Задачи возвращаются неверно.");
    }


    @Test
    void createEpic() {

        assertNull(tasksManager.epics.get(0), "Задачи возвращаются неверно.");
    }


    @Test
    void createSubTask() {

    assertNull(tasksManager.tasks.get(0), "Задачи возвращаются неверно.");
    }

}