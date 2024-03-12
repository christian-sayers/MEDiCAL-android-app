package com.example.fydp.ui.refill;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView;

import com.example.fydp.R;
import com.example.fydp.ui.profile.ProfileFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RefillFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RefillFragment extends Fragment {
    EditText quantityInput;
    Spinner spinner;
    Button submitButton;

    public RefillFragment() {
        // Required empty public constructor
    }

    public static RefillFragment newInstance() {
        RefillFragment fragment = new RefillFragment();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_refill, container, false);

        // Find the Spinner within the inflated view
        spinner = view.findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        quantityInput = view.findViewById(R.id.quantityInput);
        submitButton = view.findViewById(R.id.submitButton);
//        String selectedItem = "";


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Retrieve the selected item from the adapter

                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int quantity = Integer.parseInt(quantityInput.getText().toString());
                        String selectedItem = (String) parent.getItemAtPosition(position);
                        Toast.makeText(getActivity(), "Quantity: " + quantity + ", Selected item: " + selectedItem, Toast.LENGTH_LONG).show();
                    }

                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        return view;
    }
}