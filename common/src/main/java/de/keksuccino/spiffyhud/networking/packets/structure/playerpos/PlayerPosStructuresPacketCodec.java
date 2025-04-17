package de.keksuccino.spiffyhud.networking.packets.structure.playerpos;

import de.keksuccino.fancymenu.networking.PacketCodec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerPosStructuresPacketCodec extends PacketCodec<PlayerPosStructuresPacket> {

    private static final Logger LOGGER = LogManager.getLogger();

    public PlayerPosStructuresPacketCodec() {
        super("spiffy_player_pos_structures", PlayerPosStructuresPacket.class);
    }

}
