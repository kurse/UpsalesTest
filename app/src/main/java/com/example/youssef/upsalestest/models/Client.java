package com.example.youssef.upsalestest.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Youssef on 12/21/2016.
 *
 * The Client model annotated for parsing with the GSON library
 *
 */

public class Client {

    @SerializedName("name")
    @Expose
    String name;

    Address address;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Client(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String toString(){
        return "name: " + name;
    }
}
