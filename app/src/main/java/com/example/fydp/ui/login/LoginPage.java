package com.example.fydp.ui.login;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fydp.MainActivity;
import com.example.fydp.R;
import com.example.fydp.ui.login.CreateAccount;

public class LoginPage extends AppCompatActivity {

    EditText emailInput, passInput;
    TextView forgotPassText, createText;
    Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);
        passInput = findViewById(R.id.passInput);
        signInButton = findViewById(R.id.signInButton);

        signInButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                String email = emailInput.getText().toString();
                String pass = passInput.getText().toString();

                if (!email.isEmpty() && !pass.isEmpty()) {
                    // Log in
                    Intent intent = new Intent(LoginPage.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        forgotPassText = findViewById(R.id.forgotPassText);
        createText = findViewById(R.id.createText);

        forgotPassText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //Go to a submit email page and email them reset link
            }
        });
        createText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(LoginPage.this, CreateAccount.class);
                startActivity(intent);
            }
        });

    }
}
