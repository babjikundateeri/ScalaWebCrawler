package com.pramati.scala.crawler.service

import java.util.concurrent.{ExecutorService, Executors, Future}

import com.pramati.scala.crawler.dtos.{MailArchiveDataTransferObject, MonthlyDataTransferObject}
import com.pramati.scala.crawler.utils._
import org.slf4j.LoggerFactory

import scala.annotation.tailrec

/**
  * Created by babjik on 26/4/16.
  */

object MonthlyDataBeanService {
  val logger = LoggerFactory.getLogger(this.getClass)

  def doService (input: List[MonthlyDataTransferObject]): List[MailArchiveDataTransferObject] = {
    val pool: ExecutorService = Executors.newFixedThreadPool(WebCrawlerProperties.getMainThreadPoolSize)

    def go(monthlyDataBeans: List[MonthlyDataTransferObject]): List[Future[List[MailArchiveDataTransferObject]]] = {
        monthlyDataBeans match {
          case Nil =>  Nil// just return
          case _ =>
            val worker = new MonthlyDataBeanWorker(monthlyDataBeans.head)
            val result: Future[List[MailArchiveDataTransferObject]] = pool.submit(worker)
            go(monthlyDataBeans.tail) :+ result
        }
    }
    val listOfFututres = go(input)
    pool.shutdown()
    WebCrawlerUtils.reArrangeCollection(listOfFututres)
  }
}