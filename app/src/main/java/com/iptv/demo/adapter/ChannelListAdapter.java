package com.iptv.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iptv.demo.R;
import com.iptv.channel.Channel;

import java.util.List;

public final class ChannelListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Channel> mChannelList;

    public ChannelListAdapter(Context context, List<Channel> channelList) {
        super();

        mContext = context;
        mChannelList = channelList;
    }

    @Override
    public int getCount() {
        return mChannelList.size();
    }

    @Override
    public Object getItem(int index) {
        return mChannelList.get(index);
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
                    .inflate(R.layout.item_channel, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.mLogoImageView = (ImageView)convertView.findViewById(R.id.id_channel_logo);
            viewHolder.mChannelNameView = (TextView)convertView.findViewById(R.id.id_channel_name);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Channel channel = mChannelList.get(index);

        /**
         * FIXME：获取频道对应的台标
         */
        viewHolder.mLogoImageView.setVisibility(View.GONE);
        viewHolder.mChannelNameView.setText(channel.getName());

        return convertView;
    }

    private class ViewHolder {
        ImageView mLogoImageView;
        TextView mChannelNameView;
    }
}
