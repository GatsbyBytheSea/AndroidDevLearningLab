package fr.imt_atlantique.myfirstapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import fr.imt_atlantique.myfirstapplication.fragments.BirthDateFragment;
import fr.imt_atlantique.myfirstapplication.fragments.DisplayInfoFragment;
import fr.imt_atlantique.myfirstapplication.fragments.InputInformationFragment;

public class PersonalInfoFragmentActivity extends AppCompatActivity implements InputInformationFragment.OnPersonalInfoSubmittedListener,
        BirthDateFragment.OnBirthDateSubmittedListener,
        DisplayInfoFragment.OnCloseButtonPressedListener,
        DisplayInfoFragment.OnBackButtonPressedListener,
        BirthDateFragment.OnBackButtonPressedListener {

    private String firstName, lastName, birthPlace, phone, birthDate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info_fragment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fragment_container), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new InputInformationFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onPersonalInfoSubmitted(String firstName, String lastName, String birthPlace, String phoneNumbers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthPlace = birthPlace;
        this.phone = phoneNumbers;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new BirthDateFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBirthDateSubmitted(String birthDateInfo) {
        this.birthDate = birthDateInfo;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, DisplayInfoFragment.newInstance(firstName, lastName, birthPlace, birthDate, phone))
                .addToBackStack(null)
                .commit();
    }

    public void onBackButtonPressed() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onCloseButtonPressed() {
        finish();
    }
}
