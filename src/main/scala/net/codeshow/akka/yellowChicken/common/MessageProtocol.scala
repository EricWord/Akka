package net.codeshow.akka.yellowChicken.common

//使用样例类来构建协议

//客户端发送给服务器端的协议(序列化的对象)
case class ClientMessage(mes: String)

//服务器发给客户端的协议(样例类对象)
case class ServerMessage(mes: String)




