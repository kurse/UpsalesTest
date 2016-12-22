package com.example.youssef.upsalestest.presenters;

import android.app.Fragment;
import android.os.Handler;
import android.os.Message;

import com.example.youssef.upsalestest.UpsalesTestApplication;
import com.example.youssef.upsalestest.fragments.ClientsListFragment;
import com.example.youssef.upsalestest.interfaces.ServerApi;
import com.example.youssef.upsalestest.models.Address;
import com.example.youssef.upsalestest.models.Client;
import com.example.youssef.upsalestest.models.Manager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Youssef on 12/21/2016.
 *
 * The main Presenter handling the Clients List Fragment
 *
 */

public class ListPresenter {
    // The Upsales API KEY
    private static final String API_TOKEN = "5631b0d46a94c57bf93afe4c949f68d06ab16e1e8c30571a65939b99b8c93f80";
    // The Clients List Fragment to be handled by the Presenter
    private ClientsListFragment mClientsFragment;
    // The error to be sent to the Fragment
    private Throwable error;
    // The Retrofit Client (Injected with Dagger2)
    @Inject Retrofit retrofit;
    // The Retrofit Server API Interface
    ServerApi serverApi;
    // The main Callback to handle the Server response
    Handler.Callback callback;
    // The main Clients List Array
    ArrayList<Client> mClientsArray;
    // The Managers list
    ArrayList<Manager> managers;
    // The Managers names (used to handle duplicates)
    ArrayList<String> managersNames;

    public ArrayList<Manager> getManagers() {
        return managers;
    }

    // Initializing the response Callback
    private void initCallback(){
        callback = new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                //Handling the errors
                if(msg.getData().getString("action").equals("error"))
                    publish(false);
                // Handling the list
                else if(msg.getData().getString("action").equals("handleList")){
                    handleList(msg.getData().getString("response"));
                    boolean filtered = msg.getData().getBoolean("filtered");
                    publish(filtered);
                }
                return true;
            }
        };
    }
    // The main Constructor (which initializes the Callback in our case)
    public ListPresenter(){
        initCallback();
    }

    // The main function to handle the list from the Response
    public void handleList(String response){
        try {
            // We first get the full Response
            JSONObject jsonResponse = (new JSONObject(response));
            // We get the "Data" Array
            JSONArray clientsArray = new JSONArray(jsonResponse.getString("data"));
            // We initialize our Class member Arrays
            mClientsArray = new ArrayList<>();
            managers = new ArrayList<>();
            managersNames = new ArrayList<>();
            // Initializing the Gson parser
            Gson gson = new Gson();
            // Our limit (Max 100)
            int limit = 100;
            if(clientsArray.length()<100)
                limit = clientsArray.length();
            // We iterate over the Clients array
            for(int i=0;i<limit;i++){
                // We get the current Client data
                JSONObject curClient = new JSONObject(clientsArray.get(i).toString());
                Client client = gson.fromJson(curClient.toString(),Client.class);
                // We eliminate the empty results
                if(client.getName().equals("")){
                    limit++;
                    continue;
                }
                try{
                    // We get the Managers
                    JSONArray curClientManagers = new JSONArray(curClient.getString("users"));
                    for(int j=0;j<curClientManagers.length();j++){
                        Manager curManager = gson.fromJson(curClientManagers.get(j).toString(),Manager.class);
                        String curManagerName = curManager.getName();
                        // Handling the duplicates
                        if(!managersNames.contains(curManagerName)) {
                            managers.add(curManager);
                            managersNames.add(curManagerName);
                        }
                    }
                    // We get the Addresses and parse them with the Gson parser
                    JSONArray curClientAddresses = new JSONArray(curClient.getString("addresses"));
                    Address curVisitAddress = null;
                    for(int j=0;j<curClientAddresses.length();j++){
                        // We prioritize the Visit Address
                        if(curClientAddresses.get(j).toString().contains("Visit")) {
                            curVisitAddress = gson.fromJson(curClientAddresses.get(0).toString(), Address.class);
                            break;
                        }else if(curClientAddresses.get(j).toString().contains("Mail")) {
                            curVisitAddress = gson.fromJson(curClientAddresses.get(0).toString(), Address.class);
                            break;
                        }
                    }
                    // If the addresses are null, we get the postal address
                    if(curVisitAddress == null){
                        JSONObject soliditet = new JSONObject(curClient.getString("soliditet"));
                        JSONObject generalData = new JSONObject(soliditet.getString("generalData"));
                        curVisitAddress.setAddress(generalData.getString("postalAddress"));
                        curVisitAddress.setCity(generalData.getString("townName"));
                        curVisitAddress.setZipcode(generalData.getString("postalCode"));
                    }
                    client.setAddress(curVisitAddress);
                }catch (JSONException e){

                }
                mClientsArray.add(client);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    // We take the fragment, initialise our Server API and fetch the Clients list from the Upsales API
    public void takeFragment(ClientsListFragment clientsFragment){
        if(mClientsFragment == null || mClientsFragment != clientsFragment) {
            mClientsFragment = clientsFragment;
            ((UpsalesTestApplication) mClientsFragment.getActivity().getApplication()).getComponent().inject(this);
            serverApi = retrofit.create(ServerApi.class);
            fetchClientsList();
        }

    }

    // Sending the next action to the Fragment (Error or list showing)
    private void publish(boolean filtered){
        if(error != null)
            mClientsFragment.onError(error.getMessage());
        else{
            mClientsFragment.onNext(mClientsArray, filtered);
        }
    }
    /*
     * Getting the Clients list filtered by the selected Managers using Retrofit2
     * We handle the async server response using RxAndroid Observable and Observer objects
     * on a seperate Thread and trigger the Callback when we get it.
     */
    public void fetchManagersFilteredList(ArrayList<String> ids){
        JSONObject json = new JSONObject();
        try {
            json.put("token", API_TOKEN);
            Map<String,String> params = new HashMap<>();
            params.put("token",API_TOKEN);
            for(int i=0;i<ids.size();i++){
                params.put("user.id",ids.get(i));
            }
            Observable<String> listObservable = serverApi.getClientsList(params);

            Observer listObserver = new Observer() {
                Message msg = new Message();
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    error = e;
                    msg.getData().putString("action","error");
                    callback.handleMessage(msg);
                }

                @Override
                public void onNext(Object o) {
                    error = null;
                    msg.getData().putString("action","handleList");
                    msg.getData().putString("response",o.toString());
                    msg.getData().putBoolean("filtered",true);
                    callback.handleMessage(msg);
                }
            };
            listObservable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(listObserver);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /*
     * Getting the Clients list unfiltered using Retrofit2
     * We handle the async server response using RxAndroid Observable and Observer objects
     * on a seperate Thread and trigger the Callback when we get it.
     */
    public void fetchClientsList(){
        JSONObject json = new JSONObject();
        try {
            json.put("token", API_TOKEN);
            Map<String,String> params = new HashMap<>();
            params.put("token",API_TOKEN);
            Observable<String> listObservable = serverApi.getClientsList(params);

            Observer listObserver = new Observer() {
                Message msg = new Message();
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    error = e;
                    msg.getData().putString("action","error");
                    callback.handleMessage(msg);
                }

                @Override
                public void onNext(Object o) {
                    error = null;
                    msg.getData().putString("action","handleList");
                    msg.getData().putString("response",o.toString());
                    msg.getData().putBoolean("filtered",false);
                    callback.handleMessage(msg);
                }
            };
            listObservable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(listObserver);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
