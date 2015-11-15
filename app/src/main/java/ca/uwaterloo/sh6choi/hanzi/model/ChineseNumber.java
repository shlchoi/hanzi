package ca.uwaterloo.sh6choi.hanzi.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

/**
 * Created by Samson on 2015-11-10.
 */
public class ChineseNumber implements Parcelable {
    @SerializedName("number")
    private int mNumber;

    @SerializedName("traditional")
    private String mTraditional;

    @SerializedName("simplified")
    private String mSimplified;

    @SerializedName("pinyin")
    private String mPinyin;

    public ChineseNumber(int number, String traditional, String pinyin) {
        mNumber = number;
        mTraditional = traditional;
        mPinyin = pinyin;
    }

    public ChineseNumber(int number, String traditional, String simplified, String pinyin) {
        mNumber = number;
        mTraditional = traditional;
        mSimplified = simplified;
        mPinyin = pinyin;
    }

    protected ChineseNumber(Parcel in) {
        mNumber = in.readInt();
        mTraditional = in.readString();
        mSimplified = in.readString();
        mPinyin = in.readString();
    }

    public static final Creator<ChineseNumber> CREATOR = new Creator<ChineseNumber>() {
        @Override
        public ChineseNumber createFromParcel(Parcel in) {
            return new ChineseNumber(in);
        }

        @Override
        public ChineseNumber[] newArray(int size) {
            return new ChineseNumber[size];
        }
    };

    public int getNumber() {
        return mNumber;
    }

    public String getTraditional() {
        return mTraditional;
    }

    public String getSimplified() {
        if (TextUtils.isEmpty(mSimplified) || TextUtils.equals(mSimplified, "null")) {
            return mTraditional;
        } else {
            return mSimplified;
        }
    }

    public String getPinyin() {
        return mPinyin;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mNumber);
        dest.writeString(mTraditional);
        dest.writeString(mSimplified);
        dest.writeString(mPinyin);
    }

    public static class NumberComparator implements Comparator<ChineseNumber>
    {
        public int compare(ChineseNumber c1, ChineseNumber c2)
        {
            return c1.getNumber() - c2.getNumber();
        }
    }
}
