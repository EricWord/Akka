package net.codeshow.akka.yellowChicken.client

import akka.actor.{Actor, ActorRef, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import net.codeshow.akka.yellowChicken.common.{ClientMessage, ServerMessage}

import scala.io.StdIn

class CustomerActor(serverHost: String, serverPort: Int) extends Actor {
  //定义一个YelloChickenServerRef
  var serverActorRef: ActorSelection = _
  //在Actor中有一个方法preStart方法
  //会在actor运行前执行
  //在Akka的开发中，通常将初始化的工作放在preStart方法

  override def preStart(): Unit = {
    println("preRestart()执行了...")
    this.serverActorRef = context.actorSelection(s"akka.tcp://Server@$serverHost:$serverPort/user/YellowChickenServer")
    println("serverActorRef=" + this.serverActorRef)
  }

  override def receive: Receive = {
    case "start" => println("客户端start了，可以咨询问题")
    case mes: String => {
      //发给服务器
      serverActorRef ! ClientMessage(mes) //使用ClientMessage case class apply方法
    }
    //如果接收到了服务器端的回复
    case ServerMessage(mes) => {
      println(s"接收到小黄鸡客服(server)的回复:$mes")

    }

  }
}

//主程序，入口
object CustomerActor extends App {
  val (clientHost, clientPort, serverHost, serverPort) = ("127.0.0.1", 9990, "127.0.0.1", 9999)
  val config = ConfigFactory.parseString(
    s"""
       |akka.actor.provider="akka.remote.RemoteActorRefProvider"
       |akka.remote.netty.tcp.hostname=$clientHost
       |akka.remote.netty.tcp.port=$clientPort
       |""".stripMargin)

  //创建ActorSystem
  val clientActorSystem: ActorSystem = ActorSystem("client", config)
  //创建CustomerActor的实例和引用
  val customerActorRef: ActorRef = clientActorSystem.actorOf(Props(new CustomerActor(serverHost, serverPort)), "CustomerActor")
  //启动customerRef,也可以理解成启动Actor
  customerActorRef ! "start"

  //客户端发送消息给服务器
  while (true) {
    println("请输入要咨询的问题")
    val mes = StdIn.readLine()
    customerActorRef ! mes

  }


}
