 package com.example.ecommerce.Buyers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.Admin.AdminHomeActivity;
import com.example.ecommerce.Model.Users;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText InputPhoneNumber, InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;

    private TextView AdminLink, NotAdminLink,ForgetPasswordLink;

    private String parentDbName = "Users";

    private CheckBox chkBoxRememberMe;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        LoginButton = (Button) findViewById(R.id.login_btn);

        InputPassword = (EditText) findViewById(R.id.login__password_input);

        InputPhoneNumber = (EditText) findViewById(R.id.login_phone_number_input);
        AdminLink =(TextView)findViewById(R.id.admin_panel_link);
        NotAdminLink =(TextView)findViewById(R.id.not_admin_panel_link);

ForgetPasswordLink = findViewById(R.id.forget_password_link);

        loadingBar = new ProgressDialog(this);

        chkBoxRememberMe = (CheckBox) findViewById(R.id.remember_me_chkb);
        Paper.init(this);




        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });

ForgetPasswordLink.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
        intent.putExtra("check", "login");
        startActivity(intent);
    }
});



        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });

        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });





    }

    private void LoginUser() {

        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(phone)) {

            Toast.makeText(this, "please write your phone number....", Toast.LENGTH_SHORT);
        } else if (TextUtils.isEmpty(password)) {

            Toast.makeText(this, "please write your password....", Toast.LENGTH_SHORT);
        }

        else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("please wait , while we are checking the credential");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            
            AllowAccessToAccount(phone, password);
            
        }


    }

    private void AllowAccessToAccount(String phone, String password) {


        if (chkBoxRememberMe.isChecked()) {

            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);

        }


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(parentDbName).child(phone).exists()) {
                    Users usersData = snapshot.child(parentDbName).child(phone).getValue(Users.class);




                    Log.d("test_0000",snapshot.child(parentDbName).child(phone).toString());
                    Log.d("test_001", usersData.toString());
                    if (usersData.getPhone().equals(phone)) {

                        if (usersData.getPassword().equals(password)) {
                           if (parentDbName.equals("Admins")) {
                               Toast.makeText(LoginActivity.this," Welcome Admin, you are logged in Successfully...",Toast.LENGTH_SHORT).show();
                               loadingBar.dismiss();

                               Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                                Prevalent.currentOnlineUser =usersData;
                               startActivity(intent);
                            }

                           else if(parentDbName.equals("Users")) {
                               Toast.makeText(LoginActivity.this,"logged in Successfully...",Toast.LENGTH_SHORT).show();
                               loadingBar.dismiss();

                               Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                               Prevalent.currentOnlineUser =usersData;

                               startActivity(intent);
                           }
                        }

                        else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this,"Password is incorrect", Toast.LENGTH_SHORT).show();
                        }


                    }
                }

                else {
                    Toast.makeText(LoginActivity.this,"Account with this " + phone + "Number do not exists ", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();







                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("12345","TEST FOR DATABASE Error > " + error.getMessage());
                Toast.makeText(LoginActivity.this, "Error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

            }
        });



    }
}