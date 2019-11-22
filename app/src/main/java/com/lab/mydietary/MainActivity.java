package com.lab.mydietary;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements AddFragment.OnFragmentInteractionListener,ListFragment.OnFragmentInteractionListener{
    private final int ADD_FRAGMENT = 9831;
    private final int LIST_FRAGMENT = 3642;
    static final String FIRST_TIME = "first";
    ListFragment list_screen;
    AddFragment add_screen;
    SharedPreferences.Editor edit;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list_screen = ListFragment.newInstance();
        add_screen = AddFragment.newInstance(null);
        getSupportFragmentManager().beginTransaction().attach(add_screen).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,list_screen).commit();
        pref = getSharedPreferences(SplashActivity.SETTING_CODE,MODE_PRIVATE);
        edit = pref.edit();
        boolean isFirst = pref.getBoolean(FIRST_TIME,false);
        if(!isFirst)
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Notice");
            alert.setMessage("Splash screen on startup can be disabled in the settings dialog found in the menu above");
            alert.setPositiveButton("OK",null);
            alert.show();
            edit.putBoolean(FIRST_TIME,true);
            edit.commit();
        }

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
        else if(id == R.id.action_setting)
        {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_settings);
            dialog.setTitle("Settings");
            CheckBox set_splash = dialog.findViewById(R.id.setting_check);
            boolean have_splash =pref.getBoolean(SplashActivity.HAVE_SPLASH,false);
            if(have_splash)
                set_splash.setChecked(true);
            else
                set_splash.setChecked(false);
            ImageButton confirm_btn = dialog.findViewById(R.id.setting_confirm);
            ImageButton cancel_btn = dialog.findViewById(R.id.setting_cancel);
            confirm_btn.setOnClickListener(v -> {
                boolean new_splash = set_splash.isChecked();
                edit.putBoolean(SplashActivity.HAVE_SPLASH,new_splash);
                edit.commit();
                dialog.dismiss();
            });
            cancel_btn.setOnClickListener(v -> dialog.dismiss());
            dialog.show();
        }
        return super.onOptionsItemSelected(item);

    }

    public void switchFragment(int id)
    {
        if(id == LIST_FRAGMENT)
        {
            if(list_screen != null && !list_screen.isVisible())
            {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in,R.anim.slide_out);
                ft.replace(R.id.fragment_container,list_screen).commit();
            }
            else
                Toast.makeText(this,"You're already on the Home page",Toast.LENGTH_SHORT).show();
        }
        else if(id == ADD_FRAGMENT)
        {
            if(add_screen !=null && !add_screen.isVisible())
            {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in,R.anim.slide_out);
                ft.replace(R.id.fragment_container,add_screen).commit();
            }

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
