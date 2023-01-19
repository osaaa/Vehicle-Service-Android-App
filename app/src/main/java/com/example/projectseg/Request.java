package com.example.projectseg;

import java.util.ArrayList;

public class Request {
    private String _id;
    private String _branchId;
    private String _customerId;
    private String _serviceName;

    private ArrayList<String> _info;
    private Boolean _approved;

    public Request() {
    }
    public Request(String id,String customerId, String branchId, String serviceName, Boolean approved, ArrayList<String> info ) {
        _id = id;
        _customerId = customerId;
        _branchId = branchId;
        _serviceName = serviceName;
       _approved = approved;
        _info = info;
    }

    public void setId(String id) {
        _id = id;
    }
    public String getId() {
        return _id;
    }

    public void setCustomerId(String customerId){ _customerId =customerId; }
    public String getCustomerId(){ return _customerId; }

    public void setBranchId(String branchId) {
        _branchId = branchId;
    }
    public String getBranchId() {
        return _branchId;
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
    public void setApproved(Boolean approved) {
        _approved = approved;
    }
    public Boolean getApproved() {
        return _approved;
    }
}
