package com.mlx.administrator.myapplication.recycler;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */
public abstract class AbstractSimpleAdapter<T> extends AbstractMultiTypeAdapter {

    protected int mLayoutId;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private int selectPosition = -1;
    private List<String> mSelectList = new ArrayList<>();
    private boolean openStatus = false;
    private List<T> mSelectItemList = new ArrayList<>();

    public AbstractSimpleAdapter(Context context, List<T> datas, int layoutId) {
        super(context, datas);
        this.mLayoutId = layoutId;
    }

    public boolean isOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(boolean openStatus) {
        this.openStatus = openStatus;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    protected int getLayoutIdByType(int viewType) {
        return mLayoutId;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    protected void onBindViewHolder(final ViewHolder holder, int type, Object data, int position) {
        onBindViewHolder(holder, (T) data, position);
        final Object o = data;
        if (null != mOnItemClickListener) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onClickItem(o, holder.getAdapterPosition(), holder);
                }
            });
        }
        if (null != mOnItemLongClickListener) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemLongClickListener.onLongClickItem(o, holder.getAdapterPosition(), holder);
                    return false;
                }
            });
        }

    }

    /**
     * 子类需实现以下方法
     * @param holder item对应的holder
     * @param data item的数据源
     * @param curPosition   item对应的position
     */
    protected abstract void onBindViewHolder(ViewHolder holder, T data, int curPosition);

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    public void removeData(int position) {
        if (position < mDatas.size()) {
            mDatas.remove(position);
            notifyItemRemoved(position);
            notifyDataSetChanged();
        }
    }

    public void addData(int position, T t) {

        mDatas.add(position, t);
        notifyItemInserted(position);
    }

    public void resetData(List<T> datas){
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }


    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int position) {
        this.selectPosition = position;
    }

    public List<String> getSelectList() {
        if (mSelectList == null) {
            return new ArrayList<>();
        }
        return mSelectList;
    }

    public void setSelectList(String unid) {
        if (mSelectList.contains(unid)) {
            mSelectList.remove(unid);
        } else {
            mSelectList.add(unid);
        }
    }

    public List<T> getSelectItemList() {
        return mSelectItemList;
    }

    public void setSelectItemList(T t) {
        if (mSelectItemList.contains(t)) {
            mSelectItemList.remove(t);
        } else {
            mSelectItemList.add(t);
        }
    }

    public interface OnItemClickListener {
        /**
         * item的单击事件
         *
         * @param o          item的数据源
         * @param position   当前的position
         * @param viewHolder 当前item对应的holder
         */
        void onClickItem(Object o, int position, ViewHolder viewHolder);
    }

    public interface OnItemLongClickListener {
        /**
         * item的长按事件
         * @param o item的数据源
         * @param position  当前的position
         * @param viewHolder    当前item对应的holder
         */
        void onLongClickItem(Object o, int position, ViewHolder viewHolder);
    }

}
