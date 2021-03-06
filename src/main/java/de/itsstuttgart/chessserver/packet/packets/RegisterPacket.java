package de.itsstuttgart.chessserver.packet.packets;

import de.itsstuttgart.chessserver.clients.ChessClient;
import de.itsstuttgart.chessserver.packet.Packet;
import de.itsstuttgart.chessserver.packet.PacketHeader;
import de.itsstuttgart.chessserver.util.ByteUtils;
import de.itsstuttgart.chessserver.util.DataType;
import de.itsstuttgart.chessserver.util.model.UserModel;

/**
 * created by paul on 15.01.21 at 19:57
 */
@PacketHeader({0x72, 0x65}) // re
public class RegisterPacket implements Packet {

    @Override
    public void process(byte[] data, ChessClient client) {
        int pointer = 0;
        short usernameLen = ByteUtils.readShort(data, pointer);
        pointer += DataType.getSize(DataType.SHORT);
        String username = ByteUtils.readString(data, pointer, usernameLen);
        pointer += usernameLen;

        short passwordLen = ByteUtils.readShort(data, pointer);
        pointer += DataType.getSize(DataType.SHORT);
        String password = ByteUtils.readString(data, pointer, passwordLen);

        if (!client.getServer().getUserRepository().existsByUsername(username)) {
            UserModel model = new UserModel(username, password);
            client.getServer().getUserRepository().save(model);
            client.send(new byte[]{0x72, 0x73});
        } else {
            client.send(new byte[]{0x61, 0x6c, 0x00});
        }
    }
}
