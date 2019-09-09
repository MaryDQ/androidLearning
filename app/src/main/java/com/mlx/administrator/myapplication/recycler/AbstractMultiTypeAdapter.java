package com.mlx.administrator.myapplication.recycler;

import android.content.Context;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */
public abstract class AbstractMultiTypeAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final String TAG;
    protected final Context mContext;
    protected List mDatas;

    protected AbstractMultiTypeAdapter(Context mContext, List mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        this.TAG = getClass().getSimpleName();
    }

    public List getmDatas() {
        if (mDatas == null) {
            return new ArrayList<>();
        }
        return mDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = getLayoutIdByType(viewType);
        return ViewHolder.get(mContext, parent, layoutId);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        onBindViewHolder(holder, getItemViewType(position), mDatas.get(position), position);
    }

    @Override
    public abstract int getItemViewType(int position);

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (null != mDatas) {
            return mDatas.size();
        }
        return 0;
    }

    protected abstract void onBindViewHolder(ViewHolder holder, int type, Object data, int curPosition);

    /**
     * 子类需实现以下三个方法
     */

    protected abstract int getLayoutIdByType(int viewType);

}
