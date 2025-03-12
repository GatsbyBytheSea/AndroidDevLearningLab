package fr.imt_atlantique.myfirstapplication.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.google.android.material.snackbar.Snackbar;

import fr.imt_atlantique.myfirstapplication.R;

public class DisplayInfoFragment extends Fragment {

    private String firstName, lastName, birthPlace, birthDate, phoneNumbers;
    TextView tvDisplayInfo;

    private OnCloseButtonPressedListener closeListener;
    private OnBackButtonPressedListener backListener;

    public interface OnCloseButtonPressedListener {
        void onCloseButtonPressed();
    }

    public interface OnBackButtonPressedListener {
        void onBackButtonPressed();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnCloseButtonPressedListener && context instanceof OnBackButtonPressedListener) {
            closeListener = (OnCloseButtonPressedListener) context;
            backListener = (OnBackButtonPressedListener) context;
        } else {
            throw new ClassCastException(context + " must implement DisplayInfoFragment.OnCloseButtonPressedListener and DisplayInfoFragment.OnBackButtonPressedListener");
        }
    }

    public static DisplayInfoFragment newInstance(String firstName, String lastName, String birthPlace, String birthDate, String phoneNumbers) {
        DisplayInfoFragment fragment = new DisplayInfoFragment();
        fragment.firstName = firstName;
        fragment.lastName = lastName;
        fragment.birthPlace = birthPlace;
        fragment.birthDate = birthDate;
        fragment.phoneNumbers = phoneNumbers;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_information, container, false);
        tvDisplayInfo = view.findViewById(R.id.tv_display_info);

        MenuHost menuHost = getActivity();
        if(menuHost != null){
            menuHost.addMenuProvider(new MenuProvider() {
                @Override
                public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                    menuInflater.inflate(R.menu.menu_display_user, menu);
                }

                @Override
                public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                    int id = menuItem.getItemId();
                    if(id == R.id.action_dial_phone){
                        if (phoneNumbers != null && !phoneNumbers.isEmpty()) {
                            String[] phones = phoneNumbers.split("\\|\\|");

                            for (int i = 0; i < phones.length; i++) {
                                phones[i] = phones[i].trim();
                            }
                            if (phones.length == 1) {
                                dialPhone(phones[0]);
                            }else{
                                new androidx.appcompat.app.AlertDialog.Builder(getActivity())
                                        .setTitle("Select Phone Number")
                                        .setItems(phones, (dialog, which) -> dialPhone(phones[which]))
                                        .setNegativeButton("Cancel", null)
                                        .show();
                            }
                        } else {
                            Snackbar.make(tvDisplayInfo, "No phone numbers available", Snackbar.LENGTH_LONG).show();
                        }
                        return true;
                    }
                    return false;
                }
            },getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        }

        String displayInfo = getString(R.string.title_family_name) + " : " + firstName + "\n" +
                getString(R.string.title_first_name) + " : " + lastName + "\n" +
                getString(R.string.title_birth_place) + " : " + birthPlace + "\n" +
                getString(R.string.title_birth_date) + " : " + birthDate + "\n";

        if (phoneNumbers != null && !phoneNumbers.isEmpty()) {
            displayInfo += getString(R.string.title_phone_number)+"\n";
            String[] phones = phoneNumbers.split("\\|\\|");
            for (String phone : phones) {
                displayInfo += phone.trim() + "\n";
            }
        }

        tvDisplayInfo.setText(displayInfo);

        Button btnClose = view.findViewById(R.id.back_button_on_birthdate_page);
        btnClose.setOnClickListener(v -> {
            if(getActivity() instanceof OnCloseButtonPressedListener) {
                closeListener.onCloseButtonPressed();
            }
        });

        Button btnBack = view.findViewById(R.id.back_button);
        btnBack.setOnClickListener(v -> {
            if(getActivity() instanceof OnBackButtonPressedListener) {
                backListener.onBackButtonPressed();
            }
        });

        return view;
    }

    private void dialPhone(String phoneNumber) {
        if (!phoneNumber.isEmpty()) {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:" + phoneNumber));
            if (dialIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivity(dialIntent);
            } else {
                Snackbar.make(tvDisplayInfo, "No dialer app found", Snackbar.LENGTH_LONG).show();
            }
        } else {
            Snackbar.make(tvDisplayInfo, "No valid phone number", Snackbar.LENGTH_LONG).show();
        }
    }
}
