package tests;

import model.Epic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FileBackedTasksManager;
import tests.TaskManagerTest;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    File dir;
    @BeforeEach
    protected void setUp() {
        dir = new File("file.scv");
        tasksManager = new FileBackedTasksManager(dir);
        super.setUp();
    }

    @Test
    void loadFromFile(){

      // assertNull(FileBackedTasksManager.loadFromFile(dir));

        Epic epic1 = tasksManager.createEpic(new Epic("1", "2"));

        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(dir);

        assertEquals(epic1,fileBackedTasksManager.getEpic(epic1.getId()), "Неверное сохранение Epic");

    }
}