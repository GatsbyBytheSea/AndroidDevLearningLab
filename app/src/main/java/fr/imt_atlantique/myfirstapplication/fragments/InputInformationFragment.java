package fr.imt_atlantique.myfirstapplication.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.google.android.material.snackbar.Snackbar;

import fr.imt_atlantique.myfirstapplication.R;

public class InputInformationFragment extends Fragment {
    private LinearLayout inputLayout;
    private EditText etFirstName, etLastName;
    private Spinner spBirthPlace;

    private String phoneNumbers = "";
    private static final int STATIC_CHILD_COUNT = 7;

    private OnPersonalInfoSubmittedListener listener;

    public interface OnPersonalInfoSubmittedListener {
        void onPersonalInfoSubmitted(String firstName, String lastName, String birthPlace, String phoneNumbers);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnPersonalInfoSubmittedListener) {
            listener = (OnPersonalInfoSubmittedListener) context;
        } else {
            throw new ClassCastException(context + " must implement InputInformationFragment.OnPersonalInfoSubmittedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_input_information, container, false);

        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == R.id.action_reset){
                    etFirstName.setText("");
                    etLastName.setText("");
                    spBirthPlace.setSelection(0);
                    phoneNumbers = "";
                    int totalChildren = inputLayout.getChildCount();
                    for (int i = STATIC_CHILD_COUNT; i < totalChildren; i++) {
                        inputLayout.removeViewAt(i);
                    }
                    return true;
                }
                if(id == R.id.action_wikipedia){
                    String birthCity = spBirthPlace.getSelectedItem().toString();
                    String url = "http://fr.wikipedia.org/?search=" + birthCity;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Snackbar.make(spBirthPlace, getString(R.string.error_no_browser_message), Snackbar.LENGTH_LONG).show();
                    }
                    return true;
                }
                if (id == R.id.action_share_birth_city) {
                    String birthCity = spBirthPlace.getSelectedItem().toString();
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.title_my_birth_city) + " " + birthCity);
                    shareIntent.setType("text/plain");

                    Intent chooser = Intent.createChooser(shareIntent, getString(R.string.msg_share_via));
                    if (shareIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                        startActivity(chooser);
                    } else {
                        Snackbar.make(spBirthPlace, getString(R.string.error_no_share_app), Snackbar.LENGTH_LONG).show();
                    }
                    return true;
                }
                return false;
            }
        },getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        inputLayout = view.findViewById(R.id.input_linear_layout);
        etFirstName = view.findViewById(R.id.et_first_name);
        etLastName = view.findViewById(R.id.et_family_name);
        spBirthPlace = view.findViewById(R.id.select_birth_place);
        Button btnNext = view.findViewById(R.id.next_button);

        btnNext.setOnClickListener(v -> {
            if(listener != null) {
                phoneNumbers = collectPhoneNumbers();
                listener.onPersonalInfoSubmitted(
                        etFirstName.getText().toString(),
                        etLastName.getText().toString(),
                        spBirthPlace.getSelectedItem().toString(),
                        phoneNumbers
                );
            }
        });

        Button addPhoneButton = view.findViewById(R.id.add_phone_button);
        addPhoneButton.setOnClickListener(v -> {
            addPhone();
        });
        return view;
    }

    private void addPhone() {
        LinearLayout phoneInfoContainer = new LinearLayout(getContext());
        phoneInfoContainer.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        phoneInfoContainer.setLayoutParams(containerParams);

        TextView tvPhone = new TextView(getContext());
        tvPhone.setText(R.string.title_phone_number);
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        tvPhone.setLayoutParams(tvParams);

        EditText etPhone = new EditText(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        etPhone.setLayoutParams(params);
        etPhone.setInputType(InputType.TYPE_CLASS_PHONE);


        Button removePhoneButton = new Button(getContext());
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        removePhoneButton.setLayoutParams(buttonParams);
        removePhoneButton.setText(R.string.btn_delete);

        phoneInfoContainer.addView(tvPhone);
        phoneInfoContainer.addView(etPhone);
        phoneInfoContainer.addView(removePhoneButton);
        inputLayout.addView(phoneInfoContainer);

        removePhoneButton.setOnClickListener(v -> {
            phoneNumbers = phoneNumbers.replace(etPhone.getText().toString() + "\n", "");
            inputLayout.removeView(phoneInfoContainer);
        });
    }

    private String collectPhoneNumbers() {
        StringBuilder sb = new StringBuilder();
        int totalChildren = inputLayout.getChildCount();
        for (int i = STATIC_CHILD_COUNT; i < totalChildren; i++) {
            LinearLayout phoneContainer = (LinearLayout) inputLayout.getChildAt(i);
            EditText phoneEditText = (EditText) phoneContainer.getChildAt(1);
            String phone = phoneEditText.getText().toString();
            if (!phone.isEmpty()) {
                if (sb.length() > 0) {
                    sb.append("||");
                }
                sb.append(phone);
            }
        }
        return sb.toString();
    }
}
