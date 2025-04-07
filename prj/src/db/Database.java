package db;
import db.exception.*;

import java.util.ArrayList;

public class Database {
    private static final ArrayList<Entity> entities = new ArrayList<>();
    private static int nextId = 1;

    private Database() {}

    public static void add(Entity entity) {
        Entity entityCopy = entity.copy(); // کپی می‌کنیم
        entityCopy.id = nextId;
        nextId++;
        entities.add(entityCopy);
    }

    public static Entity get(int id) {
        for (Entity entity : entities) {
            if (entity.id == id) {
                return entity.copy(); // کپی برمی‌گردونیم
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

    public static void update(Entity entity) {
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).id == entity.id) {
                entities.set(i, entity.copy()); // کپی رو جایگزین می‌کنیم
                return;
            }
        }
        throw new EntityNotFoundException(entity.id);
    }
}