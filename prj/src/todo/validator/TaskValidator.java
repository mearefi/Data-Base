package todo.validator;

import db.*;
import db.exception.*;
import todo.entity.Task;

public class TaskValidator implements Validator {
    @Override
    public void validate(Entity entity) throws InvalidEntityException {
        if (!(entity instanceof Task)) {
            throw new IllegalArgumentException("Entity must be of type Task");
        }
        Task task = (Task) entity;
        if (task.title == null || task.title.isEmpty()) {
            throw new InvalidEntityException("Task title cannot be empty");
        }
    }
}