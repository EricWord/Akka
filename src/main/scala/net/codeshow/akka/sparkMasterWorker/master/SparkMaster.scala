package net.codeshow.akka.sparkMasterWorker.master

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import net.codeshow.akka.sparkMasterWorker.common.{RegisterWorkerInfo, RegisteredWorkerInfo, WorkerInfo}

import scala.collection.mutable

class SparkMaster extends Actor {
  //定义一个hashMap，管理workers
  val workers: mutable.Map[String, WorkerInfo] = mutable.Map[String, WorkerInfo]()

  override def receive: Receive = {
    case "start" => println("master服务器启动...")
    case RegisterWorkerInfo(id, cpu, ram) => {
      println("Master 收到RegisterWorkerInfo")
      //接收到worker注册信息
      if (!workers.contains(id)) {
        val workerInfo = new WorkerInfo(id, cpu, ram)
        //加入到workers
        workers += ((id, workerInfo))
        println("服务器workers=" + workers)
        //回复一个消息，说注册成功
        sender() ! RegisteredWorkerInfo

      }

    }

  }
}

object SparkMaster {
  def main(args: Array[String]): Unit = {
    //服务端ip地址
    val host = "127.0.0.1"
    val port = 10001

    val config = ConfigFactory.parseString(
      s"""
         |akka.actor.provider="akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname=$host
         |akka.remote.netty.tcp.port=$port
         |""".stripMargin
    )

    val sparkMasterActorSystem = ActorSystem("SparkMaster", config)
    //创建SparkMaster Actor
    val sparkMasterRef = sparkMasterActorSystem.actorOf(Props[SparkMaster], "SparkMaster01")
    //启动SparkMaster
    sparkMasterRef ! "start"

  }
}
