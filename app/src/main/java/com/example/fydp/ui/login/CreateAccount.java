package com.example.fydp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fydp.R;

public class CreateAccount extends AppCompatActivity {
    EditText nameInput, emailInput, passInput, phoneInput, emergNameInput, emergPhoneInput;
    TextView haveAccountText;
    Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create);

        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passInput = findViewById(R.id.passInput);
        phoneInput = findViewById(R.id.phoneInput);
        emergNameInput = findViewById(R.id.emergNameInput);
        emergPhoneInput = findViewById(R.id.emergPhoneInput);

        createAccountButton = findViewById(R.id.createAccountButton);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String email = emailInput.getText().toString();
                String pass = passInput.getText().toString();
                String name = nameInput.getText().toString();
                String phone = phoneInput.getText().toString();
                String eName = emergNameInput.getText().toString();
                String ePhone = emergPhoneInput.getText().toString();

                if (!email.isEmpty() && !pass.isEmpty() && !name.isEmpty() && !phone.isEmpty()
                && !eName.isEmpty() && !ePhone.isEmpty()) {
                    // Log in
                }
            }
        });

        haveAccountText = findViewById(R.id.haveAccountText);
        haveAccountText.setOnClickListener(new Z.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateAccount.this, LoginPage.class);
                startActivity(intent);
            }
        });
    }
}
