package com.example.workshop1.Admin.Vendor;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.workshop1.R;

import java.util.ArrayList;
import java.util.List;

public class ManageVendorFragment extends Fragment {

    private ListView vendorListView;
    private Button addButton;
    private List<VendorItem> vendorList;
    private VendorListAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_vendor, container, false);

        vendorListView = view.findViewById(R.id.vendor_list_view);
        addButton = view.findViewById(R.id.btn_add);

        // 测试用假数据
        vendorList = new ArrayList<>();
        vendorList.add(new VendorItem("Vendor A"));
        vendorList.add(new VendorItem("Vendor B"));
        vendorList.add(new VendorItem("Vendor C"));

        adapter = new VendorListAdapter(getContext(), vendorList);
        vendorListView.setAdapter(adapter);

        // 添加新的 Vendor
        addButton.setOnClickListener(v -> showAddVendorDialog());

        return view;
    }

    private void showAddVendorDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_vendor, null);
        EditText nameInput = dialogView.findViewById(R.id.et_vendor_name);
        Button confirmButton = dialogView.findViewById(R.id.btn_confirm);
        Button cancelButton = dialogView.findViewById(R.id.btn_cancel);

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .setCancelable(false)
                .create();

        confirmButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in the name", Toast.LENGTH_SHORT).show();
                return;
            }

            vendorList.add(new VendorItem(name));
            adapter.notifyDataSetChanged();
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
