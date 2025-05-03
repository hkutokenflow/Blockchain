package com.example.workshop1.Admin.Vendor;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.SimpleCursorAdapter;
import android.widget.CursorAdapter;

import androidx.fragment.app.Fragment;

import com.example.workshop1.Login.LoginActivity;
import com.example.workshop1.R;
import com.example.workshop1.SQLite.Mysqliteopenhelper;
import com.example.workshop1.SQLite.User;

import java.util.ArrayList;
import java.util.List;

public class ManageVendorFragment extends Fragment {

    private ListView vendorListView;
    private Button addButton;
    private List<VendorItem> vendorList;
    private VendorListAdapter adapter;
    private Mysqliteopenhelper mysqliteopenhelper;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_vendor, container, false);

        vendorListView = view.findViewById(R.id.vendor_list_view);
        addButton = view.findViewById(R.id.btn_add);
        vendorList = new ArrayList<>();

        mysqliteopenhelper = new Mysqliteopenhelper(requireContext());
        Cursor cursor = mysqliteopenhelper.getVendors();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String username = cursor.getString(1);
                String pwd = cursor.getString(2);
                String name = cursor.getString(3);
                vendorList.add(new VendorItem(username, pwd, name));
            }
        }

        adapter = new VendorListAdapter(getContext(), vendorList);
        vendorListView.setAdapter(adapter);

        // 添加新的 Vendor
        addButton.setOnClickListener(v -> showAddVendorDialog());

        return view;
    }

    private String generateUniqueUsername(String name) {
        String nameLower = name.toLowerCase();
        String[] nameParts = nameLower.split(" ");

        // If the vendor name has 1-2 words, generate username without spaces
        if (nameParts.length <= 2) {
            String namept = nameLower.replaceAll("\\s", "");  // Remove all spaces from the name
            int randomNumber = (int) (Math.random() * 10000);
            return namept + randomNumber;
        } else { // If the vendor name has more than 2 words, generate username with initials
            String namept = "";
            for (String part : nameParts) {
                if (!part.isEmpty()) {
                    namept += part.charAt(0);
                }
            }
            int randomNumber = (int) (Math.random() * 10000);
            return namept + randomNumber;
        }
    }

    // generate a random password with 8 characters containing numbers and letters
    private String generateRandomPassword() {
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";

        String combinedChars = upperCaseLetters + lowerCaseLetters + numbers;
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int index = (int) (Math.random() * combinedChars.length());
            password.append(combinedChars.charAt(index));
        }
        return password.toString();
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
            String unniqueUsername = generateUniqueUsername(name);
            String randomPassword = generateRandomPassword();

            User user = new User(unniqueUsername, randomPassword, name, "vendor");
            long res = mysqliteopenhelper.addUser(user);
            if (res != -1) {
                Toast.makeText(requireContext(), "Vendor registered successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Register failed!", Toast.LENGTH_SHORT).show();
            }

            if (name.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in the name", Toast.LENGTH_SHORT).show();
                return;
            }

            vendorList.add(new VendorItem(unniqueUsername, randomPassword, name));
            adapter.notifyDataSetChanged();
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
