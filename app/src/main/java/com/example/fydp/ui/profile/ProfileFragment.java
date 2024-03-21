package com.example.fydp.ui.profile;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;

import com.example.fydp.MainActivity;
import com.example.fydp.R;
import com.example.fydp.ui.home.Dispense;

public class ProfileFragment extends Fragment{
    TextView usersName, usersEmail, usersPhone, usersPass, usersEmergName, usersEmergPhone;
    Button editInfoButton;


    public ProfileFragment() {

    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        //Fill in users information
        String test = "";
        usersName = rootView.findViewById(R.id.nameValue);
        usersName.setText("Name: " + test);
        usersEmail = rootView.findViewById(R.id.emailValue);
        usersEmail.setText("Email: " + test);
        usersPhone = rootView.findViewById(R.id.phoneValue);
        usersPhone.setText("Phone: " + test);
        usersPass = rootView.findViewById(R.id.passValue);
        usersPass.setText("Pass: " + test);
        usersEmergName = rootView.findViewById(R.id.emergNameValue);
        usersEmergName.setText("Name: " + test);
        usersEmergPhone = rootView.findViewById(R.id.emergPhoneValue);
        usersEmergPhone.setText("Phone: " + test);
        //End fill in users information

        //ADD A LOGOUT FEATURE

        editInfoButton = rootView.findViewById(R.id.editInfoButton);
        editInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.profile_container, new EditProfileFragment());
                transaction.commit();
            }
        });
        return rootView;
    }
}
