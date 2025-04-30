package com.example.workshop1.Admin.Event;

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

import java.util.List;

public class EventListAdapter extends ArrayAdapter<EventItem> {

    private Context context;
    private List<EventItem> eventList;

    public EventListAdapter(@NonNull Context context, List<EventItem> list) {
        super(context, 0, list);
        this.context = context;
        this.eventList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.event_list_item, parent, false);
        }

        EventItem currentEvent = eventList.get(position);

        TextView nameView = convertView.findViewById(R.id.tv_event_name);
        TextView tokenView = convertView.findViewById(R.id.tv_token_count);
        ImageButton editButton = convertView.findViewById(R.id.btn_edit);
        ImageButton deleteButton = convertView.findViewById(R.id.btn_delete);

        nameView.setText(currentEvent.name);
        tokenView.setText(currentEvent.tokens + " tokens");

        //-------------------------------EDIT-------------------------------------
        editButton.setOnClickListener(v -> {
            showEditDialog(currentEvent, position);
        });

        //-------------------------------DELETE------------------------------------
        deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Event")
                    .setMessage("Are you sure you want to delete \"" + currentEvent.name + "\"?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        eventList.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Event deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });


        return convertView;
    }


    private void showEditDialog(EventItem event, int position) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_event, null);
        EditText etName = dialogView.findViewById(R.id.et_event_name);
        EditText etTokens = dialogView.findViewById(R.id.et_token_count);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm);

        // 设置原有值
        etName.setText(event.name);
        etTokens.setText(String.valueOf(event.tokens));

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            String newName = etName.getText().toString().trim();
            String tokenStr = etTokens.getText().toString().trim();

            if (newName.isEmpty() || tokenStr.isEmpty()) {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int newTokens = Integer.parseInt(tokenStr);

            // 更新 event 数据
            event.name = newName;
            event.tokens = newTokens;

            notifyDataSetChanged();  // 通知刷新
            dialog.dismiss();
        });

        dialog.show();
    }



}
