package com.pramati.scala.crawler.dtos

/**
  * Created by babjik on 26/4/16.
  */
trait DataBean {

}

case class MonthlyDataBean (id: String, href: String, msgCount: Int) extends DataBean {
  override def toString = "[DataBean @MonthlyDataBean (id = " + id + ", href = " + href + ", msgCount = " + msgCount + ")]"
}

case class MailArchiveDataBean() extends DataBean {
  override def toString = "[DataBean @MailArchiveDataBean ()]"
}