package com.example.fydp.ui.newpill;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import android.widget.TimePicker;

import com.example.fydp.MainActivity;
import com.example.fydp.R;
import com.example.fydp.databinding.ActivityMainBinding;
import com.example.fydp.ui.home.Dispense;
import com.example.fydp.ui.home.HomeFragment;
import com.example.fydp.ui.profile.ProfileFragment;

public class NewPillFragment extends Fragment {

    String medName, medDose, medFreq, medInfo;
    Button submitPillButton, timeButton;
    EditText medNameInput;
    EditText medDoseInput;
    EditText medFreqInput;
    EditText medInfoInput;
    TextView currentTime;

    int curHr, curMin, timeHour, timeMin;
    Calendar calendar;

    public NewPillFragment() {
        // Required empty public constructor
    }

    public static NewPillFragment newInstance(String param1, String param2) {
        NewPillFragment fragment = new NewPillFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_pill, container, false);

        medNameInput = rootView.findViewById(R.id.medNameInput);
        medDoseInput = rootView.findViewById(R.id.medDoseInput);
        medFreqInput = rootView.findViewById(R.id.medFreqInput);
        medInfoInput = rootView.findViewById(R.id.medInfoInput);

        currentTime = rootView.findViewById(R.id.currentTime);
        timeButton = rootView.findViewById(R.id.timeButton);
        calendar = Calendar.getInstance();
        curHr = calendar.get(Calendar.HOUR);
        curMin = calendar.get(Calendar.MINUTE);

        timeButton.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    currentTime.setText(hourOfDay + ":" + minute);
                    timeHour = hourOfDay;
                    timeMin = minute;
                }
            }, curHr, curMin, false);
            dialog.show();
        });

        submitPillButton = rootView.findViewById(R.id.submitPillButton);
        submitPillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                medName = medNameInput.getText().toString();
                medDose = medDoseInput.getText().toString();
                medFreq = medFreqInput.getText().toString();
                medInfo = medInfoInput.getText().toString();


                Toast.makeText(getActivity(), medName + medDose + medFreq + medInfo +
                        timeHour + ":" + timeMin, Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

//                HomeFragment homeFragment = new HomeFragment();
//                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.replace(R.id.moveHome, homeFragment);
//                transaction.commit();
            }
        });

        return rootView;
    }
}