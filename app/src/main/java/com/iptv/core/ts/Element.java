package com.iptv.core.ts;

final class Element {
    private int mType;

    /**
     * 构造函数
     */
    public Element(int type) {
        mType = type;
    }

    /**
     * 类型
     */
    public int getType() {
        return mType;
    }

    /**
     * 放入PES包
     */
    public void putPESPacket(PESPacket pesPacket) {
        //
    }
}
