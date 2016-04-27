package com.pramati.scala.crawler.service

import java.util.concurrent.{ExecutorService, Executors, Future}

import com.pramati.scala.crawler.dtos.{MailArchiveDataBean, MonthlyDataBean}
import com.pramati.scala.crawler.utils._
import org.slf4j.LoggerFactory

import scala.annotation.tailrec

/**
  * Created by babjik on 26/4/16.
  */

trait ServiceProvider {

}

object MonthlyDataBeanService extends ServiceProvider{
  val logger = LoggerFactory.getLogger(this.getClass)

  def doService (input: List[MonthlyDataBean]): List[MailArchiveDataBean] = {
    val pool: ExecutorService = Executors.newFixedThreadPool(5)

    def go(monthlyDataBeans: List[MonthlyDataBean]): List[Future[List[MailArchiveDataBean]]] = {
        monthlyDataBeans match {
          case Nil =>  Nil// just return
          case _ =>
            val worker = new MonthlyDataBeanWorker(monthlyDataBeans.head)
            val result: Future[List[MailArchiveDataBean]] = pool.submit(worker)
            go(monthlyDataBeans.tail) :+ result
        }
    }
    val listOfFututres = go(input)
    pool.shutdown()
    WebCrawlerCollectionUtility.reArrangeCollection(listOfFututres)
  }
}


object MailArchiveDataBeanService extends ServiceProvider{
  val logger = LoggerFactory.getLogger(this.getClass)

  def doService(input:List[MailArchiveDataBean]): Unit = {
    val pool: ExecutorService = Executors.newFixedThreadPool(50)
    @tailrec
    def go(input:List[MailArchiveDataBean]): Unit = input match {
      case Nil => // just returning
      case _ =>
        val worker = new MailArchivesDataBeanWorker(input.head)
        pool.submit(worker)
        go(input.tail)
    }

    go(input)
    pool.shutdown()
  }
}