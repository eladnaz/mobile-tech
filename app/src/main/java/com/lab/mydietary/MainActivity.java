package com.lab.mydietary;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;


public class MainActivity extends AppCompatActivity implements AddFragment.OnFragmentInteractionListener,ListFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ListFragment add_screen = ListFragment.newInstance();
        AddFragment add_screen = AddFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,add_screen).commit();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed()
    {

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
