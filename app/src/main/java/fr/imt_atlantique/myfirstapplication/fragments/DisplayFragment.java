package fr.imt_atlantique.myfirstapplication.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import fr.imt_atlantique.myfirstapplication.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class DisplayFragment extends Fragment {

    private TextView textViewDisplay;

    public DisplayFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_display, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textViewDisplay = view.findViewById(R.id.textView_display);
        if(getArguments() != null){
            String text = getArguments().getString("displayText");
            updateDisplay(text);
        }

        Button btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            if(getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    public void updateDisplay(String text) {
        if (textViewDisplay != null) {
            textViewDisplay.setText(text);
        }
    }
}
