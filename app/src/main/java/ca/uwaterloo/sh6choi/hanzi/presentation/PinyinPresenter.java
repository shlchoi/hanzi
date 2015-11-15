package ca.uwaterloo.sh6choi.hanzi.presentation;

import android.content.Context;

import java.util.List;

import ca.uwaterloo.sh6choi.hanzi.database.DatabaseRequestCallback;
import ca.uwaterloo.sh6choi.hanzi.database.PinyinComponentDataSource;
import ca.uwaterloo.sh6choi.hanzi.model.PinyinComponent;

/**
 * Created by Samson on 2015-10-23.
 */
public class PinyinPresenter {
    private final PinyinComponentView mView;

    private PinyinComponentDataSource mDataSource;

    public PinyinPresenter(Context context, PinyinComponentView view) {
        mDataSource = new PinyinComponentDataSource(context);
        mView = view;
    }

    public void obtainComponents() {
        mDataSource.open();
        mDataSource.queryPinyin(new DatabaseRequestCallback<List<PinyinComponent>>() {
            @Override
            public void processResults(List<PinyinComponent> results) {
                mView.refreshPinyinComponentList(results);
                mDataSource.close();
            }
        });
    }

    public interface PinyinComponentView {
        void refreshPinyinComponentList(List<PinyinComponent> pinyinComponentList);
    }
}
