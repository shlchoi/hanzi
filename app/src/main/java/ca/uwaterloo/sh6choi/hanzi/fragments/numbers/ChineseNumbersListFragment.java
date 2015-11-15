package ca.uwaterloo.sh6choi.hanzi.fragments.numbers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.sh6choi.hanzi.activities.MainActivity;
import ca.uwaterloo.sh6choi.hanzi.fragments.DrawerFragment;
import ca.uwaterloo.sh6choi.hanzi.R;
import ca.uwaterloo.sh6choi.hanzi.adapters.NumbersAdapter;
import ca.uwaterloo.sh6choi.hanzi.services.NumberWebIntentService;
import ca.uwaterloo.sh6choi.hanzi.services.ZhuyinWebIntentService;
import ca.uwaterloo.sh6choi.hanzi.utils.NumberUtils;

/**
 * Created by Samson on 2015-11-03.
 */
public class ChineseNumbersListFragment extends Fragment implements DrawerFragment, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = ChineseNumbersListFragment.class.getCanonicalName();
    public static final String FRAGMENT_TAG = MainActivity.TAG + ".fragment.sino_korean_numbers_list";

    private SwipeRefreshLayout mNumbersSwipeRefreshLayout;
    private RecyclerView mListRecyclerView;
    private NumbersAdapter mAdapter;
    private BroadcastReceiver mSuccessReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mNumbersSwipeRefreshLayout.setRefreshing(false);
            mAdapter.notifyDataSetChanged();
        }
    };

    public static ChineseNumbersListFragment getInstance(Bundle args) {
        ChineseNumbersListFragment fragment = new ChineseNumbersListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(false);
        return inflater.inflate(R.layout.fragment_chinese_numbers_lookup, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNumbersSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.number_swipe_refresh);
        mNumbersSwipeRefreshLayout.setOnRefreshListener(this);
        mNumbersSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        mListRecyclerView = (RecyclerView) view.findViewById(R.id.number_recycler_view);
        mListRecyclerView.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        manager.setSpanCount(3);
        GridLayoutManager.SpanSizeLookup lookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position % 2 == 1) {
                    return 2;
                }
                return 1;
            }
        };
        manager.setSpanSizeLookup(lookup);
        mListRecyclerView.setLayoutManager(manager);

        List<String> numbers = getNumbers(20);

        mAdapter = new NumbersAdapter(numbers);
        mListRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter successFilter = new IntentFilter();
        successFilter.addAction(NumberWebIntentService.ACTION_SUCCESS);
        getContext().registerReceiver(mSuccessReceiver, successFilter);
    }

    @Override
    public void onPause() {
        getContext().unregisterReceiver(mSuccessReceiver);
        super.onPause();
    }

    @Override
    public void onRefresh() {
        Intent intent = new Intent(getContext(), NumberWebIntentService.class);
        getContext().startService(intent);
    }

    private List<String> getNumbers(int maxNum) {
        List<String> numbers = new ArrayList<>();
        for (int i = 1; i <= maxNum; i ++) {
            numbers.add(NumberUtils.getTraditionalChineseNumber(i));
        }
        return numbers;
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
