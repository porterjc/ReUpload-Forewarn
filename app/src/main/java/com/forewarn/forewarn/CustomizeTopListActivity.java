package com.forewarn.forewarn;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.util.Log;
import android.widget.Toast;

import java.util.LinkedList;

public class CustomizeTopListActivity extends AppCompatActivity {

    ListView listView;
    EditText edit;
    Button deleteButton;
    MyCustomAdapter adapter;
    Global g;

    @Override
    protected void onResume() {
        super.onResume();
        listView = (ListView) findViewById(R.id.listView);
        adapter = new MyCustomAdapter(this, R.layout.list_items, g.getNameStringList());

        /** Setting the adapter to the ListView */
        listView.setAdapter(adapter);
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_top_list);
        g = (Global)getApplication();

        deleteButton = (Button) findViewById(R.id.deleteButton);

        if(g.getNameList() == null) {
            Log.d("FOREWARN", "we're in the initialize");
            g.initialize();
        }else {
            deleteButton.setVisibility(View.VISIBLE);
        }

        edit = (EditText) findViewById(R.id.textItem);
        listView = (ListView) findViewById(R.id.listView);
        adapter = new MyCustomAdapter(this, R.layout.list_items, g.getNameStringList());

        /** Setting the adapter to the ListView */
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), CustomizeSubListActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        /**
         * Sets on long click listener for editing text
         */
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            EditText text;
            int pos;
            PopupWindow popup;

            @Override
            public boolean onItemLongClick(AdapterView<?> av, View view, int position, long id) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.edit_popup, null, false);

                //Create new popup window
                popup = new PopupWindow(popupView, 400, 440, true);
                pos = position;
                popup.showAtLocation(av, Gravity.CENTER, 0, 0);

                text = (EditText) popupView.findViewById(R.id.editText);
                text.setText(g.getName(position).getName());

                //On click listener for save button
                Button save = (Button) popupView.findViewById(R.id.saveButton);
                save.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View popupView) {
                        g.setName(text.getText().toString(), pos);
                        adapter.updateStringList(g.getNameStringList());
                        adapter.notifyDataSetChanged();
                        popup.dismiss();
                        g.writeToFile();
                    }
                });

                //On click listener for cancel button
                Button cancel = (Button) popupView.findViewById(R.id.cancelButton);
                cancel.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View popupView) {
                        popup.dismiss();
                    }
                });
                return true;
            }
        });

        /**
         * Set on key listener for EditText
         * Inserts row if text is not blank
         */
        edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER
                        && event.getAction() == KeyEvent.ACTION_DOWN
                        && !(edit.getText().toString().equals(""))) {
                    StringList list = new StringList(edit.getText().toString(), false);
                    g.addList(list);
                    adapter.updateStringList(g.getNameStringList());
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
         * Delete Button On Click Listener
         * Only deletes when there are selected checkboxes
         */
        deleteButton.setOnClickListener(new View.OnClickListener() {

            PopupWindow popup;

            @Override
            public void onClick(View v) {
                //Checks for selected checkboxes
                if (g.getNumberSelectedNames() > 0) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.delete_popup, null, false);
                    //New Popup window
                    popup = new PopupWindow(popupView, 400, 440, true);
                    popup.showAtLocation(v, Gravity.CENTER, 0, 0);

                    //On click listener for yes button
                    Button yes = (Button) popupView.findViewById(R.id.yesButton);
                    yes.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View popupView) {
                            g.deleteSelectedNames();
                            adapter.updateStringList(g.getNameStringList());
                            adapter.notifyDataSetChanged();
                            popup.dismiss();
                            g.writeToFile();
                        }
                    });

                    //On click listener for no button
                    Button no = (Button) popupView.findViewById(R.id.noButton);
                    no.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View popupView) {
                            popup.dismiss();
                        }
                    });
                }
            }
        });

        Toast.makeText(getApplicationContext(), "To edit an item name, click and hold the item for 2 seconds", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_customize_top_list, menu);
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

    public void navCustomize(View view) {
        Intent intent = new Intent( this, CustomizeSubListActivity.class ); // create the intent
        startActivity( intent ); // start the camera activity
    }
}
