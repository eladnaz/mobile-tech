package com.lab.mydietary;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements AddFragment.OnFragmentInteractionListener,ListFragment.OnFragmentInteractionListener{
    private final int ADD_FRAGMENT = 9831;
    private final int LIST_FRAGMENT = 3642;
    ListFragment list_screen;
    AddFragment add_screen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list_screen = ListFragment.newInstance();
        add_screen = AddFragment.newInstance();
        getSupportFragmentManager().beginTransaction().attach(add_screen).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,list_screen).commit();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.action_add)
            switchFragment(ADD_FRAGMENT);
        else if(id == R.id.action_list)
            switchFragment(LIST_FRAGMENT);
        return super.onOptionsItemSelected(item);

    }

    public void switchFragment(int id)
    {
        if(id == LIST_FRAGMENT)
        {
            if(list_screen != null && !list_screen.isVisible())
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,list_screen).commit();
            else
                Toast.makeText(this,"You're already on the list page",Toast.LENGTH_SHORT).show();
        }
        else if(id == ADD_FRAGMENT)
        {
            if(add_screen !=null && !add_screen.isVisible())
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,add_screen).commit();
            else
                Toast.makeText(this,"You're already on the adding page",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed()
    {

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
