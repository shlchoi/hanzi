package ca.uwaterloo.sh6choi.hanzi.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.sh6choi.hanzi.model.PinyinComponent;
import ca.uwaterloo.sh6choi.hanzi.R;


/**
 * Created by Samson on 2015-09-25.
 */
public class PinyinAdapter extends RecyclerView.Adapter<PinyinViewHolder> implements PinyinViewHolder.OnItemClickListener {
    private List<PinyinComponent> mPinyinComponentList;

    private OnItemClickListener mOnItemClickListener;

    public PinyinAdapter() {
        mPinyinComponentList = new ArrayList<>();
    }

    @Override
    public PinyinViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pinyin, parent, false);
        PinyinViewHolder viewHolder = new PinyinViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PinyinViewHolder holder, int position) {
        if (position % 2 == 0) {
            holder.getTextView().setText(mPinyinComponentList.get(position / 2).getPinyin());
            holder.itemView.setTag(mPinyinComponentList.get(position / 2));
            holder.setOnItemClickListener(this);
        } else {
            holder.getTextView().setText(mPinyinComponentList.get(position / 2).getZhuyin());
            holder.itemView.setTag(mPinyinComponentList.get(position / 2));
            holder.setOnItemClickListener(this);
        }

    }

    @Override
    public int getItemCount() {
        return mPinyinComponentList.size() * 2;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void onItemClick(View view) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(view);
        }
    }

    public void setPinyinComponentList(List<PinyinComponent> pinyinComponentList) {
        mPinyinComponentList = pinyinComponentList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view);
    }
}
