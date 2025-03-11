package fr.imt_atlantique.myfirstapplication.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import fr.imt_atlantique.myfirstapplication.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class EditFragment extends Fragment {

    public interface OnSubmitInterface {
        void onSubmit(String name);
    }

    private OnSubmitInterface callback;
    private EditText editText;

    public EditFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnSubmitInterface) {
            callback = (OnSubmitInterface) context;
        } else {
            throw new RuntimeException(context.toString() + " OnEditNameInterface must be implemented");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText = view.findViewById(R.id.editText_input);
        Button button = view.findViewById(R.id.btn_send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString().trim();
                if (callback != null) {
                    callback.onSubmit(input);
                }
            }
        });
    }

    public void close(View view) {
        getActivity().finish();
    }
}
