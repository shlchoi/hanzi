package ca.uwaterloo.sh6choi.hanzi.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.sh6choi.hanzi.fragments.DrawerFragment;
import ca.uwaterloo.sh6choi.hanzi.fragments.numbers.ChineseNumbersFragment;
import ca.uwaterloo.sh6choi.hanzi.fragments.numbers.ChineseNumbersListFragment;
import ca.uwaterloo.sh6choi.hanzi.fragments.numbers.NumbersFragment;
import ca.uwaterloo.sh6choi.hanzi.fragments.pinyin.PinyinListFragment;
import ca.uwaterloo.sh6choi.hanzi.model.PinyinComponent;
import ca.uwaterloo.sh6choi.hanzi.services.NumberWebIntentService;
import ca.uwaterloo.sh6choi.hanzi.utils.NumberUtils;
import ca.uwaterloo.sh6choi.hanzi.views.DrawerMenuAdapter;
import ca.uwaterloo.sh6choi.hanzi.views.IDrawerMenuItem;
import ca.uwaterloo.sh6choi.hanzi.views.ISlidingPane;
import ca.uwaterloo.sh6choi.hanzi.fragments.pinyin.PinyinComponentFragment;
import ca.uwaterloo.sh6choi.hanzi.fragments.pinyin.PinyinFragment;
import ca.uwaterloo.sh6choi.hanzi.views.HanziMenuItem;
import ca.uwaterloo.sh6choi.hanzi.fragments.pinyin.PinyinFlashcardsFragment;
import ca.uwaterloo.sh6choi.hanzi.utils.KeyboardUtils;
import ca.uwaterloo.sh6choi.hanzi.views.NavigationDrawerLayout;
import ca.uwaterloo.sh6choi.hanzi.R;

public class MainActivity extends AppCompatActivity implements ViewTreeObserver.OnGlobalLayoutListener {

    public static final String TAG = MainActivity.class.getCanonicalName();

    public static final String ACTION_PINYIN = TAG + ".action.pinyin";
    public static final String ACTION_PINYIN_LOOKUP = ACTION_PINYIN + ".lookup";
    public static final String ACTION_PINYIN_COMPONENT = ACTION_PINYIN + ".component";
    public static final String ACTION_FLASHCARDS = ACTION_PINYIN + ".flashcards";

    public static final String ACTION_NUMBERS = TAG + ".action.numbers";
    public static final String ACTION_NUMBERS_LOOKUP = ACTION_NUMBERS + ".lookup";
    public static final String ACTION_CHINESE_NUMBERS = ACTION_NUMBERS + ".chinese";

    public static final String ACTION_SPEECH = TAG + ".action.speech";

    public static final String EXTRA_COMPONENT = TAG +".extra.character";


    private NavigationDrawerLayout mDrawerLayout;
    private DrawerLayout mWrappedDrawerLayout;

    private ActionBarDrawerToggle mDrawerToggle;

    private Toolbar mToolbar;
    private TextView mToolbarActionTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NumberUtils.refreshMap(this);

        setContentView(R.layout.activity_main);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        mDrawerLayout = (NavigationDrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setMenuAdapter(new DrawerMenuAdapter(this, setupMenu()));


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbarActionTextView = (TextView) mToolbar.findViewById(R.id.toolbar_action_tv);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME);

        mWrappedDrawerLayout = new ISlidingPane.DrawerLayoutWrapper(this, mDrawerLayout) {
            @Override
            public void openDrawer(int gravity) {
                KeyboardUtils.hideKeyboard(MainActivity.this);
                super.openDrawer(gravity);
            }
        };

        mDrawerToggle = new ActionBarDrawerToggle(this, mWrappedDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mDrawerLayout.setDrawerEnabled(true);

        if (getIntent() == null || TextUtils.isEmpty(getIntent().getAction())) {
            Intent start = new Intent(ACTION_PINYIN);
            startActivity(start);
        } else {
            handleAction(getIntent());
        }
    }

    private List<IDrawerMenuItem> setupMenu() {
        List<IDrawerMenuItem> menuList = new ArrayList<>();
        menuList.add(HanziMenuItem.PINYIN);
        menuList.add(HanziMenuItem.NUMBERS);
        return menuList;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (!TextUtils.isEmpty(intent.getAction())) {
            handleAction(intent);
            setIntent(intent);
        }
    }

