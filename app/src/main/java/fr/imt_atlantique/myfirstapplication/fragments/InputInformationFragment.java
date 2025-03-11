package fr.imt_atlantique.myfirstapplication.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import fr.imt_atlantique.myfirstapplication.R;

public class InputInformationFragment extends Fragment {
    private EditText etFirstName, etLastName;
    private Spinner spBirthPlace;
    private Button btnNext;

    private OnPersonalInfoSubmittedListener listener;

    public interface OnPersonalInfoSubmittedListener {
        void onPersonalInfoSubmitted(String firstName, String lastName, String birthPlace);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnPersonalInfoSubmittedListener) {
            listener = (OnPersonalInfoSubmittedListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement InputInformationFragment.OnPersonalInfoSubmittedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_input_information, container, false);
        etFirstName = view.findViewById(R.id.et_first_name);
        etLastName = view.findViewById(R.id.et_family_name);
        spBirthPlace = view.findViewById(R.id.select_birth_place);
        btnNext = view.findViewById(R.id.next_button);

        btnNext.setOnClickListener(v -> {
            if(listener != null) {
                listener.onPersonalInfoSubmitted(
                        etFirstName.getText().toString(),
                        etLastName.getText().toString(),
                        spBirthPlace.getSelectedItem().toString()
                );
            }
        });
        return view;
    }

}
