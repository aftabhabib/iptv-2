package com.iptv.source;

import com.iptv.channel.ChannelTable;

public interface Source {
    interface OnSetupListener {
        void onSetup(ChannelTable table);

        void onError(String error);
    }

    void setup(OnSetupListener listener);
}
