package com.forcetech.android;

class ForceApi {
    public static String switchChannel(int servPort, ForceChannel channel) {
        StringBuffer buffer = new StringBuffer();

        buffer.append("http://127.0.0.1:");
        buffer.append(servPort);
        buffer.append("/cmd.xml?cmd=switch_chan");
        buffer.append("&server=");
        buffer.append(channel.getServer());
        buffer.append("&id=");
        buffer.append(channel.getId());

        return buffer.toString();
    }

    public static String stopChannel(int servPort, ForceChannel channel) {
        StringBuffer buffer = new StringBuffer();

        buffer.append("http://127.0.0.1:");
        buffer.append(servPort);
        buffer.append("/cmd.xml?cmd=stop_chan");
        buffer.append("&id=");
        buffer.append(channel.getId());

        return buffer.toString();
    }
/*
    public static String pauseChannel(int servPort, ForceChannel channel) {
        StringBuffer buffer = new StringBuffer();

        buffer.append("http://127.0.0.1:");
        buffer.append(servPort);
        buffer.append("/cmd.xml?cmd=pause_chan");
        buffer.append("&id=");
        buffer.append(channel.getId());

        return buffer.toString();
    }

    public static String resumeChannel(int servPort, ForceChannel channel) {
        StringBuffer buffer = new StringBuffer();

        buffer.append("http://127.0.0.1:");
        buffer.append(servPort);
        buffer.append("/api?func=resume_chan");
        buffer.append("&id=");
        buffer.append(channel.getId());

        return buffer.toString();
    }

    public static String queryChannelInfo(int servPort, ForceChannel channel) {
        StringBuffer buffer = new StringBuffer();

        buffer.append("http://127.0.0.1:");
        buffer.append(servPort);
        buffer.append("/api?func=query_channel_info");
        buffer.append("&id=");
        buffer.append(channel.getId());

        return buffer.toString();
    }

    public static String queryChannelDataInfo(int servPort, ForceChannel channel) {
        StringBuffer buffer = new StringBuffer();

        buffer.append("http://127.0.0.1:");
        buffer.append(servPort);
        buffer.append("/api?func=query_chan_data_info");
        buffer.append("&id=");
        buffer.append(channel.getId());

        return buffer.toString();
    }

    public static String queryChannelP2PInfo(int servPort, ForceChannel channel) {
        StringBuffer buffer = new StringBuffer();

        buffer.append("http://127.0.0.1:");
        buffer.append(servPort);
        buffer.append("/api?func=query_chan_p2p_info");
        buffer.append("&id=");
        buffer.append(channel.getId());

        return buffer.toString();
    }

    public static String setupFlow(int servPort, ForceChannel channel, int avg, int max) {
        StringBuffer buffer = new StringBuffer();

        buffer.append("http://127.0.0.1:");
        buffer.append(servPort);
        buffer.append("/api?func=set_up_flow");
        buffer.append("&id=");
        buffer.append(channel.getId());
        buffer.append("&avg=");
        buffer.append(avg);
        buffer.append("&max=");
        buffer.append(max);

        return buffer.toString();
    }

    public static String stopAllChannel(int servPort) {
        StringBuffer buffer = new StringBuffer();

        buffer.append("http://127.0.0.1:");
        buffer.append(servPort);
        buffer.append("/api?func=stop_all_chan");

        return buffer.toString();
    }

    public static String queryProcessInfo(int servPort) {
        StringBuffer buffer = new StringBuffer();

        buffer.append("http://127.0.0.1:");
        buffer.append(servPort);
        buffer.append("/api?func=query_process_info");

        return buffer.toString();
    }

    public static String exitProcess(int servPort) {
        StringBuffer buffer = new StringBuffer();

        buffer.append("http://127.0.0.1:");
        buffer.append(servPort);
        buffer.append("/api?func=exit_process");

        return buffer.toString();
    }
*/
    private ForceApi() {
        //ignore
    }
}
