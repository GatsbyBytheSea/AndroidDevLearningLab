package fr.imt_atlantique.myfirstapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String familyName;
    private String firstName;
    private String birthPlace;
    private String phoneNumbers;

    public User(String familyName, String firstName, String birthPlace, String phoneNumbers) {
        this.familyName = familyName;
        this.firstName = firstName;
        this.birthPlace = birthPlace;
        this.phoneNumbers = phoneNumbers;
    }

    protected User(Parcel in) {
        familyName = in.readString();
        firstName = in.readString();
        birthPlace = in.readString();
        phoneNumbers = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(familyName);
        dest.writeString(firstName);
        dest.writeString(birthPlace);
        dest.writeString(phoneNumbers);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getFamilyName() {
        return familyName;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getBirthPlace() {
        return birthPlace;
    }
    public String getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setFamilyName(String familyName) { this.familyName = familyName; }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }
    public void setPhoneNumbers(String phoneNumbers) { this.phoneNumbers = phoneNumbers; }
}
