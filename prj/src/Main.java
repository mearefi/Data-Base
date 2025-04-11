import example.Human;
import example.HumanValidator;
import example.*;
import db.*;
import db.exception.*;

import java.util.Date;

public class Main {
    public static void main(String[] args) throws InvalidEntityException {
        Document doc = new Document("Eid Eid Eid");

        int assignedId = Database.add(doc);
        doc.id = assignedId;

        if (doc instanceof Trackable) {
            Trackable trackable = (Trackable) doc;
            trackable.setCreationDate(new Date());
            trackable.setLastModificationDate(new Date());
        }

        Database.add(doc);

        System.out.println("Document added");

        System.out.println("id: " + doc.id);
        System.out.println("content: " + doc.content);
        System.out.println("creation date: " + doc.getCreationDate());
        System.out.println("last modification date: " + doc.getLastModificationDate());
        System.out.println();

        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            System.out.println("Sleep interrupted!");
        }

        doc.content = "This is the new content";

        Database.update(doc);

        if (doc instanceof Trackable) {
            Trackable trackable = (Trackable) doc;
            trackable.setLastModificationDate(new Date());
        }

        System.out.println("Document updated");
        System.out.println("id: " + doc.id);
        System.out.println("content: " + doc.content);
        System.out.println("creation date: " + doc.getCreationDate());
        System.out.println("last modification date: " + doc.getLastModificationDate());
    }
}