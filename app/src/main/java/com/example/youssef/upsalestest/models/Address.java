package com.example.youssef.upsalestest.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Youssef on 12/21/2016.
 *
 * The address for each client annotated for parsing with the GSON library
 */

public class Address {
    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("zipcode")
    @Expose
    private String zipcode;

    @SerializedName("city")
    @Expose
    private String city;

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        if(address!=null) {
            sb.append(address);
        }
        sb.append(" ");
        if(zipcode!=null) {
            sb.append(zipcode);
        }
        sb.append(" ");

        if(city!=null) {
            sb.append(city);
        }
        return sb.toString();
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
            this.address = address;


    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
