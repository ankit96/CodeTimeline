package xyz.hxworld.codetimeline;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerCallbacks, SearchView.OnQueryTextListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    private int drawerPosition;

    private String jURL = "http://hxworld.xyz/json/hackerrank.php";
    private ArrayList<EventModel> events = new ArrayList<>();
    private ArrayList<EventModel> eventsList = new ArrayList<>();

    SQLiteDBHandler sqLiteDBHandler;

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

        sqLiteDBHandler = new SQLiteDBHandler(this, MainActivity.this);

        recyclerView = (RecyclerView) findViewById(R.id.mainList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerViewAdapter = new RecyclerViewAdapter(events);
//        recyclerView.setAdapter(recyclerViewAdapter);

        if(isOnline()) {
            sqLiteDBHandler.getHackerrankEvents(drawerPosition);
//            sqLiteDBHandler.getAllEvents(drawerPosition);
        } else {
            sqLiteDBHandler.getAllEvents(drawerPosition);
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        drawerPosition = position;
    }


    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_main, menu);

            final MenuItem menuItem = menu.findItem(R.id.action_search);
            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
            searchView.setOnQueryTextListener(this);

            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void setEventsArrayList(ArrayList<EventModel> eventModelArrayList) {
        events = eventModelArrayList;
        eventsList = eventModelArrayList;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        eventsList = new ArrayList<>(events);

        ArrayList<EventModel> filteredEventList = filter(eventsList, newText);
        if(newText.equals("")){
            filteredEventList = events;
        }
        recyclerViewAdapter.animateTo(filteredEventList);
        recyclerView.scrollToPosition(0);
        return true;
    }

    private ArrayList<EventModel> filter(ArrayList<EventModel> models, String query) {
        query = query.toLowerCase();

        final ArrayList<EventModel> filteredModelList = new ArrayList<>();
        for (EventModel model : models) {
            final String text = model.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}