package com.example.fydp.ui.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.fydp.MainActivity;
import com.example.fydp.R;

public class EditProfileFragment extends Fragment {

    String name, email, phone, emergName, emergPhone, pass;
    EditText nameInput;
    EditText emailInput;
    EditText phoneInput;
    EditText passInput;
    EditText emergNameInput;
    EditText emergPhoneInput;

    Button submitButton;

    public EditProfileFragment() {
        // Required empty public constructor

    }

    public static EditProfileFragment newInstance() {
        EditProfileFragment fragment = new EditProfileFragment();
        return fragment;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_profile);
        View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        nameInput = rootView.findViewById(R.id.nameInput);
        emailInput = rootView.findViewById(R.id.emailInput);
        phoneInput = rootView.findViewById(R.id.phoneInput);
        passInput = rootView.findViewById(R.id.passInput);
        emergNameInput = rootView.findViewById(R.id.emergNameInput);
        emergPhoneInput = rootView.findViewById(R.id.emergPhoneInput);

        submitButton = rootView.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameInput.getText().toString();
                email = emailInput.getText().toString();
                phone = phoneInput.getText().toString();
                pass = passInput.getText().toString();
                emergName = emergNameInput.getText().toString();
                emergPhone = emergPhoneInput.getText().toString();

                Intent intent=new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
//                Toast.makeText(getActivity(), name + email + phone + pass + emergName + emergPhone, Toast.LENGTH_LONG).show();
            }
        });
        return rootView;
        //return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}