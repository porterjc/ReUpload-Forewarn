package com.forewarn.forewarn;

import android.graphics.drawable.Drawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.LinkedList;

public class ResultsActivity extends AppCompatActivity {

    Global g;
    TextView found;
    TextView all;
    ImageView indicator;
    ProcessingActivity ocr;
    boolean acceptableItem;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private RelativeLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private ImageButton drawerButton;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private Toolbar navBar;

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

        found.setText("Forewarn Found: ");
        parseStringForListItems();

        if(!acceptableItem)
            indicator.setImageResource(R.drawable.deny);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        mDrawer = (RelativeLayout) findViewById(R.id.left_drawer);
        drawerButton = (ImageButton) findViewById(R.id.hamburger);
        found = (TextView) findViewById(R.id.textView2);
        all = (TextView) findViewById(R.id.textView3);
        indicator = (ImageView) findViewById(R.id.imageView);
        g = (Global) getApplication();

        all.append(getIntent().getStringExtra("temp"));

        acceptableItem = true;

        parseStringForListItems();

        if(!acceptableItem)
            indicator.setImageResource(R.drawable.deny);

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

    public void parseStringForListItems() {
        for(int i = 0; i < g.getNameList().size(); i++) {
            if(g.getNameStringList().get(i).getSelected()){
                for(int k = 0; k < g.getIngredientStringList(i).size(); k++){
                    if(all.getText().toString().contains(g.getIngredientStringList(i).get(k).getName()) && !found.getText().toString().contains(g.getIngredientStringList(i).get(k).getName())){
                        //Log.d("FOREWARN", "Does it have it?" + g.getIngredientStringList(i).get(k).getName());
                        if(k==0)
                            found.append(g.getIngredientStringList(i).get(k).getName());
                        else
                            found.append(", " + g.getIngredientStringList(i).get(k).getName());
                        acceptableItem = false;
                    }
                }
            }
        }
    }
}
