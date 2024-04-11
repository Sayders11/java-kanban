import service.TaskManager;
import model.*;


public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("First task", "something to do");
        Task task2 = new Task("Second task", "smth should do");

        Epic epic1 = new Epic("First epic", "1st epic desc");
        Epic epic2 = new Epic("Second epic", "2nd epic desc");

        SubTask subTask1 = new SubTask("1st subtask", "smth",
                epic1);
        SubTask subTask2 = new SubTask("2nd subtask", "do do",
                epic1);
        SubTask subTask3 = new SubTask("3rd subtask", "hehe",
                epic2);

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.createEpic(epic1);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);

        taskManager.createEpic(epic2);
        taskManager.createSubTask(subTask3);

        subTask1.setEpicId(epic1.getId());
        subTask2.setEpicId(epic1.getId());

        subTask3.setEpicId(epic2.getId());

        System.out.println(task1);
        System.out.println(task2);

        System.out.println(epic1);
        System.out.println(subTask1);
        System.out.println(subTask2);

        System.out.println(epic2);
        System.out.println(subTask3);

        task1.setStatus(Status.IN_PROGRESS);
        task2.setStatus(Status.DONE);

        subTask1.setStatus(Status.IN_PROGRESS);
        subTask2.setStatus(Status.DONE);

        subTask3.setStatus(Status.DONE);

        taskManager.epicUpdate(epic1);
        taskManager.epicUpdate(epic2);

        System.out.println("CHEEECK --------------------------------------");

        System.out.println(task1);
        System.out.println(task2);

        System.out.println(epic1);
        System.out.println(subTask1);
        System.out.println(subTask2);

        System.out.println(epic2);
        System.out.println(subTask3);

        taskManager.delete(task1.getId());
        taskManager.deleteEpic(epic1.getId());

        taskManager.getAllTasks();
        taskManager.getAllEpics();
    }

}
