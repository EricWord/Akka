package net.codeshow.akka.yellowChicken.server

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import net.codeshow.akka.yellowChicken.common.{ClientMessage, ServerMessage}

class YellowChickenServer extends Actor {
  override def receive: Receive = {
    case "start" => println("start 小黄鸡客户系统开始工作了...")
    //如果接收到ClientMessage
    case ClientMessage(mes) => {
      //使用match case进行匹配

      mes match {
        case "大数据学费" => sender() ! ServerMessage("35000RMB")
        case "学校地址" => sender() ! ServerMessage("北京昌平xx路xx大厦")
        case "学习什么技术" => sender() ! ServerMessage("大数据、前端、python")
        case _ => sender() ! ServerMessage("你说的啥子")
      }

    }

  }
}

//主程序-入口

object YellowChickenServer extends App {


  //服务端ip地址
  val host = "127.0.0.1"
  val port = 9999

  val config = ConfigFactory.parseString(
    s"""
       |akka.actor.provider="akka.remote.RemoteActorRefProvider"
       |akka.remote.netty.tcp.hostname=$host
       |akka.remote.netty.tcp.port=$port
       |""".stripMargin
  )
  //创建ActorSystem
  val serverActorSystem: ActorSystem = ActorSystem("Server", config)
  //创建YellowChickenServer的actor和actorRef
  val yellowChickenServerRef: ActorRef = serverActorSystem.actorOf(Props[YellowChickenServer], "YellowChickenServer")


  //启动
  yellowChickenServerRef ! "start"

}

