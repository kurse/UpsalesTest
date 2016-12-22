package com.example.youssef.upsalestest.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;

import com.example.youssef.upsalestest.R;
import com.example.youssef.upsalestest.models.Manager;

import java.util.ArrayList;

/**
 * Created by Youssef on 12/22/2016.
 *
 * The main Adapter for the Managers list on the right Drawer
 *
 */

public class ManagersAdapter extends BaseAdapter {

    // The Managers List
    ArrayList<Manager> managers;
    // A list keeping track of the Managers Checked and storing their ids
    ArrayList<String> checkedIds;
    Context context;

    // The main Constructor
    public ManagersAdapter(Context context){
        this.context = context;
        managers = new ArrayList<>();
        checkedIds = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return managers.size();
    }

    @Override
    public Object getItem(int i) {
        return managers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    // Getting the checked Managers IDs
    public ArrayList<String> getCheckedIds(){
        return checkedIds;
    }

    public void add(Manager manager){
        managers.add(manager);
    }

    public void setManagers(ArrayList<Manager> managers){
        this.managers = managers;
        notifyDataSetChanged();
    }
    // Getting each Manager View and handling the checkbox
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = view;
        if(v == null){
            v = ((Activity)context).getLayoutInflater().inflate(R.layout.manager_item,viewGroup,false);
        }
        AppCompatCheckBox curManager = (AppCompatCheckBox) v.findViewById(R.id.manager_view);
        curManager.setText(managers.get(i).getName());
        // Adding/Removing the Managers IDs when they are checked/unchecked
        curManager.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    checkedIds.add(managers.get(i).getId());
                else
                    checkedIds.remove(managers.get(i).getId());
            }
        });

        return v;
    }
}
