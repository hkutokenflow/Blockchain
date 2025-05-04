package com.example.workshop1.Student.EventCheckin;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.workshop1.R;
import com.example.workshop1.SQLite.Mysqliteopenhelper;

import java.util.List;

public class StudentEventListAdapter extends ArrayAdapter<EventItem> {

    private Context context;
    private List<EventItem> eventList;
    private Mysqliteopenhelper mysqliteopenhelper;

    public StudentEventListAdapter(@NonNull Context context, List<EventItem> list) {
        super(context, 0, list);
        this.context = context;
        this.eventList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.student_event_list_item, parent, false);
        }

        EventItem currentEvent = eventList.get(position);

        TextView nameView = convertView.findViewById(R.id.tv_event_name);
        TextView tokenView = convertView.findViewById(R.id.tv_token_count);
        ImageButton viewButton = convertView.findViewById(R.id.btn_view);

        mysqliteopenhelper = new Mysqliteopenhelper(context);

        nameView.setText(currentEvent.name);
        tokenView.setText(currentEvent.tokens + " tokens");


        //-------------------------------VIEW-------------------------------------
        viewButton.setOnClickListener(v -> {
            String description = currentEvent.getName();
            int reward = currentEvent.getTokens();

            int eventId = mysqliteopenhelper.getEventId(description, reward);

            // Create and show a dialog to display the username and password
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(description);
            builder.setMessage("Event ID: " + eventId);
            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            builder.show();
        });


        return convertView;
    }


}
