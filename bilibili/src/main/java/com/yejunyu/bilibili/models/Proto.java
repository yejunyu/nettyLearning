package com.yejunyu.bilibili.models;

import cn.hutool.core.util.ZipUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import lombok.Data;

/**
 * @author : YeJunyu
 * @description : 长连接建立时发送数据包
 * 二进制符号对应: <p>https://docs.python.org/3/library/struct.html</p>
 * @email : yyyejunyu@gmail.com
 * @date : 2022/5/19
 */
@Data
public class Proto {

    public final static int OP_HEARTBEAT = 2;
    public final static int OP_HEARTBEAT_REPLY = 3;
    public final static int OP_SEND_SMS_REPLY = 5;
    public final static int OP_AUTH = 7;
    public final static int OP_AUTH_REPLY = 8;

    private int packetLen = 0;
    private int headerLen = 16;
    private int ver = 0;
    private int op = 0;
    private int seq = 0;
    private String body = "";
    private int maxBody = 2048;

    public byte[] pack() {
        this.packetLen = this.body.length() + this.headerLen;
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeInt(this.packetLen);
        byteBuf.writeShort(this.headerLen);
        byteBuf.writeShort(this.ver);
        byteBuf.writeInt(this.op);
        byteBuf.writeInt(this.seq);
        byteBuf.writeBytes(body.getBytes());
        return byteBuf.array();
    }

    public Proto unpack(ByteBuf byteBuf) {
        if (byteBuf.readableBytes() < this.headerLen) {
            System.out.println("包头不够");
            return this;
        }

        this.packetLen = byteBuf.readInt();
        this.headerLen = byteBuf.readShort();
        this.ver = byteBuf.readShort();
        this.op = byteBuf.readInt();
        this.seq = byteBuf.readInt();
        if (this.packetLen < 0 || this.packetLen > this.maxBody) {
            System.out.println("包体长度不对, packetLen: " + this.packetLen);
        }
        int bodyLen = this.packetLen - this.headerLen;
        // fixme 为什么读取 byte 需要另一份新的 byte 数组, 这种读取不行
        byte[] byteBody = new byte[packetLen - 16];
        byteBuf.readBytes(byteBody);
//        final ByteBuf byteBuf1 = byteBuf.readBytes(packetLen - 16);
//        System.out.println(byteBuf1.getClass());
        // 这个不报错
//        final byte b = byteBuf.readByte();
        // 这个报错
//        final ByteBuf byteBuf1 = byteBuf.readBytes(1);
//        final ByteBuf bytes1 = byteBuf.getBytes(16, byteBody);
//        byte[] byteBody = byteBuf.array();
//        final byte[] bytes1 = Arrays.copyOfRange(byteBody, 16, 16 + packetLen);
        if (bodyLen <= 0) {
            System.out.println("bodyLen < 0");
            return this;
        }
        if (this.ver == 0) {
            // body 数据再转发
            this.body = new String(byteBody, CharsetUtil.UTF_8);
        } else if (this.ver == 2) {
            final byte[] bytes = ZipUtil.unZlib(byteBody);
            bodyLen = bytes.length;
            int offset = 0;
            final ByteBuf copiedBuffer = Unpooled.copiedBuffer(bytes);
            while (offset < bodyLen) {
                int cmdSize = copiedBuffer.getInt(offset);
                if (offset + cmdSize > bodyLen) {
                    System.out.println("offset + cmdSize > bodyLen");
                    return this;
                }
                final Proto proto = new Proto();
                ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
                proto.unpack(copiedBuffer.getBytes(offset, buffer, cmdSize));
                offset += cmdSize;
            }
        } else {
            return this;
        }
        return this;
    }
}
