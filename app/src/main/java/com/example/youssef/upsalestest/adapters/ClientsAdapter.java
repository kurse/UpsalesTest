package com.example.youssef.upsalestest.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.youssef.upsalestest.R;
import com.example.youssef.upsalestest.fragments.ClientInfoFragment;
import com.example.youssef.upsalestest.fragments.ClientsListFragment;
import com.example.youssef.upsalestest.models.Client;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Youssef on 12/21/2016.
 *
 * The main Adapter showing Clients
 *
 */

public class ClientsAdapter extends BaseAdapter {

    // Temporary filtered Clients list
    ArrayList<Client> mFilteredClients;
    // Full Clients Array
    ArrayList<Client> mClients;
    // Indicator about filtering
    boolean isFiltered = false;

    private Activity context;
    private static LayoutInflater inflater;

    // Our list Filter (by name)
    private ValueFilter valueFilter;

    // Clears the list
    public void clear(){
        mClients.clear();
    }

    // Resetting the filter
    public void resetFilter(){
        isFiltered = false;
        notifyDataSetChanged();
    }
    // The main constructor
    public ClientsAdapter(Activity context){
        mClients = new ArrayList<>();
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    // Getting the elements number (if filtered or not)
    @Override
    public int getCount() {
        if(!isFiltered)
            return mClients.size();
        else
            return mFilteredClients.size();
    }
    // Getting the main Filter
    public Filter getFilter() {
        if(valueFilter==null) {
            valueFilter=new ValueFilter();
        }
        return valueFilter;
    }
    @Override
    public Object getItem(int i) {
        if(!isFiltered)
            return mClients.get(i);
        else
            return mFilteredClients.get(i);
    }

    private void showError(){
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder errorDialog = new AlertDialog.Builder(context);
                errorDialog.setTitle("Error");
                errorDialog.setMessage("This client doesn't have an address");
                errorDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                errorDialog.show();
            }
        });


    }
    @Override
    public long getItemId(int i) {
        return i;
    }

    // The main view for each Client. We return the view according to the filtering
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if(view == null)
            view = inflater.inflate(R.layout.client_item, null);
        final TextView client = (TextView)view.findViewById(R.id.client_item);
        final Client curClient = mClients.get(i);

        // Handling the name Filter
        if(!isFiltered)
            client.setText(curClient.getName());
        else
            client.setText(mFilteredClients.get(i).getName());

        // Handling the Click on an item to open the Map Fragment
        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curClient.getAddress()!=null) {

                    FragmentManager fm = context.getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ClientInfoFragment fragment = new ClientInfoFragment();
                    Bundle params = new Bundle();
                    params.putString("name", client.getText().toString());
                    params.putString("address", curClient.getAddress().toString());
                    fragment.setArguments(params);
                    ft.replace(R.id.fragment_holder, fragment)
                            .addToBackStack("clientInfo")
                            .commit();
                    fm.beginTransaction();
                }else{
                    showError();
                }
            }
        });

        return client;
    }

    public void addItem(Client client){
        mClients.add(client);
        notifyDataSetChanged();
    }

    /*
     * Our main Filter Class to filter by name
     */
    private class ValueFilter extends Filter {


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            mFilteredClients = new ArrayList<>();
            isFiltered = true;

            FilterResults results=new FilterResults();
            if(constraint!=null && constraint.length()>0){
                ArrayList<Client> filterList=new ArrayList<>();
                for(int i=0;i<mClients.size();i++){
                    if((mClients.get(i).getName().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {
                        Client client = new Client(mClients.get(i).getName());
                        filterList.add(client);
                    }
                }
                results.count=filterList.size();
                results.values=filterList;
            }else{
                results.count=mClients.size();
                results.values=mClients;
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            mFilteredClients=(ArrayList<Client>) results.values;
            notifyDataSetChanged();
        }
    }

}
