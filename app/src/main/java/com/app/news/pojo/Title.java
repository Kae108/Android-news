package com.app.news.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

public class Title implements Parcelable {
    private String cnTitle;
    private String pyTitle;
    private int startPosition;
    private int endPosition;

    protected Title(Parcel in) {
        cnTitle = in.readString();
        pyTitle = in.readString();
        startPosition = in.readInt();
        endPosition = in.readInt();
    }

    public static final Creator<Title> CREATOR = new Creator<Title>() {
        @Override
        public Title createFromParcel(Parcel in) {
            return new Title(in);
        }

        @Override
        public Title[] newArray(int size) {
            return new Title[size];
        }
    };

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }


    public String getCnTitle() {
        return cnTitle;
    }

    public void setCnTitle(String cnTitle) {
        this.cnTitle = cnTitle;
    }

    public String getPyTitle() {
        return pyTitle;
    }

    public void setPyTitle(String pyTitle) {
        this.pyTitle = pyTitle;
    }

    public Title() {
    }

    public Title(String cnTitle, String pyTitle, int startPosition, int endPosition) {
        this.cnTitle = cnTitle;
        this.pyTitle = pyTitle;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public Title(String cnTitle, String pyTitle) {
        this.cnTitle = cnTitle;
        this.pyTitle = pyTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Title title = (Title) o;
        return Objects.equals(cnTitle, title.cnTitle) && Objects.equals(pyTitle, title.pyTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cnTitle, pyTitle);
    }

    @Override
    public String toString() {
        return "Title{" +
                "cnTitle='" + cnTitle + '\'' +
                ", pyTitle='" + pyTitle + '\'' +
                ", startPosition=" + startPosition +
                ", endPosition=" + endPosition +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(cnTitle);
        dest.writeString(pyTitle);
        dest.writeInt(startPosition);
        dest.writeInt(endPosition);
    }
}
