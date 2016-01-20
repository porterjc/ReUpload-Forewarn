package com.forewarn.forewarn;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.LinkedList;

public class CustomizeSubListActivity extends AppCompatActivity {

    ListView subListView;
    TextView textView;
    MyCustomAdapter adapter;
    EditText edit;
    Button deleteButton;
    Global g;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_sub_list);
        Bundle bundle = getIntent().getExtras();
        g = (Global)getApplication();

        //Get all buttons/views
        deleteButton = (Button) findViewById(R.id.deleteButton);
        subListView = (ListView) findViewById(R.id.listView);
        edit = (EditText) findViewById(R.id.textItem);
        textView = (TextView) findViewById(R.id.textSubView);

        //Gets the correct index for the linked list in ingredient linked list
        position = (int) bundle.get("position");
        textView.setText(g.getName(position).getName());

        if(g.getIngredientStringList(position).size() > 0 && deleteButton.getVisibility() != View.VISIBLE) {
            deleteButton.setVisibility(View.VISIBLE);
        }

        adapter = new MyCustomAdapter(this, android.R.layout.simple_list_item_1, g.getIngredientStringList(position));

        /**
         * Sets on long click listener for editing rows in sublist
         */
        subListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            EditText text;
            int listPos;
            PopupWindow popup;

            @Override
            public boolean onItemLongClick(AdapterView<?> av, View view, int listPosition, long id) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.edit_popup, null, false);
                popup = new PopupWindow(popupView, 400, 440, true);
                listPos = listPosition;
                popup.showAtLocation(av, Gravity.CENTER, 0, 0);

                text = (EditText) popupView.findViewById(R.id.editText);
                text.setText(g.getIngredientStringList(position).get(listPos).getName());

                //Add on click listener for save button
                Button save = (Button) popupView.findViewById(R.id.saveButton);
                save.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View popupView) {
                        g.setIngredient(text.getText().toString(), listPos, position);
                        adapter.updateStringList(g.getIngredientStringList(position));
                        adapter.notifyDataSetChanged();
                        popup.dismiss();
                        g.writeToFile();
                    }
                });

                //Add on click listener for cancel button
                Button cancel = (Button) popupView.findViewById(R.id.cancelButton);
                cancel.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View popupView) {
                        popup.dismiss();
                    }
                });
                return true;
            }
        });

        subListView.setAdapter(adapter);

        /**
         * Sets on key listener for edit text when inserting new ingredients into the listview
         * Only adds rows if the text is not blank
         */
        edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER
                        && event.getAction() == KeyEvent.ACTION_DOWN
                        && !(edit.getText().toString().equals(""))) {
                    StringList list = new StringList(edit.getText().toString(), false);
                    g.addIngredient(position, list);
                    adapter.updateStringList(g.getIngredientStringList(position));
                    edit.setText("");
                    if (deleteButton.getVisibility() == View.GONE) {
                        deleteButton.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                    g.writeToFile();
                    return true;
                }
                return false;
            }
        });

        /**
         * Sets on click listener for delete button
         * Only deletes if at least one checkbox is selected
         */
        deleteButton.setOnClickListener(new View.OnClickListener() {

            PopupWindow popup;

            @Override
            public void onClick(View v) {
                //Will open up a popup soon
                if (g.getNumberSelectedIngredients(position) > 0) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.delete_popup, null, false);
                    //Creates new popup window
                    popup = new PopupWindow(popupView, 400, 440, true);
                    popup.showAtLocation(v, Gravity.CENTER, 0, 0);

                    //Onclick listener for yes button
                    Button yes = (Button) popupView.findViewById(R.id.yesButton);
                    yes.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View popupView) {
                            g.deleteSelectedIngredients(position);
                            adapter.updateStringList(g.getIngredientStringList(position));
                            adapter.notifyDataSetChanged();
                            popup.dismiss();
                            g.writeToFile();
                        }
                    });

                    //Onclick listener for no button
                    Button no = (Button) popupView.findViewById(R.id.noButton);
                    no.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View popupView) {
                            popup.dismiss();
                        }
                    });
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_customize_sub_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
