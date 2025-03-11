package fr.imt_atlantique.myfirstapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.Snackbar;
import fr.imt_atlantique.myfirstapplication.R;

public class StaticFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_static, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText editText = view.findViewById(R.id.editTextInput);
        Button btnShowSnackbar = view.findViewById(R.id.btnShowSnackbar);

        btnShowSnackbar.setOnClickListener(v -> {
            String inputText = editText.getText().toString().trim();
            Snackbar.make(view, inputText, Snackbar.LENGTH_LONG).show();
        });
    }

    @Override
    public void onAttach(@NonNull android.content.Context context) {
        super.onAttach(context);
    }
}
