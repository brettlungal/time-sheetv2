package com.example.mytimesheet;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class WorkList extends ArrayAdapter<LoggedWork> {

    private Activity context;
    private List<LoggedWork> work;

    public WorkList(Activity context , List<LoggedWork> work){
        super(context, R.layout.list_layout,work);
        this.context = context;
        this.work = work;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout , null , true);
        TextView theDate = (TextView)listViewItem.findViewById(R.id.dateOfWork);
        TextView theHours = (TextView)listViewItem.findViewById(R.id.loggedHour);

        LoggedWork log = work.get(position);
        theDate.setText(log.getDate());
        theHours.setText(log.getHours()+"");

        return listViewItem;
    }
}
