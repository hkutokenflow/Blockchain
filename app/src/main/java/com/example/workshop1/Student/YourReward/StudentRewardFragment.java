package com.example.workshop1.Student.YourReward;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.workshop1.R;

import java.util.ArrayList;
import java.util.List;


public class StudentRewardFragment extends Fragment {

    private ListView rewardListView;
    private List<StudentRewardItem> rewardList;
    private StudentRewardListAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_student_reward, container, false);

        rewardListView = view.findViewById(R.id.student_reward_list_view);

        // ----------------------------SQLite-----------------------------------
        // -----------------------！！！！！！这里接数据库！！！！！！---------------------
        // ----------------------------Adapter也需要根据后端进行调整-----------------------------------
        // ###################注意：###################
        // 增删改查，这里只有增，删和改在adapter


        // 测试用假数据
        rewardList = new ArrayList<>();
        rewardList.add(new StudentRewardItem("Voucher 1"));
        rewardList.add(new StudentRewardItem("Voucher 1"));
        rewardList.add(new StudentRewardItem("Voucher 1"));
        rewardList.add(new StudentRewardItem("Voucher 1"));
        rewardList.add(new StudentRewardItem("Voucher 1"));

        //-------------------DELETE(use)在这边----------------------
        adapter = new StudentRewardListAdapter(getContext(), rewardList);
        rewardListView.setAdapter(adapter);


        return view;
    }



}


