package fr.imt_atlantique.myfirstapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.View;
import fr.imt_atlantique.myfirstapplication.fragments.DisplayFragment;
import fr.imt_atlantique.myfirstapplication.fragments.EditFragment;

public class DynamicallyCreateFragmentsActivity extends AppCompatActivity implements EditFragment.OnSubmitInterface {
    public FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamically_create_fragments);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dynamically_create_fragments), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if(savedInstanceState == null){
            fragment = new EditFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    public void onSubmit(String name) {
        DisplayFragment displayFragment = new DisplayFragment();
        Bundle bundle = new Bundle();
        bundle.putString("displayText", name);
        displayFragment.setArguments(bundle);
        fm.beginTransaction()
                .replace(R.id.fragment_container, displayFragment)
                .addToBackStack(null)
                .commit();
    }

    public void close(View view) {
        finish();
    }

}
