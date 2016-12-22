package com.example.youssef.upsalestest.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.example.youssef.upsalestest.R;
import com.example.youssef.upsalestest.fragments.ClientsListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    // The right Drawer for filtering by Managers
    @BindView(R.id.drawer)DrawerLayout drawer;
    // The main managers list
    @BindView(R.id.managers_list) ListView mManagersList;
    // The top toolbar
    @BindView(R.id.toolbar) Toolbar toolbar;
    // The close button on the right Drawer
    @BindView(R.id.close) TextView close;

    public DrawerLayout getDrawer() {
        return drawer;
    }


    public ListView getmManagersList() {
        return mManagersList;
    }

    //  Main initializing
    public void init(){
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(Gravity.RIGHT);
            }
        });

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ClientsListFragment fragment = new ClientsListFragment();
        ft.replace(R.id.fragment_holder,fragment)
                .addToBackStack("clientsList")
                .commit();
        fm.beginTransaction();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_main);

        init();
    }

    // Handling clicks on the toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getFragmentManager().popBackStack("clientsList",0);
        }else if(item.getTitle().equals("Filter")){
            DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer);
            dl.openDrawer(Gravity.RIGHT);
        }
        return super.onOptionsItemSelected(item);
    }
}
