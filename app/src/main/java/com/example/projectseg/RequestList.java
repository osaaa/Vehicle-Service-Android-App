package com.example.projectseg;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RequestList extends ArrayAdapter<Request> {
    private Activity context;
    List<Request> requests;

    public RequestList(Activity context, List<Request> requests) {
        super(context, R.layout.layout_request_list, requests);
        this.context = context;
        this.requests = requests;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_request_list, null, true);

        TextView textViewNameService = (TextView) listViewItem.findViewById(R.id.textViewRequestServiceNameK);
        TextView textViewStatus = (TextView) listViewItem.findViewById(R.id.textViewRequestStatusK);
        TextView textViewRequestInfo = (TextView) listViewItem.findViewById(R.id.textViewRequestInfoK);

        Request request = requests.get(position);
        textViewNameService.setText(request.getServiceName());
        if(request.getApproved()==false){
            textViewStatus.setText("Status: Not approved");
        }else{
            textViewStatus.setText("Status: Approved");
        }
        if(request.getInfo()!=null) {
            textViewRequestInfo.setText("Customer info: "+String.valueOf(request.getInfo()));
        }else{
            textViewRequestInfo.setText("No info in this request");
        }
        return listViewItem;
    }
}