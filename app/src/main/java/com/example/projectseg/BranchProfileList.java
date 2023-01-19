package com.example.projectseg;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BranchProfileList extends ArrayAdapter<BranchProfile> {
    private Activity context;
    List<BranchProfile> profiles;

    public BranchProfileList(Activity context, List<BranchProfile> profiles) {
        super(context, R.layout.layout_branch_list, profiles);
        this.context = context;
        this.profiles = profiles;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_branch_list, null, true);

        TextView textViewCity = (TextView) listViewItem.findViewById(R.id.textViewCityL);
        TextView textViewAddress = (TextView) listViewItem.findViewById(R.id.textViewAddressL);
        TextView textViewPhone = (TextView) listViewItem.findViewById(R.id.textViewPhoneL);
        TextView textViewOpening = (TextView) listViewItem.findViewById(R.id.textViewOpeningL);
        TextView textViewClosing = (TextView) listViewItem.findViewById(R.id.textViewClosingL);
        TextView textViewServices = (TextView) listViewItem.findViewById(R.id.textViewServicesL);

        BranchProfile profile = profiles.get(position);
        textViewCity.setText(profile.getBranchCity());
        textViewAddress.setText(profile.getBranchAddress());
        textViewPhone.setText(profile.getBranchPhone());
        textViewOpening.setText("Opens at: "+profile.getBranchOpen()+" am");
        textViewClosing.setText("Closes at: "+profile.getBranchClose()+ " pm");
        if(profile.getBranchServices()!=null) {
            ArrayList<String> ar = new ArrayList<>();
            for(int i=0; i<profile.getBranchServices().size();i++){
                ar.add(profile.getBranchServices().get(i).getServiceName());
            }
            textViewServices.setText("Services offered: "+String.valueOf(ar));
        }else{
            textViewServices.setText("No Services by this branch yet");
        }
        return listViewItem;
    }
}