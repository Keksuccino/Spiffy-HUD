package de.keksuccino.spiffyhud.networking.packets;

import de.keksuccino.fancymenu.networking.PacketRegistry;
import de.keksuccino.spiffyhud.networking.packets.structure.playerpos.PlayerPosStructuresPacketCodec;
import de.keksuccino.spiffyhud.networking.packets.structure.structures.StructuresPacketCodec;

public class Packets {

    public static final StructuresPacketCodec STRUCTURES_PACKET_CODEC = new StructuresPacketCodec();
    public static final PlayerPosStructuresPacketCodec PLAYER_POS_STRUCTURES_PACKET_CODEC = new PlayerPosStructuresPacketCodec();

    public static void registerAll() {

        PacketRegistry.register(STRUCTURES_PACKET_CODEC);
        PacketRegistry.register(PLAYER_POS_STRUCTURES_PACKET_CODEC);

    }

}
