package com.iptv.core.ts;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class TransportStream {
    private static final String TAG = "TransportStream";

    private Map<Integer, PayloadBuffer> mPayloadBufferTable;

    private ProgramAssociationTable mProgramAssociationTable = null;
    private boolean[] mProgramAssociationSectionStatus;

    private Program mActiveProgram = null;

    public TransportStream() {
        mPayloadBufferTable = new HashMap<Integer, PayloadBuffer>();
    }

    /**
     * 放入数据包
     */
    public void putPacket(TransportPacket packet) {
        int packetId = packet.getPacketId();

        if (isProgramAssociationTable(packetId)
                || isProgramMapTable(packetId)
                || isActiveProgramElement(packetId)) {
            if (packet.containsPayloadData()) {
                onReceiveTransportPacket(packet);
            }
            else {
                /**
                 * no payload data, ignore
                 */
            }
        }
        else {
            /**
             * not PAT or Program_map_PID, and not elementary_PID of active program, discard
             */
        }
    }

    /**
     * 数据包是PAT
     */
    private boolean isProgramAssociationTable(int packetId) {
        return packetId == 0x00;
    }

    /**
     * 数据包是PMT
     */
    private boolean isProgramMapTable(int packetId) {
        if (mProgramAssociationTable == null) {
            return false;
        }

        return mProgramAssociationTable.containsProgram(packetId);
    }

    /**
     * 数据包是当前播放节目的元素
     */
    private boolean isActiveProgramElement(int packetId) {
        if (mActiveProgram == null) {
            return false;
        }

        return mActiveProgram.containsElement(packetId);
    }

    /**
     * 收到数据包
     */
    private void onReceiveTransportPacket(TransportPacket packet) {
        PayloadBuffer buffer = getPayloadBuffer(packet.getPacketId());

        if (!buffer.checkContinuity(packet.getContinuityCounter())) {
            /**
             * 不连续
             */
            if (!buffer.isEmpty()) {
                /**
                 * 缓冲中有未完成的负载单元，清除！
                 */
                buffer.clear();
            }
        }

        if (packet.isPayloadUnitStart()) {
            /**
             * current packet is the start of a new payload unit
             */
            if (!buffer.isEmpty()) {
                onReceivePayloadUnit(packet.getPacketId(), buffer.read());
            }

            buffer.write(packet.getPayloadData());
        }
        else {
            if (buffer.isEmpty()) {
                Log.w(TAG, "data is not the start of payload unit, discard");
            }
            else {
                buffer.write(packet.getPayloadData());
            }
        }
    }

    private PayloadBuffer getPayloadBuffer(int packetId) {
        if (!mPayloadBufferTable.containsKey(packetId)) {
            mPayloadBufferTable.put(packetId, new PayloadBuffer());
        }

        return mPayloadBufferTable.get(packetId);
    }

    private void onReceivePayloadUnit(int packetId, byte[] payloadUnit) {
        if (isProgramAssociationTable(packetId)) {
            ProgramAssociationSection section = ProgramAssociationSection.parse(payloadUnit);
            if (section != null) {
                onReceiveProgramAssociationSection(section);
            }
            else {
                Log.e(TAG, "program association section is malformed");
            }
        }
        else if (isProgramMapTable(packetId)) {
            ProgramMapSection section = ProgramMapSection.parse(payloadUnit);
            if (section != null) {
                onReceiveProgramMapSection(packetId, section);
            }
            else {
                Log.e(TAG, "program map section is malformed");
            }
        }
        else {
            PESPacket pesPacket = PESPacket.parse(payloadUnit);
            if (pesPacket != null) {
                onReceivePESPacket(packetId, pesPacket);
            }
            else {
                Log.e(TAG, "pes packet is malformed");
            }
        }
    }

    private void onReceiveProgramAssociationSection(ProgramAssociationSection section) {
        if (mProgramAssociationTable == null) {
            /**
             * PAT init
             */
            mProgramAssociationTable = new ProgramAssociationTable(section.getTableVersion());
            mProgramAssociationSectionStatus = new boolean[section.getLastSectionNumber() + 1];
        }
        else {
            if (mProgramAssociationTable.getVersion() != section.getTableVersion()) {
                /**
                 * PAT update
                 */
                mProgramAssociationTable = new ProgramAssociationTable(section.getTableVersion());
                mProgramAssociationSectionStatus = new boolean[section.getLastSectionNumber() + 1];

                /**
                 * reset active program
                 */
                if (mActiveProgram != null) {
                    mActiveProgram = null;
                }
            }
        }

        if (!mProgramAssociationSectionStatus[section.getSectionNumber()]) {
            mProgramAssociationTable.putAssociations(section.getAssociations());
            mProgramAssociationSectionStatus[section.getSectionNumber()] = true;
        }
    }

    private void onReceiveProgramMapSection(int packetId, ProgramMapSection section) {
        Program program = mProgramAssociationTable.getProgram(packetId);

        if (!program.containsDefinition())  {
            /**
             * program definition init
             */
            program.setDefinition(section.getVersion(), section.getElements());

            if (mActiveProgram == null) {
                /**
                 * default selection, the first program
                 */
                mActiveProgram = program;
            }
        }
        else {
            if (program.getMapVersion() != section.getVersion()) {
                /**
                 * program definition update
                 */
                program.setDefinition(section.getVersion(), section.getElements());
            }
        }
    }

    private void onReceivePESPacket(int packetId, PESPacket pesPacket) {
        Element element = mActiveProgram.getElement(packetId);
        element.putPESPacket(pesPacket);

        /**
         * try read MediaSample
         */
    }
}
