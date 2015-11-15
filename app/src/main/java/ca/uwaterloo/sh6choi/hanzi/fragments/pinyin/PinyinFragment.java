package ca.uwaterloo.sh6choi.hanzi.fragments.pinyin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ca.uwaterloo.sh6choi.hanzi.R;
import ca.uwaterloo.sh6choi.hanzi.activities.MainActivity;
import ca.uwaterloo.sh6choi.hanzi.fragments.DrawerFragment;


/**
 * Created by Samson on 2015-09-25.
 */
public class PinyinFragment extends Fragment implements DrawerFragment, View.OnClickListener {

    private static final String TAG = PinyinFragment.class.getCanonicalName();
    public static final String FRAGMENT_TAG = MainActivity.TAG + ".fragment.pinyin";

    private Button mLookupButton;
    private Button mFlashcardButton;

    public static PinyinFragment getInstance(Bundle args) {
        PinyinFragment fragment = new PinyinFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View contentView = inflater.inflate(R.layout.fragment_pinyin, container, false);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLookupButton = (Button) view.findViewById(R.id.lookup_button);
        mFlashcardButton = (Button) view.findViewById(R.id.flashcards_button);

        mLookupButton.setOnClickListener(this);
        mFlashcardButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lookup_button:
                onLookupButtonClicked();
                break;
            case R.id.flashcards_button:
                onFlashcardsButtonClicked();
                break;
        }
    }

    private void onLookupButtonClicked() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setAction(MainActivity.ACTION_PINYIN_LOOKUP);
        startActivity(intent);
    }

    private void onFlashcardsButtonClicked() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setAction(MainActivity.ACTION_FLASHCARDS);
        startActivity(intent);
    }

    @Override
    public String getFragmentTag() {
        return FRAGMENT_TAG;
    }

    @Override
    public int getTitleStringResId() {
        return R.string.nav_menu_pinyin;
    }

    @Override
    public boolean shouldShowUp() {
        return false;
    }

    @Override
    public boolean shouldAddToBackstack() {
        return false;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
