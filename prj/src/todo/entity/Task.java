package todo.entity;

import db.Entity;
import db.Trackable;
import java.util.Date;

public class Task extends Entity implements Trackable {
    public static final int TASK_ENTITY_CODE = 1;
    public String title;
    public String description;
    public Date dueDate;
    public Status status;
    private Date creationDate;
    private Date lastModificationDate;

    public Task(String title, String description, Date dueDate) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = Status.NotStarted;
    }

    @Override
    public Entity copy() {
        Task copyTask = new Task(title, description, dueDate);
        copyTask.id = id;
        copyTask.status = status;
        copyTask.creationDate = creationDate != null ? new Date(creationDate.getTime()) : null;
        copyTask.lastModificationDate = lastModificationDate != null ? new Date(lastModificationDate.getTime()) : null;
        return copyTask;
    }

    @Override
    public int getEntityCode() {
        return TASK_ENTITY_CODE;
    }

    @Override
    public void setCreationDate(Date date) {
        this.creationDate = date;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public void setLastModificationDate(Date date) {
        this.lastModificationDate = date;
    }

    @Override
    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    public enum Status {
        NotStarted,
        InProgress,
        Completed
    }
}