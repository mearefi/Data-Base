package todo.entity;

import db.Entity;

public class Step extends Entity {
    public static final int STEP_ENTITY_CODE = 2;
    public String title;
    public Status status;
    public final int taskRef;

    public Step(String title, int taskRef) {
        this.title = title;
        this.taskRef = taskRef;
        this.status = Status.NotStarted;
    }
}