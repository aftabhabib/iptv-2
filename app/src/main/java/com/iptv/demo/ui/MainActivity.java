package com.iptv.demo.ui;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.HandlerThread;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.iptv.demo.R;
import com.iptv.demo.adapter.ChannelListAdapter;
import com.source.BaseClient;
import com.source.Channel;
import com.source.ChannelGroup;
import com.source.GroupInfo;
import com.source.firetv.FireTVClient;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    private SurfaceView mScreenView;
    private LinearLayout mChannelGroup;
    private ListView mSourceListView;

    private MediaPlayer mPlayer;

    private HandlerThread mSourceThread;
    private BaseClient mSource;

    private List<ChannelGroup> mChannelGroupList;
    private int mGroupIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 隐藏虚拟按键
         */
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);
        /**
         * 布局
         */
        setContentView(R.layout.activity_main);

        initView();

        loadChannels();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        /**
         * 显示屏幕
         */
        mScreenView = (SurfaceView)findViewById(R.id.id_screen);
        mScreenView.setVisibility(View.VISIBLE);
        mScreenView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * 显示频道表、节目源
                 */
                if (mChannelGroup.getVisibility() == View.GONE) {
                    mChannelGroup.setVisibility(View.VISIBLE);

                    /**
                     * FIXME： 10秒后自动隐藏
                     */
                }
            }
        });

        /**
         * 隐藏频道表
         */
        mChannelGroup = (LinearLayout)findViewById(R.id.id_channel_group);
        mChannelGroup.setVisibility(View.GONE);

        /**
         * “上一组/下一组”按钮
         */
        ImageView prevGroupBtn = (ImageView)findViewById(R.id.id_prev_group);
        prevGroupBtn.setImageResource(R.mipmap.arrow_left);
        prevGroupBtn.setOnClickListener(mOnSwitchGroupListener);

        ImageView nextGroupBtn = (ImageView)findViewById(R.id.id_next_group);
        prevGroupBtn.setImageResource(R.mipmap.arrow_right);
        nextGroupBtn.setOnClickListener(mOnSwitchGroupListener);

        /**
         * 隐藏节目源
         */
        mSourceListView = (ListView)findViewById(R.id.id_source_list);
        mSourceListView.setVisibility(View.GONE);
    }

    private void loadChannels() {
        mSourceThread = new HandlerThread("iptv source");
        mSourceThread.start();

        mSource = new FireTVClient(this, mSourceThread.getLooper());
        mSource.setListener(new BaseClient.Listener() {
            @Override
            public void onSetup(List<Channel> channelList, List<GroupInfo> groupList) {
                /**
                 * 整理频道分组
                 */
                mChannelGroupList = new LinkedList<ChannelGroup>();

                for (int i = 0; i < groupList.size(); i++) {
                    GroupInfo info = groupList.get(i);
                    List<Channel> channels = getChannelsInGroup(info, channelList);

                    mChannelGroupList.add(new ChannelGroup(info, channels));
                }

                /**
                 * 更新频道表
                 */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateChannelList();
                    }
                });
            }

            @Override
            public void onDecodeSource(String source) {
                /**
                 * TODO：player
                 */
            }

            @Override
            public void onError(String error) {
                /**
                 * TODO：toast
                 */
            }
        });
        mSource.setup();
    }

    private static List<Channel> getChannelsInGroup(GroupInfo info, List<Channel> allChannels) {
        List<Channel> groupChannels = new LinkedList<Channel>();

        for (int i = 0; i < allChannels.size(); i++) {
            Channel channel = allChannels.get(i);

            for (String groupId : channel.getGroupIdList()) {
                if (info.getId().equals(groupId)) {
                    groupChannels.add(channel);
                    break;
                }
            }
        }

        return groupChannels;
    }

    private View.OnClickListener mOnSwitchGroupListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.id_prev_group: {
                    if (mGroupIndex > 0) {
                        mGroupIndex--;
                    }
                    else {
                        /**
                         * 循环滚动
                         */
                        mGroupIndex = mChannelGroupList.size() - 1;
                    }

                    break;
                }
                case R.id.id_next_group: {
                    if (mGroupIndex < mChannelGroupList.size() - 1) {
                        mGroupIndex++;
                    }
                    else {
                        /**
                         * 循环滚动
                         */
                        mGroupIndex = 0;
                    }

                    break;
                }
                default: {
                    throw new IllegalArgumentException("only support prev_group or next_group");
                }
            }

            updateChannelList();
        }
    };

    private void updateChannelList() {
        ChannelGroup group = mChannelGroupList.get(mGroupIndex);

        TextView groupNameView = (TextView)findViewById(R.id.id_group_name);
        groupNameView.setText(group.getName());

        ListView channelListView = (ListView)findViewById(R.id.id_channel_list);
        channelListView.setAdapter(new ChannelListAdapter(MainActivity.this, group.getChannelList()));
    }
}
