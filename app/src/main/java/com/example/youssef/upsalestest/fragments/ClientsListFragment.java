package com.example.youssef.upsalestest.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.youssef.upsalestest.R;
import com.example.youssef.upsalestest.activities.MainActivity;
import com.example.youssef.upsalestest.adapters.ClientsAdapter;
import com.example.youssef.upsalestest.adapters.ManagersAdapter;
import com.example.youssef.upsalestest.models.Client;
import com.example.youssef.upsalestest.presenters.ListPresenter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Youssef on 12/21/2016.
 */

public class ClientsListFragment extends Fragment{

    // Main layout with refreshing enabled
    @BindView(R.id.objects_list_layout)SwipeRefreshLayout swipeRefreshLayout;
    // Main clients list
    @BindView(R.id.clients_list)ListView mClientsList;
    // Managers list adapter for the drawer
    ManagersAdapter managersAdapter;
    // An indicator whether or not the current list is filtered
    boolean isFiltered;
    // The main presenter who "pilots" the fragment
    ListPresenter mClientsPresenter;
    // The full clients list
    ClientsAdapter mClientsListAdapter;
    // The temporary clients list filtered by Managers
    ClientsAdapter mClientsManagerAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    // Main initialisation
    private void init(){
        TextView title = (TextView)getActivity().findViewById(R.id.title);
        title.setText(getString(R.string.app_name));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(isFiltered)
                    mClientsPresenter.fetchManagersFilteredList(managersAdapter.getCheckedIds());
                else
                    mClientsPresenter.fetchClientsList();
            }
        });
        if(mClientsPresenter == null) {
            swipeRefreshLayout.setRefreshing(true);
            mClientsPresenter = new ListPresenter();
            mClientsPresenter.takeFragment(this);
        }
        if(mClientsListAdapter == null) {
            mClientsListAdapter = new ClientsAdapter(getActivity());
            mClientsManagerAdapter = new ClientsAdapter(getActivity());
        }
        mClientsList.setAdapter(mClientsListAdapter);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_clients_list, container, false);
    }

    // Setting the right drawer to filter managers
    public void setupManagersDrawer(){
        managersAdapter  = new ManagersAdapter(getActivity());
        managersAdapter.setManagers(mClientsPresenter.getManagers());
        ((MainActivity)getActivity()).getmManagersList().setAdapter(managersAdapter);

        ((MainActivity)getActivity()).getDrawer().addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // If there are any managers checked, we fetch the filtered list
                if(managersAdapter.getCheckedIds().size()>0){
                    swipeRefreshLayout.setRefreshing(true);
                    mClientsPresenter.fetchManagersFilteredList(managersAdapter.getCheckedIds());
                }else // Or we reset the Clients list to its unfiltered state
                    mClientsList.setAdapter(mClientsListAdapter);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        init();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        //Handling the to search bar to filter Clients
        inflater.inflate(R.menu.menu_search,menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mClientsListAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        //The following two listeners are used to handle the title not being displayed properly with the search bar
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView title = (TextView)getActivity().findViewById(R.id.title);
                title.setVisibility(View.GONE);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mClientsListAdapter.resetFilter();
                TextView title = (TextView)getActivity().findViewById(R.id.title);
                title.setVisibility(View.VISIBLE);
                return false;
            }
        });

        //The following two lines are hiding the back button after showing a Client on the map
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);

        super.onCreateOptionsMenu(menu, inflater);

    }


    //Handling the Presenter instruction to show the list
    public void onNext(ArrayList<Client> clientsList, boolean filtered){
        swipeRefreshLayout.setRefreshing(false);
        isFiltered = filtered;
        // Handling the case where the list is filtered by managers or not by switching adapters
        if(!filtered) {
            mClientsListAdapter.clear();
            for (int i = 0; i < clientsList.size(); i++) {
                mClientsListAdapter.addItem(clientsList.get(i));
            }
            // We refresh the list and setup the new Managers for the list
            mClientsList.setAdapter(mClientsListAdapter);
            setupManagersDrawer();
        }else{
            mClientsManagerAdapter.clear();
            for (int i = 0; i < clientsList.size(); i++) {
                mClientsManagerAdapter.addItem(clientsList.get(i));
            }
            mClientsList.setAdapter(mClientsManagerAdapter);
        }
    }

    // Showing the errors sent by the presenter
    public void onError(String error){
        swipeRefreshLayout.setRefreshing(false);
        AlertDialog.Builder errorDialog = new AlertDialog.Builder(getActivity());
        errorDialog.setTitle("Error");
        errorDialog.setMessage(error);
        errorDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        errorDialog.show();

    }

    @Override
    public void onDestroy() {
        getActivity().finish();
        super.onDestroy();
    }

}
