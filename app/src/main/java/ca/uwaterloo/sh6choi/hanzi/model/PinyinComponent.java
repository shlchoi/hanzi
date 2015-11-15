package ca.uwaterloo.sh6choi.hanzi.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Samson on 2015-09-23.
 */
public class PinyinComponent implements Parcelable, FlashcardItem {

    private static final Character[] INITIAL_ORDERING = new Character[]{'a', 'i', 'u', 'e', 'o', 'y'};
    private static final Character[] FINAL_ORDERING = new Character[]{'k', 's', 't', 'n', 'h', 'm', 'y', 'r', 'w', 'g', 'z', 'd', 'b', 'p'};

    @SerializedName("pinyin")
    private String mPinyin;

    @SerializedName("zhuyin")
    private String mZhuyin;

    @SerializedName("no_intial_form")
    private String mNoIntialForm;

    @SerializedName("alternate_form")
    private String mAlternateForm;

    @SerializedName("type")
    private String mType;

    public PinyinComponent(String pinyin, String zhuyin, String type) {
        mPinyin = pinyin;
        mZhuyin = zhuyin;
        mType = type;
    }

    public PinyinComponent(String pinyin, String zhuyin, String noIntialForm, String type) {
        mPinyin = pinyin;
        mZhuyin = zhuyin;
        mNoIntialForm = noIntialForm;
        mType = type;
    }

    public PinyinComponent(String pinyin, String zhuyin, String noIntialForm, String alternateForm, String type) {
        mPinyin = pinyin;
        mZhuyin = zhuyin;
        mNoIntialForm = noIntialForm;
        mAlternateForm = alternateForm;
        mType = type;
    }

    protected PinyinComponent(Parcel in) {
        mPinyin = in.readString();
        mZhuyin = in.readString();
        mNoIntialForm = in.readString();
        mAlternateForm = in.readString();
        mType = in.readString();
    }

    public static final Creator<PinyinComponent> CREATOR = new Creator<PinyinComponent>() {
        @Override
        public PinyinComponent createFromParcel(Parcel in) {
            return new PinyinComponent(in);
        }

        @Override
        public PinyinComponent[] newArray(int size) {
            return new PinyinComponent[size];
        }
    };

    public String getPinyin() {
        return mPinyin;
    }

    public String getZhuyin() {
        return mZhuyin;
    }

    public String getNoIntialForm() {
        return mNoIntialForm;
    }

    public String getAlternateForm() {
        return mAlternateForm;
    }

    public String getType() {
        return mType;
    }

    @Override
    public String getFlashcardText() {
        return getZhuyin();
    }

    @Override
    public String getFlashcardHint() {
        return getPinyin();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPinyin);
        dest.writeString(mZhuyin);
        dest.writeString(mNoIntialForm);
        dest.writeString(mAlternateForm);
        dest.writeString(mType);
    }
//
//    public static class CharacterComparator implements Comparator<PinyinComponent>
//    {
//        public int compare(PinyinComponent c1, PinyinComponent c2)
//        {
//            List<Character> initialOrder = Arrays.asList(INITIAL_ORDERING);
//            List<Character> finalOrder = Arrays.asList(FINAL_ORDERING);
//
//        }
//    }
}
