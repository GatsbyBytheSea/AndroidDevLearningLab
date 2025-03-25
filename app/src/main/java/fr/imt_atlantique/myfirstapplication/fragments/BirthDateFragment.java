package fr.imt_atlantique.myfirstapplication.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;

import fr.imt_atlantique.myfirstapplication.PersonalInfoViewModel;
import fr.imt_atlantique.myfirstapplication.R;

public class BirthDateFragment extends Fragment {


    private Button btnSelectBirthDate;
    private String selectedBirthDate = "";
    private PersonalInfoViewModel viewModel;

    private OnBirthDateSubmittedListener submitListener;
    private OnBackButtonPressedListener backListener;

    public interface OnBirthDateSubmittedListener {
        void onBirthDateSubmitted(String birthDate);
    }

    public interface OnBackButtonPressedListener {
        void onBackButtonPressed();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnBirthDateSubmittedListener && context instanceof OnBackButtonPressedListener) {
            submitListener = (OnBirthDateSubmittedListener) context;
            backListener = (OnBackButtonPressedListener) context;
        } else {
            throw new ClassCastException(context + " must implement BirthDateFragment.OnBirthDateSubmittedListener and BirthDateFragment.OnBackButtonPressedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_birth_date, container, false);
        Button btnDisplay = view.findViewById(R.id.next_button);
        btnSelectBirthDate = view.findViewById(R.id.select_birth_date);
        btnSelectBirthDate.setOnClickListener(v-> showDatePicker());

        viewModel = new ViewModelProvider(requireActivity()).get(PersonalInfoViewModel.class);

        if (savedInstanceState != null) {
            selectedBirthDate = savedInstanceState.getString("selectedBirthDate", "");
        }
        if (!viewModel.birthDate.isEmpty()) {
            selectedBirthDate = viewModel.birthDate;
        }

        if (!selectedBirthDate.isEmpty()) {
            btnSelectBirthDate.setText(selectedBirthDate);
        }

        btnDisplay.setOnClickListener(v -> {
            viewModel.birthDate = selectedBirthDate;
            if (getActivity() instanceof OnBirthDateSubmittedListener) {
                submitListener.onBirthDateSubmitted(selectedBirthDate);
            }
        });

        Button btnBack = view.findViewById(R.id.back_button_on_birthdate_page);
        btnBack.setOnClickListener(v -> {
            if (getActivity() instanceof OnBackButtonPressedListener) {
                backListener.onBackButtonPressed();
            }
        });
        return view;
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year1, month1, dayOfMonth) -> {
                    selectedBirthDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    btnSelectBirthDate.setText(selectedBirthDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("selectedBirthDate", selectedBirthDate);
    }
}
