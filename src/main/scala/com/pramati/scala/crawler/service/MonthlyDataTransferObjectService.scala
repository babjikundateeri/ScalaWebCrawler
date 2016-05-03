package com.pramati.scala.crawler.service

import java.util.concurrent.{ExecutorService, Executors, Future}

import com.pramati.scala.crawler.dtos.{MailArchiveDataTransferObject, MonthlyDataTransferObject}
import com.pramati.scala.crawler.utils._
import org.slf4j.LoggerFactory

import scala.annotation.tailrec

/**
  * Created by babjik on 26/4/16.
  */

object MonthlyDataTransferObjectService {
  val logger = LoggerFactory.getLogger(this.getClass)

  def doService (input: List[MonthlyDataTransferObject]): List[MailArchiveDataTransferObject] = {
    val pool: ExecutorService = Executors.newFixedThreadPool(WebCrawlerProperties.getMainThreadPoolSize)
    val listOfFututres = input.map(processMonthlyDataTransferObject(pool, _))

    pool.shutdown()
    WebCrawlerUtils.reArrangeCollection(listOfFututres)
  }

  private def processMonthlyDataTransferObject(pool: ExecutorService, monthlyDataTransferObject: MonthlyDataTransferObject): Future[List[MailArchiveDataTransferObject]] = {
    pool.submit(new MonthlyDataTransferObjectWorker(monthlyDataTransferObject))
  }
}