    private void handleAction(Intent intent) {
        String action = intent.getAction();

        if (TextUtils.equals(action, ACTION_PINYIN)) {
            onPinyin();
        } else if (TextUtils.equals(action, ACTION_PINYIN_LOOKUP)) {
            onPinyinLookup();
        } else if (TextUtils.equals(action, ACTION_PINYIN_COMPONENT)) {
            onPinyinComponent(intent);
        } else if (TextUtils.equals(action, ACTION_FLASHCARDS)) {
            onFlashcards();
        } else if (TextUtils.equals(action, ACTION_NUMBERS)) {
            onNumbers();
        } else if (TextUtils.equals(action, ACTION_NUMBERS_LOOKUP)) {
            onNumbersLookup();
        } else if (TextUtils.equals(action, ACTION_CHINESE_NUMBERS)) {
            onChineseNumbers();
        } else {
            onPinyin();
        }
    }

    private void onPinyin() {
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof PinyinFragment) {
            mDrawerLayout.closePane();
            return;
        }
        PinyinFragment fragment = PinyinFragment.getInstance(new Bundle());
        swapFragment(fragment);
    }

    private void onPinyinLookup() {
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof PinyinListFragment) {
            mDrawerLayout.closePane();
            return;
        }
        PinyinListFragment fragment = PinyinListFragment.getInstance(new Bundle());
        swapFragment(fragment);
    }

    private void onPinyinComponent(Intent intent) {
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof PinyinComponentFragment) {
            mDrawerLayout.closePane();
            return;
        }

        PinyinComponent character = intent.getParcelableExtra(EXTRA_COMPONENT);

        Bundle args = new Bundle();
        args.putParcelable(PinyinComponentFragment.ARG_PINYIN_COMPONENT, character);

        PinyinComponentFragment fragment = PinyinComponentFragment.getInstance(args);
        swapFragment(fragment);
    }

    private void onFlashcards() {
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof PinyinFlashcardsFragment) {
            mDrawerLayout.closePane();
            return;
        }
        PinyinFlashcardsFragment fragment = PinyinFlashcardsFragment.getInstance(new Bundle());
        swapFragment(fragment);
    }

    private void onNumbers() {
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof NumbersFragment) {
            mDrawerLayout.closePane();
            return;
        }
        NumbersFragment fragment = NumbersFragment.getInstance(new Bundle());
        swapFragment(fragment);
    }

    private void onNumbersLookup() {
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof ChineseNumbersListFragment) {
            mDrawerLayout.closePane();
            return;
        }
        ChineseNumbersListFragment fragment = ChineseNumbersListFragment.getInstance(new Bundle());
        swapFragment(fragment);
    }

    private void onChineseNumbers() {
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof ChineseNumbersFragment) {
            mDrawerLayout.closePane();
            return;
        }
        ChineseNumbersFragment fragment = ChineseNumbersFragment.getInstance(new Bundle());
        swapFragment(fragment);
    }

    private <T extends Fragment & DrawerFragment> void swapFragment(T fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);

        if (fragment.shouldAddToBackstack()) {
            transaction.addToBackStack(fragment.getFragmentTag());
        }

        transaction.replace(R.id.fragment_container, fragment, fragment.getFragmentTag());
        transaction.commit();

        updateToolbar(fragment);

        final View fragmentContainer = findViewById(R.id.fragment_container);

        fragmentContainer.getViewTreeObserver().addOnGlobalLayoutListener(this);

        refreshToolbar(fragment.shouldShowUp(), fragment.getTitleStringResId());
    }

    private <T extends Fragment & DrawerFragment> void updateToolbar(T fragment) {
        mToolbarActionTextView.setText(fragment.getTitleStringResId());
    }

    @Override
    public void onBackPressed() {
        KeyboardUtils.hideKeyboard(this);
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (currentFragment instanceof DrawerFragment) {
            if (!((DrawerFragment) currentFragment).onBackPressed()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK && data != null) {
//            if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof PronunciationFragment) {
//                Intent intent = new Intent();
//                intent.setAction(ACTION_SPEECH);
//                intent.putExtra(PronunciationFragment.EXTRA_RESULTS, data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS));
//
//                sendBroadcast(intent);
//            }
//        }
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    public void onGlobalLayout() {
        final View fragmentContainer = findViewById(R.id.fragment_container);
        mDrawerLayout.closePane();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            fragmentContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        else
            fragmentContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerToggle != null) {
            mDrawerToggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void refreshToolbar(boolean shouldShowUp, int titleResId) {
        if (getSupportActionBar() == null)
            return;

        if (shouldShowUp) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                    | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_HOME);
            mDrawerLayout.setDrawerEnabled(false);
            setTitle(titleResId);

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } else {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                    | ActionBar.DISPLAY_SHOW_HOME);
            mDrawerLayout.setDrawerEnabled(true);

            mDrawerToggle = new ActionBarDrawerToggle(this, mWrappedDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

            mDrawerLayout.addDrawerListener(mDrawerToggle);
            mDrawerToggle.syncState();

            mDrawerToggle.setToolbarNavigationClickListener(null);
        }
    }
}
