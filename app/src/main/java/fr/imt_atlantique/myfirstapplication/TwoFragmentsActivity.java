package fr.imt_atlantique.myfirstapplication;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import fr.imt_atlantique.myfirstapplication.fragments.EditFragment;
import fr.imt_atlantique.myfirstapplication.fragments.DisplayFragment;

public class TwoFragmentsActivity extends AppCompatActivity implements EditFragment.OnSubmitInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_fragments);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main_container), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FragmentManager fm = getSupportFragmentManager();
        if (savedInstanceState == null) {
            fm.beginTransaction()
                    .replace(R.id.fragment_container_edit, new EditFragment(), "EDIT_FRAGMENT")
                    .commit();
            fm.beginTransaction()
                    .replace(R.id.fragment_container_display, new DisplayFragment(), "DISPLAY_FRAGMENT")
                    .commit();
        }
    }

    @Override
    public void onSubmit(String name) {
        DisplayFragment displayFragment = (DisplayFragment) getSupportFragmentManager().findFragmentByTag("DISPLAY_FRAGMENT");
        if (displayFragment != null) {
            displayFragment.updateDisplay(name);
        }
    }

    public void close(View view) {
        finish();
    }
}
