package tests;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.TaskManager;

import java.io.FileNotFoundException;
import java.util.List;

import static model.Status.IN_PROGRESS;
import static model.Status.NEW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

abstract class TaskManagerTest<T extends TaskManager>{

    T tasksManager;
    Task task;
    Epic epic;
    SubTask subTask;
    int taskId;
    int epicId;
    int subTaskId;

    protected  void setUp() {
        task = tasksManager.createTask(new Task("1", "2"));
        epic = tasksManager.createEpic(new Epic("1", "2"));
        subTask = tasksManager.createSubTask(new SubTask("1", "2", epic.getId()));
        taskId = task.getId();
        epicId = epic.getId();
        subTaskId = subTask.getId();
    }

    @Test
    void createTask() {

        final Task savedTask = tasksManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");


        final List<Task> tasksList = tasksManager.getAllTasks();

        assertNotNull(tasksList, "Задачи на возвращаются.");
        assertEquals(1, tasksList.size(), "Неверное количество задач.");
        assertEquals(task, tasksList.get(0), "Задачи не совпадают.");

        tasksManager.deleteTask(taskId);

        assertEquals(0, tasksManager.getAllTasks().size(), "Неверное количество задач.");

        task.settingTheTime(20, "15.08.2023 - 14:00");

        assertEquals("15.08.2023 - 14:20", task.getEndTime(), "Неверное стартовое время");
        //со статусом не получится проверить неверный индификатор, т.к. его нет в enum,
        // то программа с ним не запустится выдаст ошибку(на вход уже тип Status подается)
    }

    @Test
    void createEpic() {

        final Epic savedEpic = tasksManager.getEpic(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");
        assertEquals(NEW, epic.getStatus(), "Неверный статус Epic");


        final List<Epic> tasksList = tasksManager.getAllEpics();

        assertNotNull(tasksList, "Задачи на возвращаются.");
        assertEquals(1, tasksList.size(), "Неверное количество задач.");
        assertEquals(epic, tasksList.get(0), "Задачи не совпадают.");

        tasksManager.deleteEpic(epicId);

        assertEquals(0, tasksManager.getAllEpics().size(), "Неверное количество задач.");

    }

    @Test
    void createSubTask() {

        final Task savedTask = tasksManager.getSubTask(subTaskId);
        assertEquals(epicId, subTask.getEpicId(), "Неверный id Epic");

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(subTask, savedTask, "Задачи не совпадают.");


        final List<SubTask> tasksList = tasksManager.getAllSubTasks();

        assertNotNull(tasksList, "Задачи на возвращаются.");
        assertEquals(1, tasksList.size(), "Неверное количество задач.");
        assertEquals(subTask, tasksList.get(0), "Задачи не совпадают.");

        tasksManager.deleteSubTask(subTaskId);

        assertEquals(0, tasksManager.getAllSubTasks().size(), "Неверное количество задач.");

        subTask.settingTheTime(20, "15.08.2023 - 14:00");

        assertEquals("15.08.2023 - 14:20", subTask.getEndTime(), "Неверное стартовое время");

    }

    @Test
    void updateTask() {
        task.setStatus(IN_PROGRESS);
        tasksManager.updateTask(task);
        assertEquals(IN_PROGRESS, task.getStatus(), "Неверное обновление статус Task");

    }

    @Test
    void updateEpic() {
        epic.setDescription("3");
        tasksManager.updateEpic(epic);
        assertEquals("3", epic.getDescription(), "Неверное обновление Description Epic");
    }

    @Test
    void updateSubTask() {
        subTask.setStatus(IN_PROGRESS);
        tasksManager.updateSubTask(subTask);
        assertEquals(IN_PROGRESS, subTask.getStatus(), "Неверное обновление статус SubTask");
        assertEquals(IN_PROGRESS, epic.getStatus(), "Неверный статус Epic");
    }


    @Test
    void deleteAllTasks() {
        tasksManager.deleteAllTasks();

        assertEquals(0, tasksManager.getAllTasks().size(), "Неверное количество задач.");
    }

    @Test
    void deleteAllEpics() {
        tasksManager.deleteAllEpics();

        assertEquals(0, tasksManager.getAllEpics().size(), "Неверное количество Epic.");
        assertEquals(0, tasksManager.getAllSubTasks().size(), "Неверное удаляет Subtask.");
    }

    @Test
    void deleteAllSubTasks() {
        tasksManager.deleteAllSubTasks();

        assertEquals(0, tasksManager.getAllSubTasks().size(), "Неверное удаляет Subtask.");
        assertEquals(NEW, epic.getStatus(), "Неверное количество Epic.");
    }

    @Test
    void deleteAll() {
        tasksManager.deleteAll();

        assertEquals(0, tasksManager.getAllTasks().size(), "Неверное количество Task.");
        assertEquals(0, tasksManager.getAllEpics().size(), "Неверное количество Epic.");
        assertEquals(0, tasksManager.getAllSubTasks().size(), "Неверное количество Subtask.");
    }

    @Test
    void getHistory() {
        tasksManager.getTask(taskId);
        tasksManager.getEpic(epicId);
        tasksManager.getSubTask(subTaskId);
        List<Task> tasks = List.of( tasksManager.getTask(taskId),
        tasksManager.getEpic(epicId),
        tasksManager.getSubTask(subTaskId));

        Assertions.assertArrayEquals(tasks.toArray(), tasksManager.getHistory().toArray(), "Неверный вывод истории");
        Assertions.assertIterableEquals(tasks, tasksManager.getHistory(), "Неверный вывод истории");

        tasksManager.deleteAll();

        assertEquals(0, tasksManager.getHistory().size(), "Неверный вывод истории");

    }
    @Test
    void getTheSubTasksEpic() throws FileNotFoundException {

        Assertions.assertIterableEquals(List.of(tasksManager.getSubTask(subTaskId)),
                tasksManager.getTheSubTasksEpic(epicId), " Неверный вывод SubTask");

        tasksManager.deleteAllSubTasks();

        assertEquals(0, tasksManager.getTheSubTasksEpic(epicId).size(), "Неверный размер List");
    }

}