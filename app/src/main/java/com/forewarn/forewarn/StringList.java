package com.forewarn.forewarn;

/**
 * Created by SamPastoriza on 10/24/15.
 */
public class StringList {

    private String name = null;
    private boolean selected = false;

    public StringList(String name, boolean selected)
    {
        this.name = name;
        this.selected = selected;
    }

    @Override
    public String toString() {
        if(this.selected) {
            return this.name + ", true";
        }
        return this.name + ", false";
    }

    /**
     * Set a new name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Flips the selected boolean
     */
    public void changeSelected() {
        this.selected = !this.selected;
    }

    /**
     * Returns the name
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * Return the selected boolean
     * @return
     */
    public boolean getSelected() {
        return this.selected;
    }

}