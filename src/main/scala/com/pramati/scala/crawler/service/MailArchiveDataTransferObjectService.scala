package com.pramati.scala.crawler.service

import java.util.concurrent.{ExecutorService, Executors}

import com.pramati.scala.crawler.dtos.MailArchiveDataTransferObject
import com.pramati.scala.crawler.utils.WebCrawlerProperties
import org.slf4j.LoggerFactory

import scala.annotation.tailrec

/**
  * Created by babjik on 2/5/16.
  */
object MailArchiveDataTransferObjectService {
  val logger = LoggerFactory.getLogger(this.getClass)

  def doService(input: List[MailArchiveDataTransferObject]): Unit = {
    val pool: ExecutorService = Executors.newFixedThreadPool(WebCrawlerProperties.getFileWriterConcurrency)
    input.map(processMailArchiveDataTransferObject(pool,_))
    pool.shutdown()
  }

  private def processMailArchiveDataTransferObject(pool: ExecutorService, mailArchiveDataTransferObject: MailArchiveDataTransferObject): Unit = {
    pool.submit(new MailArchiveDataTransferObjectWorker(mailArchiveDataTransferObject))
  }
}
