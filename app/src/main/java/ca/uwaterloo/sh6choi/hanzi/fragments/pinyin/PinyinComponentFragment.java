package ca.uwaterloo.sh6choi.hanzi.fragments.pinyin;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ca.uwaterloo.sh6choi.hanzi.fragments.DrawerFragment;
import ca.uwaterloo.sh6choi.hanzi.model.PinyinComponent;
import ca.uwaterloo.sh6choi.hanzi.R;
import ca.uwaterloo.sh6choi.hanzi.services.TextToSpeechService;
import ca.uwaterloo.sh6choi.hanzi.activities.MainActivity;

/**
 * Created by Samson on 2015-09-25.
 */
public class PinyinComponentFragment extends Fragment implements DrawerFragment, View.OnClickListener,
        TextToSpeechService.OnInitializedListener {

    private static final String TAG = PinyinComponentFragment.class.getCanonicalName();
    public static final String FRAGMENT_TAG = MainActivity.TAG + ".fragment.pinyin.component";

    public static final String ARG_PINYIN_COMPONENT = TAG + ".arg.character";

    private PinyinComponent mPinyinComponent;

    private TextView mCharacterTextView;
    private TextView mNameTextView;
    private FloatingActionButton mPlayFab;

    private TextToSpeechService mTextToSpeechService;
    private boolean mBound;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            if (service != null) {
                TextToSpeechService.TextToSpeechBinder binder = (TextToSpeechService.TextToSpeechBinder) service;
                mTextToSpeechService = binder.getService();
                mTextToSpeechService.setOnInitializedListener(PinyinComponentFragment.this);

                mBound = true;
                mPlayFab.setEnabled(mTextToSpeechService.isInitialized());
            } else {
                mBound = false;
                mPlayFab.setEnabled(false);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            mBound = false;
            mTextToSpeechService = null;
        }
    };

    public static PinyinComponentFragment getInstance(Bundle args) {
        PinyinComponentFragment fragment = new PinyinComponentFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View contentView = inflater.inflate(R.layout.fragment_pinyin_component, container, false);

        Bundle args = getArguments();

        if (args.containsKey(ARG_PINYIN_COMPONENT)) {
            mPinyinComponent = args.getParcelable(ARG_PINYIN_COMPONENT);
        }

        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCharacterTextView = (TextView) view.findViewById(R.id.zhuyin_text_view);
        mCharacterTextView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mCharacterTextView.setText(mPinyinComponent.getZhuyin());

        mNameTextView = (TextView) view.findViewById(R.id.pinyin_text_view);
        mNameTextView.setText(mPinyinComponent.getFlashcardHint());

        mPlayFab = (FloatingActionButton) view.findViewById(R.id.play_fab);
        mPlayFab.setOnClickListener(this);
        mPlayFab.setEnabled(false);

        Intent intent = new Intent(getActivity(), TextToSpeechService.class);
        getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onInitialized() {
        mPlayFab.setEnabled(true);
    }

    @Override
    public void onError() {
        mPlayFab.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_fab:
                if (mBound) {
                    mTextToSpeechService.playSound(mPinyinComponent.getZhuyin());
                }
                break;
        }
    }

    @Override
    public String getFragmentTag() {
        return FRAGMENT_TAG;
    }

    @Override
    public int getTitleStringResId() {
        return R.string.toolbar_title_character_lookup;
    }

    @Override
    public boolean shouldShowUp() {
        return true;
    }

    @Override
    public boolean shouldAddToBackstack() {
        return true;
    }

    @Override
    public boolean onBackPressed() {
        getActivity().getSupportFragmentManager().popBackStack();
        return true;
    }
}
