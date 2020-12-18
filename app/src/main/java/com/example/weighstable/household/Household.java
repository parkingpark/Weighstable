package com.example.weighstable.household;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.lang.Math;

public class Household implements Serializable {
    String[] people;
    int wLimit;
    int cLimit;
    String trashDay;

    public Household() {
        people = null;
        wLimit = 0;
        cLimit = 0;
        trashDay = null;
    }

    public Household(String[] people, int wLimit, int cLimit, String trashDay) {
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
        for (int i=0; i < p.length; i++) {
            int pos = (int) (Math.random() * p.length);
            String temp = p[pos];
            p[pos] = p[i];
            p[i] = temp;
        }
        this.people = p;
    }

    public void rotate() {
        if (people != null) {
            String temp = people[0];
            for (int i=1; i < people.length; i++) {
                people[i - 1] = people[i];
            }
            people[people.length - 1] = temp;
        }
    }

    public void setTrashDay(String d) {
        this.trashDay = d;
    }

    public String[] getPeople() {
        return people;
    }

    public String getTrashDay() {
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
        trashDay = (String) stream.readObject();
    }
}
