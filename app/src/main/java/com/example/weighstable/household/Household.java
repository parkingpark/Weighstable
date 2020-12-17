package com.example.weighstable.household;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

public class Household implements Serializable {
    String[] people;
    int wLimit;
    int cLimit;
    Date trashDay;

    public Household() {
        people = null;
        wLimit = 0;
        cLimit = 0;
        trashDay = null;
    }

    public Household(String[] people, int wLimit, int cLimit, Date trashDay) {
        this.people = people;
        this.wLimit = wLimit;
        this.cLimit = cLimit;
        this.trashDay = trashDay;
    }

    public int getwLimit() {
        return wLimit;
    }

    public void setwLimit(int wLimit) {
        this.wLimit = wLimit;
    }

    public int getcLimit() {
        return cLimit;
    }

    public void setcLimit(int cLimit) {
        this.cLimit = cLimit;
    }

    public void setPeople(String[] p) {
        this.people = p;
    }

    public void setTrashDay(Date d) {
        this.trashDay = d;
    }

    public String[] getPeople() {
        return people;
    }

    public Date getTrashDay() {
        return trashDay;
    }

    public void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(people);
        stream.writeInt(wLimit);
        stream.writeInt(cLimit);
        stream.writeObject(trashDay);
    }

    public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        people = (String[]) stream.readObject();
        wLimit = stream.readInt();
        cLimit = stream.readInt();
        trashDay = (Date) stream.readObject();
    }
}
