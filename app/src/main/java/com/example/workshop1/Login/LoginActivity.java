package com.example.workshop1.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.workshop1.Admin.AdminActivity;
import com.example.workshop1.R;
import com.example.workshop1.SQLite.Mysqliteopenhelper;
import com.example.workshop1.SQLite.User;
import com.example.workshop1.Student.StudentActivity;
import com.example.workshop1.Vendor.VendorActivity;
import com.example.workshop1.Ethereum.EthereumManager;

import java.math.BigInteger;

public class LoginActivity extends AppCompatActivity {

    private EditText et_name,et_password,et_phoneCode;
    private Mysqliteopenhelper mysqliteopenhelper;
    private ImageView iv_showCode,iv_eye;

    //产生验证码
    private String realCode;
    private int Visiable=0;

    //记住密码
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox remeberPass;
    private String account,password;

    private EthereumManager ethereumManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mysqliteopenhelper=new Mysqliteopenhelper(this);
        /* for testing */
        //mysqliteopenhelper.reset();

        et_name=findViewById(R.id.et_login_name);
        et_password=findViewById(R.id.et_login_password);
        et_phoneCode=findViewById(R.id.et_number_code);//验证码数字

        //图片验证码
        iv_showCode = (ImageView) findViewById(R.id.iv_showCode);
        //将验证码用图片的形式显示出来
        iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();

        //密码是否可见
        iv_eye=findViewById(R.id.iv_eye);

        //记住密码
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        remeberPass=(CheckBox)findViewById(R.id.remeber_pass);

        // Initialize EthereumManager
        ethereumManager = new EthereumManager();
    }


    @Override
    protected void onResume() {
        super.onResume();

        //-----------------------------实现记住密码功能-----------------------------
        //展示数据
        boolean isRemember=pref.getBoolean("remember_password",false);
        if(isRemember){
            //将账号和密码都设置到文本框当中
            account=pref.getString("account","");
            password=pref.getString("password","");
            et_name.setText(account);
            et_password.setText(password);
            remeberPass.setChecked(true);
        }
        else{
            et_name.setText("");
            et_password.setText("");
            remeberPass.setChecked(false);
        }
        et_phoneCode.setText("");

    }

    // login button listener
    public void jumptoMainActivity(View view){
        account=et_name.getText().toString();
        password=et_password.getText().toString();

        //登陆按钮监听，验证码是否正确
        String phoneCode = et_phoneCode.getText().toString().toLowerCase();//大小写都行
        if (phoneCode.equals(realCode)) {
            // Toast.makeText(this, phoneCode + "Verification Code CORRECT", Toast.LENGTH_SHORT).show();

            //验证码正确之后再尝试登陆
            User login_success = mysqliteopenhelper.login(account,password);
            if(login_success != null){
                //读取数据
                editor=pref.edit();
                if(remeberPass.isChecked()){
                    editor.putBoolean("remember_password",true);
                    editor.putString("account",account);
                    editor.putString("password",password);
                }
                else{
                    editor.putBoolean("remember_password",false);
                    editor.clear();
                }
                editor.apply();

                Toast.makeText(this,"Login successful",Toast.LENGTH_SHORT).show();


                // -----------------------Jump to right activity----------------------
                String type = login_success.getType();

                Intent intent = null;

                // Based on selected user type, go to the respective activity
                switch (type) {
                    case "student":
                        intent = new Intent(this, StudentActivity.class);
                        // Assign student role in smart contract if needed
                        ethereumManager.assignRole(login_success.getWalletAddress(), "STUDENT");
                        break;
                    case "vendor":
                        intent = new Intent(this, VendorActivity.class);
                        // Assign vendor role in smart contract if needed
                        ethereumManager.assignRole(login_success.getWalletAddress(), "VENDOR");
                        break;
                    case "admin":
                        intent = new Intent(this, AdminActivity.class);
                        break;
                    default:
                        Toast.makeText(this, "user type invalid", Toast.LENGTH_SHORT).show();
                        return;
                }

                // Get user's token balance
                BigInteger balance = ethereumManager.getBalance(login_success.getWalletAddress());
                intent.putExtra("tokenBalance", balance.toString());
                intent.putExtra("userObj", login_success);
                startActivity(intent);  // 登陆成功，跳转到对应的 Activity

            }
            else{
                Toast.makeText(this,"Incorrect email or password.",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Verification code error!", Toast.LENGTH_SHORT).show();
        }

    }

    //监听注册按钮
    public void jumptoRegister(View view){
        Intent intent=new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    //监听图片验证码
    public void changenumber(View view){
        iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();//不区分大小写哈
    }

    //监听密码是否可见
    public void Isvisiable(View view){
        if(Visiable==0){
            et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());//可见
            iv_eye.setImageResource(R.drawable.baseline_visibility_24);
            Visiable=1;
        }
        else{
            et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());//不可见
            iv_eye.setImageResource(R.drawable.baseline_visibility_off_24);
            Visiable=0;
        }
    }
}