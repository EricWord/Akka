package net.codeshow.akka.actor

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

//当继承Actor后就是Actor了
//核心方法是Receive，需要重写
class SayHelloActor extends Actor {

  //receive方法会被该Actor的MailBox(实现了Runable接口)调用
  //当该Actor的MailBox接收到消息时就会调用receive方法
  //Receive是一个偏函数
  override def receive: Receive = {
    case "hello" => println("收到hello,回应hello too :)")
    case "ok" => println("收到ok,回应Ok too~")
    case "exit" => {
      println("接收到退出指令")
      //退出ActorRef
      context.stop(self)
      //退出ActorSystem
      context.system.terminate()
    }
    case _ => println("匹配不到")


  }
}

object SayHelloActorDemo {

  //1.先创建一个ActorSystem,专门用于创建Actor
  private val actorFactory: ActorSystem = ActorSystem("actorFactory")
  //2.创建一个Actor的同时，返回Actor的ActorRef
  //Props[SayHelloActor] 创建了一个SayHelloActor的实例，使用了反射机制
  //第二个参数"sayHelloActor"是Actor的name
  //sayHelloActorRef: ActorRef 是SayHelloActor实例的一个引用
  private val sayHelloActorRef: ActorRef = actorFactory.actorOf(Props[SayHelloActor], "sayHelloActor")

  def main(args: Array[String]): Unit = {

    //给SayHelloActor发消息
    sayHelloActorRef ! "hello"
    sayHelloActorRef ! "ok"
    sayHelloActorRef ! "ok~~~"
    sayHelloActorRef ! "exit"


  }


}
