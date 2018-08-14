package com.iptv.core.ts;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TransportStream {
    private Map<Integer, PayloadBuffer> mPayloadBufferTable;

    private ProgramAssociationTable mProgramAssociationTable = null;
    private boolean[] mProgramAssociationSectionStatus;

    private Program mActiveProgram = null;

    /**
     * 构造函数
     */
    public TransportStream() {
        mPayloadBufferTable = new HashMap<Integer, PayloadBuffer>();
    }

    /**
     * 放入数据包
     */
    public void putPacket(TransportPacket packet) {
        int packetId = packet.getPacketId();

        if (isProgramSpecificInformation(packetId) || isActiveProgramElement(packetId)) {
            if (packet.containsPayloadData()) {
                onReceiveTransportPacket(packet);
            }
            else {
                /**
                 * no payload data, discard
                 */
            }
        }
        else {
            /**
             * not PAT or Program_map_PID, and not elementary_PID of active program, discard
             */
        }
    }

    private boolean isProgramSpecificInformation(int packetId) {
        if (isProgramAssociationTable(packetId)
                || isConditionalAccessTable(packetId)
                || isTransportStreamDescriptionTable(packetId)
                || isProgramMapTable(packetId)) {
            return true;
        }

        return false;
    }

    /**
     * 数据包是PAT
     */
    private boolean isProgramAssociationTable(int packetId) {
        return packetId == 0x00;
    }

    /**
     * 数据包是CAT
     */
    private boolean isConditionalAccessTable(int packetId) {
        return packetId == 0x01;
    }

    /**
     * 数据包是TSDT
     */
    private boolean isTransportStreamDescriptionTable(int packetId) {
        return packetId == 0x02;
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
                /**
                 * buffer empty, wait the start of a new payload unit
                 */
            }
            else {
                buffer.write(packet.getPayloadData());
            }
        }
    }

    /**
     * 获取PID对应的PayloadBuffer（如果没有，则新建）
     */
    private PayloadBuffer getPayloadBuffer(int packetId) {
        if (!mPayloadBufferTable.containsKey(packetId)) {
            mPayloadBufferTable.put(packetId, new PayloadBuffer());
        }

        return mPayloadBufferTable.get(packetId);
    }

    /**
     * 收到负载单元
     */
    private void onReceivePayloadUnit(int packetId, byte[] payloadUnit) {
        if (isProgramSpecificInformation(packetId)) {
            int pointerField = payloadUnit[0];
            if (pointerField < payloadUnit.length - 1) {
                byte[] sectionData =
                        Arrays.copyOfRange(payloadUnit, 1 + pointerField, payloadUnit.length);
                onReceiveProgramSpecificInformation(packetId, sectionData);
            }
        }
        else {
            PESPacket pesPacket = PESPacket.parse(payloadUnit);
            if (pesPacket != null) {
                onReceivePESPacket(packetId, pesPacket);
            }
        }
    }

    /**
     * 收到ProgramSpecificInformation
     */
    private void onReceiveProgramSpecificInformation(int packetId, byte[] sectionData) {
        if (isProgramAssociationTable(packetId)) {
            ProgramAssociationSection section = ProgramAssociationSection.parse(sectionData);
            if (section != null) {
                onReceiveProgramAssociationSection(section);
            }
        }
        else if (isProgramMapTable(packetId)) {
            ProgramMapSection section = ProgramMapSection.parse(sectionData);
            if (section != null) {
                onReceiveProgramMapSection(packetId, section);
            }
        }
        else {
            /**
             * we do not care, ignore
             */
        }
    }

    /**
     * 收到ProgramAssociationTable的片段
     */
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

    /**
     * 收到ProgramMapTable的片段
     */
    private void onReceiveProgramMapSection(int packetId, ProgramMapSection section) {
        Program program = mProgramAssociationTable.getProgram(packetId);

        if (!program.containsDefinition())  {
            /**
             * program definition init
             */
            program.setDefinition(section.getVersion(), section.getElements());

            /**
             * default selection, choose the first program
             */
            if (mActiveProgram == null) {
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

    /**
     * 收到PES包
     */
    private void onReceivePESPacket(int packetId, PESPacket pesPacket) {
        Element element = mActiveProgram.getElement(packetId);
        element.putPESPacket(pesPacket);

        /**
         * try read MediaSample
         */
    }
}
