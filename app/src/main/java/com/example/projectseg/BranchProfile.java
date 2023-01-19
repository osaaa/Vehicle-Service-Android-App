package com.example.projectseg;

import java.util.ArrayList;

public class BranchProfile {
    private String _id;
    private String _branchCity;
    private String _branchAddress;
    private String _branchPhone;
    private ArrayList<Service> _branchServices;
    private String  _branchOpen;
    private String _branchClose;

    public BranchProfile() {
    }
    public BranchProfile(String id, String branchCity, String branchAddress, String branchPhone, String branchOpen, String branchClose, ArrayList<Service> branchServices ) {
        _id = id;
        _branchCity = branchCity;
        _branchAddress = branchAddress;
        _branchPhone = branchPhone;
        _branchServices = branchServices;
        _branchOpen = branchOpen;
        _branchClose = branchClose;
    }

    public void setId(String id) {
        _id = id;
    }
    public String getId() {
        return _id;
    }

    public void setBranchServices(ArrayList<Service> branchServices) { _branchServices = branchServices; }
    public ArrayList<Service> getBranchServices() {
        return _branchServices;
    }

    public void setBranchCity(String branchCity){ _branchCity = branchCity; }
    public String getBranchCity(){return _branchCity;}

    public void setBranchAddress(String branchAddress) {
        _branchAddress = branchAddress;
    }
    public String getBranchAddress() {
        return _branchAddress;
    }


    public void setBranchPhone(String phone) {
        _branchPhone = phone;
    }
    public String getBranchPhone() {
        return _branchPhone;
    }

    public void setBranchOpen(String open) {
        _branchOpen = open;
    }
    public String getBranchOpen() {
        return _branchOpen;
    }


    public void setBranchClose(String close) {
        _branchClose = close;
    }
    public String getBranchClose() {
        return _branchClose;
    }


}
