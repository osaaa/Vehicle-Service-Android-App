package com.example.projectseg;

import java.util.ArrayList;

public class Service {
    private String _id;
    private String _serviceName;
    private double _price;
    private ArrayList<String> _info;

    public Service() {
    }
    public Service(String id, String serviceName, double price, ArrayList<String> info ) {
        _id = id;
        _serviceName = serviceName;
        _price = price;
        _info = info;
    }

    public void setId(String id) {
        _id = id;
    }
    public String getId() {
        return _id;
    }
    public void setInfo(ArrayList<String> info) {
        _info = info;
    }
    public ArrayList<String> getInfo() {
        return _info;
    }
    public void setServiceName(String serviceName) {
        _serviceName = serviceName;
    }
    public String getServiceName() {
        return _serviceName;
    }
    public void setPrice(double price) {
        _price = price;
    }
    public double getPrice() {
        return _price;
    }
}
