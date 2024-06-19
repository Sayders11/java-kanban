import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.FileBackedTaskManager;
import service.InMemoryHistoryManager;


public class Main {

    public static void main(String[] args) {
        //TaskManager taskManager = Managers.getDefaults();
        FileBackedTaskManager fileManager = new FileBackedTaskManager(new InMemoryHistoryManager());

        Task task1 = new Task("First task", "something to do");
        Task task2 = new Task("Second task", "smth should do");

        Epic epic1 = new Epic("First epic", "1st epic desc");
        Epic epic2 = new Epic("Second epic", "2nd epic desc");

        Subtask subtask1 = new Subtask("1st subtask", "smth",
                epic1);
        Subtask subtask2 = new Subtask("2nd subtask", "do do",
                epic1);
        Subtask subtask3 = new Subtask("3rd subtask", "hehe",
                epic2);

        fileManager.createTask(task1);
        fileManager.createTask(task2);

        fileManager.createEpic(epic1);
        fileManager.createSubtask(subtask1);
        fileManager.createSubtask(subtask2);

        fileManager.createEpic(epic2);
        fileManager.createSubtask(subtask3);

        subtask1.setEpicId(epic1.getId());
        subtask2.setEpicId(epic1.getId());

        subtask3.setEpicId(epic2.getId());

        System.out.println(task1);
        System.out.println(task2);

        System.out.println(epic1);
        System.out.println(subtask1);
        System.out.println(subtask2);

        System.out.println(epic2);
        System.out.println(subtask3);

        task1.setStatus(Status.IN_PROGRESS);
        task2.setStatus(Status.DONE);

        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.DONE);

        subtask3.setStatus(Status.DONE);

        fileManager.updateEpic(epic1);
        fileManager.updateEpic(epic2);

        System.out.println("CHEEECK --------------------------------------");

        System.out.println(task1);
        System.out.println(task2);

        System.out.println(epic1);
        System.out.println(subtask1);
        System.out.println(subtask2);

        System.out.println(epic2);
        System.out.println(subtask3);

        fileManager.deleteTask(task1.getId());
        fileManager.deleteEpic(epic1.getId());

        fileManager.getAllTasks();
        fileManager.getAllEpics();
    }

}
