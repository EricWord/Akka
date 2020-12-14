package net.codeshow.akka.actors

import akka.actor.{ActorRef, ActorSystem, Props}

object ActorGame extends App {
  //创建ActorSystem
  val actorFactory: ActorSystem = ActorSystem("actorFactory")
  //先创建BActor的引用
  val bActorRef: ActorRef = actorFactory.actorOf(Props[BActor], "bActor")
  //创建AActor的引用
  val aActorRef: ActorRef = actorFactory.actorOf(Props(new AActor(bActorRef)), "aActor")
  //A Actor出招
  aActorRef ! "start"
}
