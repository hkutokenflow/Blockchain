package com.example.workshop1.Vendor.changePassword;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.workshop1.R;
import com.example.workshop1.SQLite.Mysqliteopenhelper;
import com.example.workshop1.Login.EmailSender;

import java.util.Random;

public class changeVendorPasswordFragment extends Fragment {

    private EditText et_email, et_pwd, et_equal, et_verifyCode;
    private ImageView iv_eye2, iv_eye3;
    private Button btn_sendCode, btn_changePassword;
    private Mysqliteopenhelper mysqliteopenhelper;
    private int Visiable1 = 0, Visiable2 = 0;
    private String sentCode = "nocode";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_vendor_password, container, false);

        et_email = view.findViewById(R.id.et_changepwd_email);
        et_pwd = view.findViewById(R.id.et_change_password);
        et_equal = view.findViewById(R.id.et_equal_password);
        et_verifyCode = view.findViewById(R.id.et_verify_code);
        iv_eye2 = view.findViewById(R.id.iv_eye2);
        iv_eye3 = view.findViewById(R.id.iv_eye3);
        btn_sendCode = view.findViewById(R.id.btn_sendCode);
        btn_changePassword = view.findViewById(R.id.btn_confirm);

        mysqliteopenhelper = new Mysqliteopenhelper(requireContext());

        btn_sendCode.setOnClickListener(v -> sendEmailCode());
        btn_changePassword.setOnClickListener(this::change_password);

        iv_eye2.setOnClickListener(this::Isvisiable2);
        iv_eye3.setOnClickListener(this::Isvisiable3);

        return view;
    }

    private void sendEmailCode() {
        String email = et_email.getText().toString().trim();
        if (!isHKUEmail(email)) {
            Toast.makeText(requireContext(), "Must use HKU email", Toast.LENGTH_SHORT).show();
            return;
        }

        sentCode = generateCode();

        new Thread(() -> {
            try {
                EmailSender.sendEmail(email, "Your Verification Code", "Your code is: " + sentCode);
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Code sent!", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Failed to send email", Toast.LENGTH_SHORT).show());
                e.printStackTrace();
            }
        }).start();
    }

    private String generateCode() {
        Random rand = new Random();
        int code = 100000 + rand.nextInt(900000); // 6-digit code
        return String.valueOf(code);
    }

    private boolean verifyCode() {
        String inputCode = et_verifyCode.getText().toString().trim();
        return inputCode.equals(sentCode);
    }

    public void change_password(View view) {
        String name = et_email.getText().toString();
        String pwd = et_pwd.getText().toString();
        String equal = et_equal.getText().toString();

        if (!verifyCode()) {
            Toast.makeText(requireContext(), "Email Code incorrect", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isHKUEmail(name)) {
            Toast.makeText(requireContext(), "Email must be your school email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isPasswordValid(pwd)) {
            return;
        }

        if (!pwd.equals(equal)) {
            Toast.makeText(requireContext(), "Two time passwords are different", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: update password in database
        Toast.makeText(requireContext(), "Password changed successfully!", Toast.LENGTH_SHORT).show();
    }

    private boolean isHKUEmail(String email) {
        return email.endsWith("@connect.hku.hk") || email.endsWith("@hku.hk");
    }

    private boolean isPasswordValid(String password) {
        if (password.length() < 6) {
            Toast.makeText(requireContext(), "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.matches(".*[a-z].*")) {
            Toast.makeText(requireContext(), "Password must include at least one lowercase letter", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            Toast.makeText(requireContext(), "Password must include at least one number", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void Isvisiable2(View view) {
        if (Visiable1 == 0) {
            et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            iv_eye2.setImageResource(R.drawable.baseline_visibility_24);
            Visiable1 = 1;
        } else {
            et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            iv_eye2.setImageResource(R.drawable.baseline_visibility_off_24);
            Visiable1 = 0;
        }
    }

    public void Isvisiable3(View view) {
        if (Visiable2 == 0) {
            et_equal.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            iv_eye3.setImageResource(R.drawable.baseline_visibility_24);
            Visiable2 = 1;
        } else {
            et_equal.setTransformationMethod(PasswordTransformationMethod.getInstance());
            iv_eye3.setImageResource(R.drawable.baseline_visibility_off_24);
            Visiable2 = 0;
        }
    }
}
