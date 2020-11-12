package com.yejunyu.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * @author: YeJunyu
 * @description: 测试序列化反序列化
 * @email: yyyejunyu@gmail.com
 * @date: 2020/11/8
 */
public class ProtoBufTest {

    public static void main(String[] args) throws InvalidProtocolBufferException {
        DataInfo.Student student = DataInfo.Student.newBuilder()
                .setName("张三")
                .setAge(20)
                .setAddress("北京")
                .addPhone(
                        DataInfo.Student.PhoneNumber.newBuilder()
                                .setNumber("555-666")
                                .setType(DataInfo.Student.PhoneType.MOBILE)
                                .build()
                )
                .build();
        // 序列化
        byte[] student2ByteArray = student.toByteArray();
        // 反序列化
        DataInfo.Student student1 = DataInfo.Student.parseFrom(student2ByteArray);
        System.out.println(student1.getName());
        System.out.println(student1.getAge());
        System.out.println(student1.getAddress());

    }
}
