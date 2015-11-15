package ca.uwaterloo.sh6choi.hanzi.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ca.uwaterloo.sh6choi.hanzi.R;

/**
 * Created by Samson on 2015-11-03.
 */
public class NumbersAdapter extends RecyclerView.Adapter<ListItemViewHolder> implements ListItemViewHolder.OnItemClickListener {

    private List<String> mNumberList;
    private OnItemClickListener mOnItemClickListener;

    public NumbersAdapter(List<String> numberList) {
        mNumberList = numberList;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_list, parent, false);
        ListItemViewHolder viewHolder = new ListItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        if (position % 2 == 1) {
            holder.getTextView().setText(mNumberList.get(position / 2));

            holder.itemView.setTag(mNumberList.get(position / 2));
            holder.setOnItemClickListener(this);
        } else {
            holder.getTextView().setText(Integer.toString((position / 2) + 1));
        }
    }

    @Override
    public int getItemCount() {
        return mNumberList.size() * 2;
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

    public interface OnItemClickListener {
        void onItemClick(View view);
    }
}
