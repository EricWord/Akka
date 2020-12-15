package net.codeshow.akka.sparkMasterWorker.worker

import akka.actor.{Actor, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import net.codeshow.akka.sparkMasterWorker.common.{RegisterWorkerInfo, RegisteredWorkerInfo}
import net.codeshow.akka.sparkMasterWorker.master.SparkMaster

class SparkWorker(masterHost: String, masterPort: Int) extends Actor {
  //masterProxy是master的代理/引用ref
  var masterProxy: ActorSelection = _
  val id = java.util.UUID.randomUUID().toString

  override def preStart(): Unit = {
    println("preStart()调用了")
    //初始化masterProxy
    masterProxy = context.actorSelection(s"akka.tcp://Server@$masterHost:$masterPort/user/SparkMaster01")
    println("masterProxy=" + masterProxy)

  }

  override def receive: Receive = {
    case "start" => {
      println("Worker启动了")
      //发出一个注册消息
      masterProxy ! RegisterWorkerInfo(id, 16, 16 * 1024)
    }

    case RegisteredWorkerInfo => {
      println("workerId= " + id + " 注册成功~")
    }

  }
}

object SparkWorker {
  def main(args: Array[String]): Unit = {

    val workerHost = "127.0.0.1"
    val workerPort = 10002
    val masterHost = "127.0.0.1"
    val masterPort = 10001

    val config = ConfigFactory.parseString(
      s"""
         |akka.actor.provider="akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname=$workerHost
         |akka.remote.netty.tcp.port=$workerPort
         |""".stripMargin
    )

    val sparkMasterActorSystem = ActorSystem("SparkWorker", config)
    //创建SparkMaster Actor
    val sparkWorkerRef = sparkMasterActorSystem.actorOf(Props(new SparkWorker(masterHost, masterPort)), "SparkWorker01")
    //启动SparkMaster
    sparkWorkerRef ! "start"

  }
}


