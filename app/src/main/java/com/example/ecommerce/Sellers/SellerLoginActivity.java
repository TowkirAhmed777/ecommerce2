package com.example.ecommerce.Sellers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SellerLoginActivity extends AppCompatActivity {

    private TextView loginSellerBtn;
    private EditText  emailInput, passwordInput ;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);


        mAuth = FirebaseAuth.getInstance();

        emailInput = findViewById(R.id.seller_login_email);
        passwordInput = findViewById(R.id.seller_login_password);
        loginSellerBtn = findViewById(R.id.seller_login_btn);

        loadingBar = new ProgressDialog(this);



   loginSellerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginSeller();
            }
        });

    }

    private void LoginSeller() {


       final  String email = emailInput.getText().toString();
        final String password = passwordInput.getText().toString();

        if (!email.equals("") && !password.equals("") ) {


            loadingBar.setTitle(" Seller Account Login");
            loadingBar.setMessage("please wait , while we are checking the credential");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(SellerLoginActivity
                                        .this,SellerHomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

        }
        else  {
            Toast.makeText(this, "Please complete the Login form.", Toast.LENGTH_SHORT).show();
        }
    }
}