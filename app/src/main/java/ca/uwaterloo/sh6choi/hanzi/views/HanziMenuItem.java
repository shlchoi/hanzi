package ca.uwaterloo.sh6choi.hanzi.views;


import ca.uwaterloo.sh6choi.hanzi.R;

/**
 * Created by Samson on 2015-09-22.
 */
public enum HanziMenuItem implements IDrawerMenuItem {
    PINYIN(R.string.nav_menu_pinyin),
    NUMBERS(R.string.nav_menu_numbers);

    private int mStringResId;

    HanziMenuItem(int stringResId) {
        mStringResId = stringResId;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.list_item_nav_menu;
    }

    @Override
    public int getStringResId() {
        return mStringResId;
    }

    @Override
    public int getDrawableResId() {
        return 0;
    }
}
