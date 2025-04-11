package todo.service;

import db.*;
import db.exception.*;
import todo.entity.*;
import java.util.ArrayList;
import java.util.Date;

public class TaskService {
    public static void saveTask(String title, String description, Date dueDate) throws InvalidEntityException {
        Task task = new Task(title, description, dueDate);
        task.setCreationDate(new Date());
        Database.add(task);
    }

    public static void setAsCompleted(int taskId) throws InvalidEntityException {
        Task task = (Task) Database.get(taskId);
        task.status = Task.Status.Completed;
        task.setLastModificationDate(new Date());
        Database.update(task);
        ArrayList<Entity> steps = Database.getAll(Step.STEP_ENTITY_CODE);
        for (Entity entity : steps) {
            Step step = (Step) entity;
            if (step.taskRef == taskId && step.status != Step.Status.Completed) {
                step.status = Step.Status.Completed;
                Database.update(step);
            }
        }
    }
}