package lab_6;

import java.io.*;

/**
 * Provides serialization and deserialization of objects for data persistence.
 */
public class dataManager {

    /**
     * Saves an object to file.
     * @throws NullPointerException if object is null
     */
    public static void saveObject(Object obj, String filename) throws IOException {
        if (obj == null) {
            throw new NullPointerException("Cannot save a null object.");
        }
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
        oos.writeObject(obj);
        oos.close();
    }

    /**
     * Loads a saved object from file.
     */
    public static Object loadObject(String filename) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
        Object obj = ois.readObject();
        ois.close();
        return obj;
    }
}
