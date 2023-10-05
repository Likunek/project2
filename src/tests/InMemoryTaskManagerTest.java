package tests;


import org.junit.jupiter.api.BeforeEach;
import service.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {


    @BeforeEach
    protected void setUp() {
        tasksManager = new InMemoryTaskManager();
        super.setUp();
    }

}