package com.example.fydp.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fydp.R;
import com.example.fydp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    Button pillButton1, pillButton2;

    public HomeFragment() {
        // Required empty public constructor

    }

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
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


        String[] elements = {"Button 1", "Button 2", "Button 3"};

        LinearLayout layout = rootView.findViewById(R.id.buttonLayout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, // width
            LinearLayout.LayoutParams.WRAP_CONTENT // height
        );

        int backC = getResources().getColor(R.color.blue_500);
        int textC = getResources().getColor(R.color.white);

        for (int i = 0; i < elements.length; i++) {
            // Create a new button
            Button button = new Button(requireActivity());
            // Set button text
            button.setText(elements[i]);

            if (i > 0) {
                layoutParams.setMargins(0, 16, 0, 0); // Top margin between buttons
            }
            button.setLayoutParams(layoutParams);
            button.setBackgroundColor(backC);
            button.setTextColor(textC);
//            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16); // Set text size in scaled pixels
            button.setPadding(16, 16, 16, 16);

            String pill = "4:00 pm - " + elements[i];
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), Dispense.class);
                    intent.putExtra("pillName", pill);
                    startActivity(intent);
                }
            });

            layout.addView(button);
        }
        return rootView;
    }
}