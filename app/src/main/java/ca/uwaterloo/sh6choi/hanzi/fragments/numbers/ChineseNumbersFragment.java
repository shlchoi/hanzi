package ca.uwaterloo.sh6choi.hanzi.fragments.numbers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;
import java.util.Random;

import ca.uwaterloo.sh6choi.hanzi.R;
import ca.uwaterloo.sh6choi.hanzi.activities.MainActivity;
import ca.uwaterloo.sh6choi.hanzi.fragments.DrawerFragment;
import ca.uwaterloo.sh6choi.hanzi.utils.NumberUtils;

/**
 * Created by Samson on 2015-11-02.
 */
public class ChineseNumbersFragment extends Fragment implements DrawerFragment, View.OnClickListener {
    private static final String TAG = ChineseNumbersFragment.class.getCanonicalName();
    private static final String FRAGMENT_TAG = MainActivity.TAG + ".fragment.numbers.sino_korean";

    private final int MAX_INT = 100;

    private int mCurNumber = -1;

    private TextView mNumberTextView;
    private EditText mInputEditText;
    private Button mCheckButton;

    public static ChineseNumbersFragment getInstance(Bundle args) {
        ChineseNumbersFragment fragment = new ChineseNumbersFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View contentView = inflater.inflate(R.layout.fragment_chinese_numbers, container, false);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNumberTextView = (TextView) view.findViewById(R.id.number_text_view);
        mNumberTextView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mInputEditText = (EditText) view.findViewById(R.id.input_edit_text);
        mInputEditText.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mCheckButton = (Button) view.findViewById(R.id.check_button);
        mCheckButton.setOnClickListener(this);

        switchNumber();
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.check_button:
                String answer = NumberUtils.getTraditionalChineseNumber(mCurNumber);

                if (TextUtils.equals(mInputEditText.getText().toString().replace(" ", ""), answer)) {
                    switchNumber();
                } else {
                    mInputEditText.setError("Incorrect");
                }
                break;
        }
    }

    private void switchNumber() {
        Random random = new Random(new Date().getTime());

        int nextInt;
        do {
            nextInt = random.nextInt(MAX_INT + 1);
        } while (nextInt == mCurNumber);

        mCurNumber = nextInt;

        mNumberTextView.setText(Integer.toString(mCurNumber));

        mInputEditText.setText("");
        mInputEditText.setError(null);
    }

    @Override
    public String getFragmentTag() {
        return FRAGMENT_TAG;
    }

    @Override
    public int getTitleStringResId() {
        return R.string.nav_menu_numbers;
    }

    @Override
    public boolean shouldShowUp() {
        return true;
    }

    @Override
    public boolean shouldAddToBackstack() {
        return false;
    }

    @Override
    public boolean onBackPressed() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setAction(MainActivity.ACTION_NUMBERS);

        startActivity(intent);
        return true;
    }

}
