package lab_6;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class dataManagerTest {

    @Test
    public void testSaveAndLoadSingleObject() throws IOException, ClassNotFoundException, Exception {
        checkingAcc acc = new checkingAcc();
        acc.deposit(100);

        dataManager.saveObject(acc, "test_checking.ser");
        checkingAcc loaded = (checkingAcc) dataManager.loadObject("test_checking.ser");

        assertEquals(acc.getBalance(), loaded.getBalance(), 0.001);
    }

    @Test
    public void testNullObjectSave() {
        assertThrows(NullPointerException.class, () -> {
            dataManager.saveObject(null, "null_test.ser");
        });
    }

    @Test
    public void testIncompatibleType() throws IOException {
        checkingAcc acc = new checkingAcc();
        dataManager.saveObject(acc, "badfile.ser");

        assertThrows(ClassCastException.class, () -> {
            savingAcc wrongType = (savingAcc) dataManager.loadObject("badfile.ser");
        });
    }

    @Test
    public void testLoadNonExistentFile() {
        assertThrows(IOException.class, () -> {
            dataManager.loadObject("fileDoesNotExist.ser");
        });
    }
}
