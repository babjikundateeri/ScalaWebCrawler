package com.pramati.scala.crawler.service

import java.util.concurrent.{ExecutorService, Executors}

import com.pramati.scala.crawler.dtos.MailArchiveDataTransferObject
import com.pramati.scala.crawler.utils.WebCrawlerProperties
import org.slf4j.LoggerFactory

import scala.annotation.tailrec

/**
  * Created by babjik on 2/5/16.
  */
object MailArchiveDataBeanService {
  val logger = LoggerFactory.getLogger(this.getClass)

  def doService(input:List[MailArchiveDataTransferObject]): Unit = {
    val pool: ExecutorService = Executors.newFixedThreadPool(WebCrawlerProperties.getFileWriterConcurrency)
    @tailrec
    def go(input:List[MailArchiveDataTransferObject]): Unit = input match {
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
