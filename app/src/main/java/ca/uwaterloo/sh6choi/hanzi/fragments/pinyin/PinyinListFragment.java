package ca.uwaterloo.sh6choi.hanzi.fragments.pinyin;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ca.uwaterloo.sh6choi.hanzi.adapters.PinyinAdapter;
import ca.uwaterloo.sh6choi.hanzi.fragments.DrawerFragment;
import ca.uwaterloo.sh6choi.hanzi.model.PinyinComponent;
import ca.uwaterloo.sh6choi.hanzi.presentation.PinyinPresenter;
import ca.uwaterloo.sh6choi.hanzi.R;
import ca.uwaterloo.sh6choi.hanzi.activities.MainActivity;
import ca.uwaterloo.sh6choi.hanzi.services.ZhuyinWebIntentService;

/**
 * Created by Samson on 2015-10-26.
 */
public class PinyinListFragment extends Fragment implements DrawerFragment, PinyinPresenter.PinyinComponentView,
    SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = PinyinFragment.class.getCanonicalName();
    public static final String FRAGMENT_TAG = MainActivity.TAG + ".fragment.pinyin.list";

    private SwipeRefreshLayout mPinyinSwipeRefreshLayout;
    private RecyclerView mPinyinRecyclerView;
    private PinyinAdapter mPinyinAdapter;

    private PinyinPresenter mPresenter;

    private BroadcastReceiver mSuccessReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mPresenter != null) {
                mPinyinSwipeRefreshLayout.setRefreshing(false);
                mPresenter.obtainComponents();
            }
        }
    };

    public static PinyinListFragment getInstance(Bundle args) {
        PinyinListFragment fragment = new PinyinListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View contentView = inflater.inflate(R.layout.fragment_pinyin_list, container, false);
        setHasOptionsMenu(true);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPinyinSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.zhuyin_swipe_refresh);
        mPinyinSwipeRefreshLayout.setOnRefreshListener(this);
        mPinyinSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        mPinyinRecyclerView = (RecyclerView) view.findViewById(R.id.zhuyin_recycler_view);
        mPinyinRecyclerView.setHasFixedSize(true);
        mPinyinRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        mPinyinAdapter = new PinyinAdapter();
        mPinyinRecyclerView.setAdapter(mPinyinAdapter);

        mPresenter = new PinyinPresenter(getContext(), this);
        mPresenter.obtainComponents();
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter successFilter = new IntentFilter();
        successFilter.addAction(ZhuyinWebIntentService.ACTION_SUCCESS);
        getContext().registerReceiver(mSuccessReceiver, successFilter);
    }

    @Override
    public void onPause() {
        getContext().unregisterReceiver(mSuccessReceiver);
        super.onPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_refresh_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                mPinyinSwipeRefreshLayout.setRefreshing(true);
                onRefresh();
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRefresh() {
        Intent zhuyin = new Intent(getContext(), ZhuyinWebIntentService.class);
        getContext().startService(zhuyin);
    }

    @Override
    public void refreshPinyinComponentList(List<PinyinComponent> pinyinComponentList) {
        mPinyinAdapter.setPinyinComponentList(pinyinComponentList);
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
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setAction(MainActivity.ACTION_PINYIN);

        startActivity(intent);
        return true;
    }
}
