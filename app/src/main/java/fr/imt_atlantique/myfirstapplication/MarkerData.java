package fr.imt_atlantique.myfirstapplication;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class MarkerData implements Parcelable {

    public LatLng position;
    public String title;
    public String markerImagePath;

    public MarkerData(LatLng position, String title, String markerImagePath) {
        this.position = position;
        this.title = title;
        this.markerImagePath = markerImagePath;
    }

    protected MarkerData(Parcel in) {
        position = in.readParcelable(LatLng.class.getClassLoader());
        title = in.readString();
        markerImagePath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(position, flags);
        dest.writeString(title);
        dest.writeString(markerImagePath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MarkerData> CREATOR = new Creator<>() {
        @Override
        public MarkerData createFromParcel(Parcel in) {
            return new MarkerData(in);
        }

        @Override
        public MarkerData[] newArray(int size) {
            return new MarkerData[size];
        }
    };
}
