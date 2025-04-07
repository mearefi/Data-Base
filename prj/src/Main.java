import example.Human;
import db.*;
import db.exception.*;

public class Main {
    public static void main(String[] args) {
        Human ali = new Human("Ali");
        Human aliCopy = ali.copy();

        System.out.println("ali's name: " + ali.name);
        System.out.println("aliCopy's name: " + aliCopy.name);
        System.out.println();

        ali.name = "Ali Hosseini";

        System.out.println("ali's name: " + ali.name);
        System.out.println("aliCopy's name: " + aliCopy.name);
    }
}