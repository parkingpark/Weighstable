package com.example.weighstable.household;
import java.io.Serializable;
import java.util.Date;

public class Household implements Serializable {
    String[] people;
    TrashCan can;
    Date trashDay;

    public Household(String[] people, int wLimit, int cLimit, Date trashDay) {
        this.people = people;
        this.can = new TrashCan(wLimit, cLimit);
        this.trashDay = trashDay;
    }
}
