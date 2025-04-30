package com.example.workshop1.Vendor.Voucher;

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
import com.example.workshop1.Vendor.Voucher.VoucherItem;
import com.example.workshop1.Vendor.Voucher.VoucherListAdapter;

import java.util.ArrayList;
import java.util.List;


public class EditVouchersFragment extends Fragment {

    private ListView voucherListView;
    private Button addButton;
    private List<VoucherItem> voucherList;
    private VoucherListAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_vouchers, container, false);

        voucherListView = view.findViewById(R.id.voucher_list_view);
        addButton = view.findViewById(R.id.btn_add);

        // ----------------------------SQLite-----------------------------------
        // -----------------------！！！！！！这里接数据库！！！！！！---------------------
        // ----------------------------Adapter也需要根据后端进行调整-----------------------------------
        // ###################注意：###################
        // 增删改查，这里只有增，删和改在adapter


        //-------------------------ADD----------------------------------
        addButton.setOnClickListener(v -> showAddVoucherDialog());
        // 测试用假数据
        voucherList = new ArrayList<>();
        voucherList.add(new VoucherItem("Voucher 1", 20));
        voucherList.add(new VoucherItem("Voucher 12", 35));
        voucherList.add(new VoucherItem("Voucher 13", 35));
        voucherList.add(new VoucherItem("Voucher 14", 35));
        voucherList.add(new VoucherItem("Voucher 15", 35));

        //-------------------EDIT和DELETE都在这边----------------------
        adapter = new VoucherListAdapter(getContext(), voucherList);
        voucherListView.setAdapter(adapter);




        return view;
    }



    private void showAddVoucherDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_voucher, null);
        EditText nameInput = dialogView.findViewById(R.id.et_voucher_name);
        EditText tokenInput = dialogView.findViewById(R.id.et_token_count);
        Button confirmButton = dialogView.findViewById(R.id.btn_confirm);
        Button cancelButton = dialogView.findViewById(R.id.btn_cancel);

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .setCancelable(false)
                .create();

        confirmButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String tokenStr = tokenInput.getText().toString().trim();

            if (name.isEmpty() || tokenStr.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int tokens = Integer.parseInt(tokenStr);
                voucherList.add(new VoucherItem(name, tokens));
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Token amount must be a number", Toast.LENGTH_SHORT).show();
            }
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

}


