package com.iptv.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iptv.demo.R;

import java.util.List;

public final class SourceListAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mSourceList;

    public SourceListAdapter(Context context, List<String> sourceList) {
        super();

        mContext = context;
        mSourceList = sourceList;
    }

    @Override
    public int getCount() {
        return mSourceList.size();
    }

    @Override
    public Object getItem(int index) {
        return mSourceList.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_source, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.mSourceDescView = (TextView)convertView.findViewById(R.id.id_source_desc);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        /**
         * FIXME：应该基于url定义源的描述， 现在只能是用索引来描述
         */
        viewHolder.mSourceDescView.setText("源" + index);

        return convertView;
    }

    private class ViewHolder {
        TextView mSourceDescView;
    }
}
