package fr.imt_atlantique.myfirstapplication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
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

    private String firstName;
    private String lastName;
    private String birthPlace;
    private String phone;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info_fragment);
        EdgeToEdge.enable(this);
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
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, DisplayInfoFragment.newInstance(firstName, lastName, birthPlace, birthDateInfo, phone))
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
