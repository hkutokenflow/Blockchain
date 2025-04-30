package com.example.workshop1.Admin.Vendor;


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

public class VendorListAdapter extends ArrayAdapter<VendorItem> {

    private Context context;
    private List<VendorItem> vendorList;

    public VendorListAdapter(@NonNull Context context, List<VendorItem> list) {
        super(context, 0, list);
        this.context = context;
        this.vendorList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.vendor_list_item, parent, false);
        }

        VendorItem currentVendor = vendorList.get(position);

        TextView nameView = convertView.findViewById(R.id.tv_vendor_name);
        ImageButton editButton = convertView.findViewById(R.id.btn_edit);
        ImageButton deleteButton = convertView.findViewById(R.id.btn_delete);

        nameView.setText(currentVendor.name);

        //-------------------------------EDIT-------------------------------------
        editButton.setOnClickListener(v -> {
            showEditDialog(currentVendor, position);
        });

        //-------------------------------DELETE------------------------------------
        deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Vendor")
                    .setMessage("Are you sure you want to delete \"" + currentVendor.name + "\"?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        vendorList.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Vendor deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        return convertView;
    }

    private void showEditDialog(VendorItem vendor, int position) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_vendor, null);
        EditText etName = dialogView.findViewById(R.id.et_vendor_name);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm);

        // 设置原有值
        etName.setText(vendor.name);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            String newName = etName.getText().toString().trim();

            if (newName.isEmpty()) {
                Toast.makeText(context, "Please fill in the name", Toast.LENGTH_SHORT).show();
                return;
            }

            // 更新 vendor 数据
            vendor.name = newName;

            notifyDataSetChanged();  // 通知刷新
            dialog.dismiss();
        });

        dialog.show();
    }
}
