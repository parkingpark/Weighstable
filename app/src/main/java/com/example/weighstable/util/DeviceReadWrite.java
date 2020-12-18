package com.example.weighstable.util;
import java.io.*;
import android.content.Context;
import com.example.weighstable.household.Household;

public class DeviceReadWrite {
    private static final String FILE = "data.txt";

    public static void writeHousehold(Household household, Context context) throws IOException {
            FileOutputStream outputStream = context.openFileOutput(FILE, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutput = new ObjectOutputStream(outputStream);
            household.writeObject(objectOutput);
            objectOutput.close();
            outputStream.close();
    }

    public static Household readHousehold(Context context) throws IOException, ClassNotFoundException {
        Household household = new Household();
        FileInputStream inputStream;
        try {
            inputStream = context.openFileInput(FILE);
        } catch (FileNotFoundException e) {
            writeHousehold(household, context);
            inputStream = context.openFileInput(FILE);
        }
        try {
            ObjectInputStream objectInput = new ObjectInputStream(inputStream);
            household.readObject(objectInput);
            objectInput.close();
        } catch (EOFException e) {
            return household;
        }
        inputStream.close();
        return household;
    }
}