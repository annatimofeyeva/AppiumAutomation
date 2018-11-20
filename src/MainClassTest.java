import org.junit.Assert;
import org.junit.Test;

public class MainClassTest {

    @Test
    public void testGetLocalNumber() {
        Assert.assertEquals("MainClass.getLocalNumber() return unexpected value",
                14, MainClass.getLocalNumber());
    }

    @Test
    public void testGetClassNumber() {
        Assert.assertTrue("class_number < 45",
                MainClass.getClassNumber() > 45);
    }

    @Test
    public void testGetClassString() {
        Assert.assertTrue("class_string variable doesn't contain \"hello\" or \"Hello\"",
                MainClass.getClassString().contains("hello") ||
                        MainClass.getClassString().contains("Hello"));
    }
}