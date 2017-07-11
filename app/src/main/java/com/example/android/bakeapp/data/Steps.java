package com.example.android.bakeapp.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by bjoern on 10.05.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description: Here we are store all of the steps information
 * and we are using Parcelable for the serialization of the
 * Steps objects.
 */

public class Steps implements Parcelable {
    public static final Creator<Steps> CREATOR = new Creator<Steps>() {
        @Override
        public Steps createFromParcel(Parcel in) {
            return new Steps(in);
        }

        @Override
        public Steps[] newArray(int size) {
            return new Steps[size];
        }
    };
    //Everything around the single steps
    private String stepsShortDescription;
    private String stepsDescription;
    private String stepsVideoURL;
    private String stepsThumbnailURL;

    public Steps (String stepsShortDescription,
                  String stepsDescription,
                  String stepsVideoURL,
                  String stepsThumbnailURL)
    {
        this.stepsShortDescription = stepsShortDescription;
        this.stepsDescription = stepsDescription;
        this.stepsVideoURL = stepsVideoURL;
        this.stepsThumbnailURL = stepsThumbnailURL;
    }

    /**
     * The constructor for the serialisation
     *
     * @param in the parcel object
     */

    protected Steps(Parcel in) {
        stepsShortDescription = in.readString();
        stepsDescription = in.readString();
        stepsVideoURL = in.readString();
        stepsThumbnailURL = in.readString();
    }

    public String getStepsShortDescription() {
        return stepsShortDescription;
    }

    public String getStepsVideoURL() {
        return stepsVideoURL;
    }

    public String getStepsThumbnailURL() {
        if (TextUtils.isEmpty(stepsThumbnailURL)) {
            return null;
        }
        return stepsThumbnailURL;
    }

    public String getStepsDescription() {
        return stepsDescription;
    }

    // Everything around the serialization of the objects.
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getStepsShortDescription());
        parcel.writeString(getStepsDescription());
        parcel.writeString(getStepsVideoURL());
        parcel.writeString(getStepsThumbnailURL());
    }
    ////
}
