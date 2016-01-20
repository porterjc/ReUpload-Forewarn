import com.forewarn.forewarn.StringList;

import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Created by SamPastoriza on 11/3/15.
 */
public class StringListTest {

    /**
     * Test initialize method
     */
    @Test
    public void testInitializeStringList() {
        StringList list = new StringList("Test List", false);
        StringList list2 = new StringList("Test List2", true);
        assertNotNull(list);
        assertNotNull(list2);
    }

    /**
     * Test overridden toString() method
     */
    @Test
    public void testToString() {
        StringList list = new StringList("Test List", false);
        StringList list2 = new StringList("Test List2", true);
        assertEquals("Test List, false", list.toString());
        assertEquals("Test List2, true", list2.toString());
    }

    /**
     * Test setName(), getName()
     */
    @Test
    public void testSetGetName() {
        StringList list = new StringList("Test List", false);
        StringList list2 = new StringList("Test List2", true);
        list.setName("Test List3");
        list2.setName("Test List4");
        assertEquals("Test List3, false", list.toString());
        assertEquals("Test List4, true", list2.toString());
    }

    /**
     * Test changeSelected()
     */
    @Test
    public void testChangeSelected() {
        StringList list = new StringList("Test List", false);
        StringList list2 = new StringList("Test List2", true);
        list.changeSelected();
        list2.changeSelected();
        assertEquals("Test List, true", list.toString());
        assertEquals("Test List2, false", list2.toString());
    }

    /**
     * Test getSelected()
     */
    @Test
    public void testGetSelected() {
        StringList list = new StringList("Test List", false);
        assertEquals(false, list.getSelected());
    }
}
