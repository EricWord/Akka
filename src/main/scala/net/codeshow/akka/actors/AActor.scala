package net.codeshow.akka.actors

import akka.actor.{Actor, ActorRef}

class AActor(actorRef: ActorRef) extends Actor {
  val bActorRef: ActorRef = actorRef

  override def receive: Receive = {
    case "start" => {
      println("A Actor 出招了，start ok!")
      //发给自己
      self ! "我打"
    }
    case "我打" => {
      //给B Actor发出消息
      //这里需要持有B Actor的引用
      println("AActor(黄飞鸿) 厉害，看我佛山无影脚")
      Thread.sleep(1000)
      bActorRef ! "我打"

    }


  }
}
