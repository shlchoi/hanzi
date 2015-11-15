package ca.uwaterloo.sh6choi.hanzi.fragments.pinyin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.sh6choi.hanzi.adapters.FlashcardAdapter;
import ca.uwaterloo.sh6choi.hanzi.fragments.DrawerFragment;
import ca.uwaterloo.sh6choi.hanzi.model.PinyinComponent;
import ca.uwaterloo.sh6choi.hanzi.presentation.PinyinPresenter;
import ca.uwaterloo.sh6choi.hanzi.R;
import ca.uwaterloo.sh6choi.hanzi.activities.MainActivity;

/**
 * Created by Samson on 2015-09-22.
 */
public class PinyinFlashcardsFragment extends Fragment implements DrawerFragment, PinyinPresenter.PinyinComponentView {

    private static final String TAG = PinyinFlashcardsFragment.class.getCanonicalName();
    public static final String FRAGMENT_TAG = MainActivity.TAG + ".fragment.pinyin.flashcards";

    private List<PinyinComponent> mPinyinComponentList;
    private PinyinPresenter mPresenter;

    private int mCurIndex = -1;

    private RecyclerView mFlashcardRecyclerView;
    private FlashcardAdapter<PinyinComponent> mFlashcardAdapter;

    public static PinyinFlashcardsFragment getInstance(Bundle args) {
        PinyinFlashcardsFragment fragment = new PinyinFlashcardsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View contentView = inflater.inflate(R.layout.fragment_pinyin_flashcard, container, false);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPinyinComponentList = new ArrayList<>();

        mFlashcardRecyclerView = (RecyclerView) view.findViewById(R.id.flashcard_recycler_view);
        mFlashcardAdapter = new FlashcardAdapter<>(R.layout.list_item_flashcard_character);

        ItemTouchHelper.Callback listItemTouchHelper = new ItemTouchHelper.Callback() {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.LEFT);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    mFlashcardAdapter.nextCard();
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(listItemTouchHelper);
        itemTouchHelper.attachToRecyclerView(mFlashcardRecyclerView);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        mFlashcardRecyclerView.setLayoutManager(manager);
        mFlashcardRecyclerView.setAdapter(mFlashcardAdapter);

        mPresenter  = new PinyinPresenter(getContext(), this);
        mPresenter.obtainComponents();
    }

    @Override
    public void refreshPinyinComponentList(List<PinyinComponent> pinyinComponentList) {
        mFlashcardAdapter.setFlashcardList(pinyinComponentList);
    }

    @Override
    public String getFragmentTag() {
        return FRAGMENT_TAG;
    }

    @Override
    public int getTitleStringResId() {
        return R.string.toolbar_title_flashcards;
    }

    @Override
    public boolean shouldAddToBackstack() {
        return false;
    }

    @Override
    public boolean shouldShowUp() {
        return true;
    }

    @Override
    public boolean onBackPressed() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setAction(MainActivity.ACTION_PINYIN);

        startActivity(intent);
        return true;
    }
}
