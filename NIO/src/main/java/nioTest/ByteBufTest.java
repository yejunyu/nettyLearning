package nioTest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author : YeJunyu
 * @description :
 * @email : yyyejunyu@gmail.com
 * @date : 2021/7/7
 */
public class ByteBufTest {
    public static void main(String[] args) {
        ByteBuf buffer = Unpooled.buffer(5);
        for (int i = 0; i < 10; i++) {
            buffer.writeChar(i * i);
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(buffer.getByte(i));
        }
        // CompositeByteBuf
        // ReferenceCounted
        for (int i = 0; i < 10; i++) {
            System.out.println(buffer.readByte());
        }
    }
}
