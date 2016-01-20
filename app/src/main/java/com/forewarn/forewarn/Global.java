package com.forewarn.forewarn;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.LinkedList;

public class Global extends Application {

    private LinkedList<StringList> nameList;
    private LinkedList<LinkedList<StringList>> ingredientList;

    public Global() {
        this.initialize();
    }


    /**
     * Initializes the Linked Lists at the very beginning
     */
    public void initialize() {
        nameList = new LinkedList<>();
        ingredientList = new LinkedList<>();
    }

    /**
     * Returns the size of the namelist
     */
    public int getNameListSize() {
        return this.nameList.size();
    }

    /**
     * Returns the size of the ingredient list at a given position
     */
    public int getIngredientListSize(int position) {
        return this.ingredientList.get(position).size();
    }


    /**
     * Returns a linked list of strings that are the names of the lists
     */
    public LinkedList<String> getNameList() {
        if(this.nameList == null) {
            return null;
        }
        LinkedList<String> names = new LinkedList<String>();
        for(int i = 0; i < this.nameList.size(); i++) {
            names.add(this.nameList.get(i).getName());
        }
        return names;
    }

    /**
     * Returns a linked list of stringlists that are the names and cooresponding
     * checkbox selection flags
     */
    public LinkedList<StringList> getNameStringList() {
        return this.nameList;
    }

    /**
     * Gets a StringList Ingredient at a given linked list position and list position
     */
    public StringList getIngredient(int linkedListPos, int position) {
        return this.ingredientList.get(linkedListPos).get(position);
    }


    /**
     * Gets a StringList Name at a given position
     */
    public StringList getName(int position) {
        return this.nameList.get(position);
    }


    /**
     * Gets the linked list of stringlist from the ingredient list that cooresponds to
     * the row that was clicked in the top list
     */
    public  LinkedList<StringList> getIngredientStringList(int position) {
        return this.ingredientList.get(position);
    }

    /**
     * Gets the number of booleans that are set to true from the namelist
     */
    public int getNumberSelectedNames() {
        int numSelected = 0;
        for(int i = 0; i < this.nameList.size(); i++) {
            if(this.nameList.get(i).getSelected()) {
                numSelected++;
            }
        }
        return numSelected;
    }

    /**
     * Gets the number of booleans that are set to true from the given ingredient list
     */
    public int getNumberSelectedIngredients(int position) {
        int numSelected = 0;
        LinkedList<StringList> list = this.ingredientList.get(position);
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getSelected()) {
                numSelected++;
            }
        }
        return numSelected;
    }

    /**
     * Adds a new StringList to the end of the two linkedlists
     */
    public boolean addList(StringList name) {
        this.nameList.add(name);
        this.ingredientList.add(new LinkedList<StringList>());
        return true;
    }

    /**
     * Adds an ingredient to the end of the ingredient list
     */
    public boolean addIngredient(int linkedListPos, StringList ingredient) {
        if(this.ingredientList.get(linkedListPos) == null) {
            return false;
        }
        this.ingredientList.get(linkedListPos).add(ingredient);
        return true;
    }

    /**
     * Deletes the names that have their selected booleans set to true
     * If the selected booleans are true, the checkbox has been selected
     */
    public boolean deleteSelectedNames() {
        if(this.nameList == null) {
            return false;
        }
        int size = this.nameList.size();
        int numberDeleted = 0;
        for(int i = 0; i-numberDeleted < this.nameList.size(); i++) {
            if(this.nameList.get(i - numberDeleted).getSelected()) {
                this.ingredientList.get(i-numberDeleted).clear();
                this.nameList.remove(i - numberDeleted++);

            }
        }
        if(this.nameList.size() == size) {
            return false;
        }
        return true;
    }

    /**
     * Deletes the ingredients that have their selected booleans set to true
     * If the selected booleans are true, the checkbox has been selected
     */
    public boolean deleteSelectedIngredients(int position) {
        LinkedList<StringList> selectedIngredients = this.ingredientList.get(position);
        if(selectedIngredients == null) {
            return false;
        }
        int size = selectedIngredients.size();
        int numberDeleted = 0;
        for(int i = 0; i-numberDeleted < selectedIngredients.size(); i++) {
            if(selectedIngredients.get(i - numberDeleted).getSelected()) {
                selectedIngredients.remove(i - numberDeleted);
                numberDeleted++;
            }
        }
        System.out.println("List Size" + selectedIngredients.size());
        if(selectedIngredients.size() == size) {
            return false;
        }
        return true;
    }

    /**
     * Sets a new name for a name list
     */
    public void setName(String name, int position) {
        this.nameList.get(position).setName(name);
    }

    /**
     * Sets a new ingredient name for an specified ingredient in the ingredient list
     */
    public void setIngredient(String ingredient, int listPosition, int position) {
        this.ingredientList.get(position).get(listPosition).setName(ingredient);
    }

    public void writeToFile() {
        try {
            // Create a new output file stream
            FileOutputStream file = openFileOutput("saved.txt", Context.MODE_PRIVATE);
            OutputStreamWriter bw = new OutputStreamWriter(file);
            for(int i = 0; i < this.nameList.size(); i++) {
                System.out.println(this.nameList.get(i).getName());
                if(i == this.nameList.size() - 1) {
                    bw.write(this.nameList.get(i).getName());
                } else {
                    bw.write(this.nameList.get(i).getName() + ",");
                }
            }
            bw.write("\n");
            for(int j = 0; j < this.ingredientList.size(); j++) {
                for (int k = 0; k < this.ingredientList.get(j).size(); k++) {
                    if(k == this.ingredientList.get(j).size() - 1) {
                        bw.write(this.ingredientList.get(j).get(k).getName());
                    } else {
                        bw.write(this.ingredientList.get(j).get(k).getName() + ",");
                    }
                }
                bw.write("\n");
            }
            bw.close();
            //Log.d("Success", "Success");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void readFromFile() {
//        BufferedReader br;

        try {
            FileInputStream fileIn=openFileInput("saved.txt");
            InputStreamReader in = new InputStreamReader(fileIn);
            BufferedReader br = new BufferedReader(in);
            //Log.d("Opened", "Opened");
            this.initialize();
            String line = br.readLine();
            if(line.equals(""))
                return;
            String[] lineArray = line.split(",");
            //Log.d("line0", line);
            //Log.d("array0", Arrays.toString(lineArray));
            for(int i = 0; i < lineArray.length; i++) {
                this.addList(new StringList(lineArray[i], false));
            }
            int j = 0;
            while(true) {
                line = br.readLine();
                if(line == null || line.equals(""))
                {
                    break;
                }
                //Log.d("Line" + j, line);
                lineArray = line.split(",");
                //Log.d("Array" + j, Arrays.toString(lineArray));
                this.ingredientList.add(new LinkedList<StringList>());
                for (int k = 0; k < lineArray.length; k++) {
                    this.addIngredient(j, new StringList(lineArray[k], false));
                }
                j++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}