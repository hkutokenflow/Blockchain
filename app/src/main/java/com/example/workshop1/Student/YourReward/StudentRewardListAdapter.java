package com.example.workshop1.Student.YourReward;

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

public class StudentRewardListAdapter extends ArrayAdapter<StudentRewardItem> {

    private Context context;
    private List<StudentRewardItem> studentRewardList;

    public StudentRewardListAdapter(@NonNull Context context, List<StudentRewardItem> list) {
        super(context, 0, list);
        this.context = context;
        this.studentRewardList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.student_reward_list_item, parent, false);
        }

        StudentRewardItem currentStudentReward = studentRewardList.get(position);

        TextView nameView = convertView.findViewById(R.id.tv_voucher_name);
        Button useButton = convertView.findViewById(R.id.btn_use);

        nameView.setText(currentStudentReward.voucher);


        //-------------------------------DELETE------------------------------------
        useButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Use Rewards")
                    .setMessage("Are you sure you want to use the \"" + currentStudentReward.voucher + "\"?")
                    .setPositiveButton("Use", (dialog, which) -> {
                        studentRewardList.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Reward used", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });


        return convertView;
    }


}
