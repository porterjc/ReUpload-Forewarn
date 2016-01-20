package com.forewarn.forewarn;

import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.support.v7.widget.Toolbar;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private RelativeLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar navBar;
    private Global g;
    private Button helpButton;
    private ImageButton drawerButton;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    @Override
    protected void onResume() {
        super.onResume();

        LinkedList<StringList> l = g.getNameStringList();

        if( l != null ) {
            MyCustomAdapter adapter = new MyCustomAdapter(this, R.layout.list_items, g.getNameStringList());

            mDrawerList.setAdapter(adapter);
        }
        if(mDrawerLayout.isDrawerOpen(Gravity.LEFT))
            mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        mDrawer = (RelativeLayout) findViewById(R.id.left_drawer);
        helpButton = (Button) findViewById(R.id.helpButton);
        drawerButton = (ImageButton) findViewById(R.id.hamburger);

        g = (Global)getApplication();
        g.readFromFile();



 //       mDrawerList.setAdapter(new ArrayAdapter<Button>(this, R.layout.drawer_layout, R.id.custom_button));

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, navBar, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        drawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {

            private PopupWindow popup;
            private TextView helpText;
            private ImageButton closeButton;

            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.help_popup, null, false);
                helpText = (TextView) popupView.findViewById(R.id.textHelp);
                closeButton = (ImageButton) popupView.findViewById(R.id.closeButton);

                //Create new popup window
                popup = new PopupWindow(popupView, 400, 440, true);
                popup.showAtLocation(v, Gravity.CENTER, 0, 0);
                String text = "To add an item to the list, click on the customize button in the navigation" +
                        ", and enter the new item into the list. When finished, click return on the keypad. \n\n " +
                        "To edit an item in a list, go to that list through the navigation drawer and click and hold " +
                        "the item until the popup appears \n\n" +
                        "To delete an item, go to the list containing the list, check the item and hit the delete button" +
                        " at the top right corner of the application\n";
                helpText.setText(text);

                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup.dismiss();
                    }
                });

            }
        });
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawer);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    public void navCamera(View view) {
        Intent intent = new Intent( this, CameraActivity.class ); // create the intent
        startActivity( intent ); // start the camera activity
    }

    public void navCustomize(View view) {
        Intent intent = new Intent( this, CustomizeTopListActivity.class ); // create the intent
        startActivity( intent ); // start the camera activity
    }


}
