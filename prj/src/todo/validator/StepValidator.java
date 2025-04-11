package todo.validator;

import db.*;
import db.exception.*;
import todo.entity.Step;

public class StepValidator implements Validator {
    @Override
    public void validate(Entity entity) throws InvalidEntityException {
        if (!(entity instanceof Step)) {
            throw new IllegalArgumentException("Entity must be of type Step");
        }
        Step step = (Step) entity;
        if (step.title == null || step.title.isEmpty()) {
            throw new InvalidEntityException("Step title cannot be empty");
        }
        try {
            Database.get(step.taskRef);
        } catch (EntityNotFoundException e) {
            throw new InvalidEntityException("Cannot find task with ID=" + step.taskRef);
        }
    }
}