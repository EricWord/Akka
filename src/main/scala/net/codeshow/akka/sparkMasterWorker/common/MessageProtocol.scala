package net.codeshow.akka.sparkMasterWorker.common

case class RegisterWorkerInfo(id:String,cpu:Int,ram:Int)

//这个信息将来是保存到master的hashMap(该hashMap用于管理worker)
//将来这个WorkerInfo会扩展(比如增加worker上一次的心跳时间)
class WorkerInfo(val id:String,val cpu:Int,val ram:Int)

//当worker注册成功，服务器返回一个RegisterWorkerInfo对象实例
case object RegisteredWorkerInfo