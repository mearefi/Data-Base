package db;
import db.exception.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;

public class Database {
    private static final ArrayList<Entity> entities = new ArrayList<>();
    private static int nextId = 1;
    private static HashMap<Integer, Validator> validators = new HashMap<>();

    private Database() {}

    public static void registerValidator(int entityCode, Validator validator) {
        if (validators.containsKey(entityCode)) {
            throw new IllegalArgumentException("Validator already registered for entity code: " + entityCode);
        }
        validators.put(entityCode, validator);
    }

    public static int add(Entity entity) throws InvalidEntityException {
        Validator validator = validators.get(entity.getEntityCode());
        if (validator != null) {
            validator.validate(entity);
        }
        Entity entityCopy = entity.copy();
        entityCopy.id = nextId;
        nextId++;
        if (entityCopy instanceof Trackable) {
            Trackable trackable = (Trackable) entityCopy;
            Date now = new Date();
            trackable.setCreationDate(now);
            trackable.setLastModificationDate(now);
        }
        entities.add(entityCopy);
        return entityCopy.id;
    }

    public static Entity get(int id) {
        for (Entity entity : entities) {
            if (entity.id == id) {
                return entity.copy();
            }
        }
        throw new EntityNotFoundException(id);
    }

    public static void delete(int id) {
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).id == id) {
                entities.remove(i);
                return;
            }
        }
        throw new EntityNotFoundException(id);
    }

    public static void update(Entity entity) throws InvalidEntityException {
        Validator validator = validators.get(entity.getEntityCode());
        if (validator != null) {
            validator.validate(entity);
        }
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).id == entity.id) {
                Entity entityCopy = entity.copy();
                if (entityCopy instanceof Trackable) {
                    Trackable trackable = (Trackable) entityCopy;
                    trackable.setLastModificationDate(new Date());
                }
                entities.set(i, entityCopy);
                return;
            }
        }
        throw new EntityNotFoundException(entity.id);
    }
}