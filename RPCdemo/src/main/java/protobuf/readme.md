```
在 /Users/yejunyu/IdeaProjects/nettyLearning/RPCdemo 目录下
protoc --java_out=src/main/java src/main/java/protobuf/Student.proto

```

每个语句都要有分号

```
客户端如果有多个消息类型
可以用 Message.proto 的方式用 oneof 消息类型枚举
```
