package com.example.fydp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fydp.R;
import com.example.fydp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

//    String name, email, phone, emergName, emergPhone, pass;
//    EditText nameInput;
//    EditText emailInput;
//    EditText phoneInput;
//    EditText passInput;
//    EditText emergNameInput;
//    EditText emergPhoneInput;

    Button pillButton1, pillButton2;

    // TODO: Rename and change types of parameters

    public HomeFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static com.example.fydp.ui.home.HomeFragment newInstance() {
        com.example.fydp.ui.home.HomeFragment fragment = new com.example.fydp.ui.home.HomeFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


        pillButton1 = rootView.findViewById(R.id.pillButton1);
        pillButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Dispense.class);
                String pill = "Advil";
                intent.putExtra("key", pill);
                startActivity(intent);
            }
        });
//        pillButton2 = rootView.findViewById(R.id.pillButton2);
//        pillButton2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getActivity(), Dispense.class);
//                String pill = "Tylenol";
//                intent.putExtra("pillName", pill);
//                startActivity(intent);
//            }
//        });
        return rootView;
        //return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}