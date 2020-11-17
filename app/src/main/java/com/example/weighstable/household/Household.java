package com.example.weighstable.household;
import java.io.Serializable;
import java.util.Date;

public class Household implements Serializable {
    People[] people;
    TrashCan can;
    Date trashDay;

    public Household(People[] people, int wLimit, int cLimit, Date trashDay) {
        this.people = people;
        this.can = new TrashCan(wLimit, cLimit);
        this.trashDay = trashDay;
    }
}
