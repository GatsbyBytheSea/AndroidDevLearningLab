package fr.imt_atlantique.myfirstapplication;

import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

public class PersonalInfoViewModel extends ViewModel {
    public String firstName = "";
    public String lastName = "";
    public String birthPlace = "";
    public String birthDate = "";

    public List<String> phoneNumbers = new ArrayList<>();
}
