package ca.uwaterloo.sh6choi.hanzi.views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import ca.uwaterloo.sh6choi.hanzi.R;
import ca.uwaterloo.sh6choi.hanzi.activities.MainActivity;

/**
 * Created by Samson on 2015-09-22.
 */
public class HanziDrawerLayout extends NavigationDrawerLayout {

    public HanziDrawerLayout(Context context) {
        super(context);
    }

    public HanziDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HanziDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void initializeMenu() {

        mDrawerMenu = (ListView) findViewById(R.id.navigation_drawer);

        if (mMenuAdapter == null) {
            mMenuAdapter = new HanziDrawerMenuAdapter(getContext(), new ArrayList<IDrawerMenuItem>());
        }
        mDrawerMenu.setAdapter(mMenuAdapter);

        setupClickListener();
    }

    @Override
    protected void setupClickListener() {
        mDrawerMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IDrawerMenuItem itemByPosition = mMenuAdapter.getItem(position);
                startNavigation(itemByPosition);
            }
        });
    }

    @Override
    protected void startNavigation(IDrawerMenuItem navigateToItem) {
        if (navigateToItem == null || !(navigateToItem instanceof HanziMenuItem)) {
            return;
        }

        Intent intent;
        switch ((HanziMenuItem) navigateToItem) {
            case PINYIN:
            default:
                intent = new Intent(getContext(), MainActivity.class);
                intent.setAction(MainActivity.ACTION_PINYIN);
                break;
            case NUMBERS:
                intent = new Intent(getContext(), MainActivity.class);
                intent.setAction(MainActivity.ACTION_NUMBERS);
                break;
        }
        getContext().startActivity(intent);
    }
}
