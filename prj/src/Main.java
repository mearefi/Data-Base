import db.*;
import db.exception.*;
import todo.entity.*;
import todo.service.*;
import todo.validator.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Scanner;

public class Main {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final Scanner scr = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.print("Enter command: ");
            String command = scr.nextLine().trim();

            try {
                switch (command) {
                    case "add task":
                        handleAddTask();
                        break;
                    case "add step":
                        handleAddStep();
                        break;
                    case "delete":
                        handleDelete();
                        break;
                    case "update task":
                        handleUpdateTask();
                        break;
                    case "update step":
                        handleUpdateStep();
                        break;
                    case "get task-by-id":
                        handleGetTaskById();
                        break;
                    case "get all-tasks":
                        handleGetAllTasks();
                        break;
                    case "get incomplete-tasks":
                        handleGetIncompleteTasks();
                        break;
                    case "exit":
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Unknown command.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void handleAddTask() throws Exception {
        System.out.print("Title: ");
        String title = scr.nextLine();
        System.out.print("Description: ");
        String description = scr.nextLine();
        System.out.print("Due date (yyyy-MM-dd): ");
        Date dueDate = dateFormat.parse(scr.nextLine());
        TaskService.saveTask(title, description, dueDate);
        Task task = (Task) Database.getAll(Task.TASK_ENTITY_CODE).get(Database.getAll(Task.TASK_ENTITY_CODE).size() - 1);
        System.out.println("Task saved successfully.\nID: " + task.id);
    }

    private static void handleAddStep() throws InvalidEntityException {
        System.out.print("TaskID: ");
        int taskId = Integer.parseInt(scr.nextLine());
        System.out.print("Title: ");
        String title = scr.nextLine();
        StepService.saveStep(taskId, title);
        Step step = (Step) Database.getAll(Step.STEP_ENTITY_CODE).get(Database.getAll(Step.STEP_ENTITY_CODE).size() - 1);
        System.out.println("Step saved successfully.\nID: " + step.id);
    }

    private static void handleDelete() throws InvalidEntityException {
        System.out.print("ID: ");
        int id = Integer.parseInt(scr.nextLine());
        Entity entity = Database.get(id);
        if (entity instanceof Task) {
            ArrayList<Entity> steps = Database.getAll(Step.STEP_ENTITY_CODE);
            for (Entity e : steps) {
                Step step = (Step) e;
                if (step.taskRef == id) {
                    Database.delete(step.id);
                }
            }
        }
        Database.delete(id);
        System.out.println("Entity with ID=" + id + " successfully deleted.");
    }

    private static void handleUpdateTask() throws Exception {
        System.out.print("ID: ");
        int id = Integer.parseInt(scr.nextLine());
        Task task = (Task) Database.get(id);
        System.out.print("Field: ");
        String field = scr.nextLine().toLowerCase();
        System.out.print("New Value: ");
        String newValue = scr.nextLine();
        String oldValue = "";
        switch (field) {
            case "title":
                oldValue = task.title;
                task.title = newValue;
                break;
            case "description":
                oldValue = task.description;
                task.description = newValue;
                break;
            case "dueDate":
                oldValue = dateFormat.format(task.dueDate);
                task.dueDate = dateFormat.parse(newValue);
                break;
            case "status":
                oldValue = task.status.name();
                task.status = Task.Status.valueOf(newValue);
                if (task.status == Task.Status.Completed) {
                    TaskService.setAsCompleted(id);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid field: " + field);
        }
        task.setLastModificationDate(new Date());
        Database.update(task);
        System.out.println("Successfully updated the task.\nField: " + field + "\nOld Value: " + oldValue + "\nNew Value: " + newValue + "\nModification Date: " + task.getLastModificationDate());
    }

    private static void handleUpdateStep() throws InvalidEntityException {
        System.out.print("ID: ");
        int id = Integer.parseInt(scr.nextLine());
        Step step = (Step) Database.get(id);
        System.out.print("Field: ");
        String field = scr.nextLine().toLowerCase();
        System.out.print("New Value: ");
        String newValue = scr.nextLine();
        String oldValue = "";
        switch (field) {
            case "title":
                oldValue = step.title;
                step.title = newValue;
                break;
            case "status":
                oldValue = step.status.name();
                step.status = Step.Status.valueOf(newValue);
                break;
            case "taskref":
                throw new IllegalArgumentException("taskRef cannot be updated");
            default:
                throw new IllegalArgumentException("Invalid field: " + field);
        }
        Database.update(step);
        Task task = (Task) Database.get(step.taskRef);
        if (step.status == Step.Status.Completed && task.status == Task.Status.NotStarted) {
            task.status = Task.Status.InProgress;
            Database.update(task);
        }
        ArrayList<Entity> steps = Database.getAll(Step.STEP_ENTITY_CODE);
        boolean allCompleted = true;
        for (Entity e : steps) {
            Step s = (Step) e;
            if (s.taskRef == step.taskRef && s.status != Step.Status.Completed) {
                allCompleted = false;
                break;
            }
        }
        if (allCompleted) {
            task.status = Task.Status.Completed;
            Database.update(task);
        }
        System.out.println("Successfully updated the step.\nField: " + field + "\nOld Value: " + oldValue + "\nNew Value: " + newValue + "\nModification Date: " + task.getLastModificationDate());
    }

    private static void handleGetTaskById() throws EntityNotFoundException {
        System.out.print("ID: ");
        int id = Integer.parseInt(scr.nextLine());
        Task task = (Task) Database.get(id);
        System.out.println("ID: " + task.id + "\nTitle: " + task.title + "\nDue Date: " + dateFormat.format(task.dueDate) + "\nStatus: " + task.status);
        ArrayList<Entity> steps = Database.getAll(Step.STEP_ENTITY_CODE);
        System.out.println("Steps:");
        for (Entity e : steps) {
            Step step = (Step) e;
            if (step.taskRef == id) {
                System.out.println("    + " + step.title + ":\n        ID: " + step.id + "\n        Status: " + step.status);
            }
        }
    }

    private static void handleGetAllTasks() {
        ArrayList<Entity> tasks = Database.getAll(Task.TASK_ENTITY_CODE);
        tasks.sort(Comparator.comparing(e -> ((Task) e).dueDate));
        for (Entity e : tasks) {
            Task task = (Task) e;
            System.out.println("ID: " + task.id + "\nTitle: " + task.title + "\nDue Date: " + dateFormat.format(task.dueDate) + "\nStatus: " + task.status);
            ArrayList<Entity> steps = Database.getAll(Step.STEP_ENTITY_CODE);
            if (!steps.isEmpty()) {
                System.out.println("Steps:");
                for (Entity se : steps) {
                    Step step = (Step) se;
                    if (step.taskRef == task.id) {
                        System.out.println("    + " + step.title + ":\n        ID: " + step.id + "\n        Status: " + step.status);
                    }
                }
            }
            System.out.println();
        }
    }

    private static void handleGetIncompleteTasks() {
        ArrayList<Entity> tasks = Database.getAll(Task.TASK_ENTITY_CODE);
        tasks.sort(Comparator.comparing(e -> ((Task) e).dueDate));
        for (Entity e : tasks) {
            Task task = (Task) e;
            if (task.status != Task.Status.Completed) {
                System.out.println("ID: " + task.id + "\nTitle: " + task.title + "\nDue Date: " + dateFormat.format(task.dueDate) + "\nStatus: " + task.status);
                ArrayList<Entity> steps = Database.getAll(Step.STEP_ENTITY_CODE);
                if (!steps.isEmpty()) {
                    System.out.println("Steps:");
                    for (Entity se : steps) {
                        Step step = (Step) se;
                        if (step.taskRef == task.id) {
                            System.out.println("    + " + step.title + ":\n        ID: " + step.id + "\n        Status: " + step.status);
                        }
                    }
                }
                System.out.println();
            }
        }
    }
}