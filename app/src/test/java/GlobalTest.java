import com.forewarn.forewarn.Global;
import com.forewarn.forewarn.StringList;

import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;
/**
 * Created by SamPastoriza on 11/3/15.
 */
public class GlobalTest {

    public Global g;

    @Test
    public void testInitializeGlobal() {
        g = new Global();
        g.initialize();
        assertEquals(new LinkedList<StringList>(), g.getNameStringList());
    }

    /**
     * Test getNameList(), addList()
     */
    @Test
    public void testGlobal2() {
        g = new Global();
        g.initialize();
        g.addList(new StringList("Sam's List", false));
        g.addList(new StringList("Jack's List", false));
        g.addList(new StringList("Matt's List", true));
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add("Sam's List");
        linkedList.add("Jack's List");
        linkedList.add("Matt's List");
        assertArrayEquals(linkedList.toArray(), g.getNameList().toArray());
    }

    /**
     * Test getName(), toString()
     */
    @Test
    public void testGlobal3() {
        g = new Global();
        g.initialize();
        g.addList(new StringList("Sam's List", false));
        g.addList(new StringList("Jack's List", false));
        g.addList(new StringList("Matt's List", true));
        assertEquals("Sam's List, false", g.getName(0).toString());
        assertEquals("Jack's List, false", g.getName(1).toString());
        assertEquals("Matt's List, true", g.getName(2).toString());
    }

    /**
     * Test getNumberSelectedNames()
     */
    @Test
    public void testGlobal4() {
        g = new Global();
        g.initialize();
        g.addList(new StringList("Sam's List", false));
        g.addList(new StringList("Jack's List", false));
        g.addList(new StringList("Matt's List", true));
        assertEquals(1, g.getNumberSelectedNames());
    }

    /**
     * Test deleteSelectedNames(), getNameListSize()
     */
    @Test
    public void testGlobal5() {
        g = new Global();
        g.initialize();
        g.addList(new StringList("Sam's List", false));
        g.addList(new StringList("Jack's List", false));
        g.addList(new StringList("Matt's List", true));
        g.deleteSelectedNames();
        assertEquals(2, g.getNameListSize());
    }


    /**
     * Test setName()
     */
    @Test
    public void testGlobal6() {
        g = new Global();
        g.initialize();
        g.addList(new StringList("Sam's List", false));
        g.addList(new StringList("Jack's List", false));
        g.addList(new StringList("Matt's List", true));
        g.setName("Matthew's List", 2);
        assertEquals("Matthew's List, true", g.getName(2).toString());
    }

    /**
     * Test getNumberSelectedIngredients(), addIngredient()
     */
    @Test
    public void testGlobal7() {
        g = new Global();
        g.initialize();
        g.addList(new StringList("Sam's List", false));
        g.addIngredient(0, new StringList("cashew", true));
        g.addIngredient(0, new StringList("pistachio", false));
        g.addIngredient(0, new StringList("mango", true));
        assertEquals(2, g.getNumberSelectedIngredients(0));
    }

    /**
     * Test deleteSelectedIngredients(), getIngredientListSize()
     */
    @Test
    public void testGlobal8() {
        g = new Global();
        g.initialize();
        g.addList(new StringList("Sam's List", false));
        g.addIngredient(0, new StringList("cashew", true));
        g.addIngredient(0, new StringList("pistachio", false));
        g.addIngredient(0, new StringList("mango", true));
        g.deleteSelectedIngredients(0);
        assertEquals(1, g.getIngredientListSize(0));
    }

    /**
     * Test setIngredient(), getIngredient
     */
    @Test
    public void testGlobal9() {
        g = new Global();
        g.initialize();
        g.addList(new StringList("Sam's List", false));
        g.addIngredient(0, new StringList("cashew", true));
        g.addIngredient(0, new StringList("pistachio", false));
        g.addIngredient(0, new StringList("mango", true));
        g.setIngredient("peanut", 1, 0);
        assertEquals("peanut, false", g.getIngredient(0, 1).toString());
    }

}
