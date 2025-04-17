package de.keksuccino.spiffyhud.networking.packets.structure.structures;

import de.keksuccino.fancymenu.networking.PacketCodec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StructuresPacketCodec extends PacketCodec<StructuresPacket> {

    private static final Logger LOGGER = LogManager.getLogger();

    public StructuresPacketCodec() {
        super("spiffy_structures", StructuresPacket.class);
    }

}
