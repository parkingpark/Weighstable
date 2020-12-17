package com.example.weighstable.util;
import java.io.*;
import android.content.Context;
import com.example.weighstable.household.Household;

public class DeviceReadWrite {
    private static final String FILE = "data.txt";

    public static void writeHousehold(Household household, Context context) throws IOException {
        FileOutputStream outputStream = context.getApplicationContext().openFileOutput(FILE, Context.MODE_PRIVATE);
        ObjectOutputStream objectOutput = new ObjectOutputStream(outputStream);
        household.writeObject(objectOutput);
    }

    public static Household readHousehold(Context context) throws IOException, ClassNotFoundException {
        Household household = new Household();
        FileInputStream inputStream = context.getApplicationContext().openFileInput(FILE);
        try {
            ObjectInputStream objectInput = new ObjectInputStream(inputStream);
            household.readObject(objectInput);
        } catch (EOFException e) {
            return household;
        }
        return household;
    }
}