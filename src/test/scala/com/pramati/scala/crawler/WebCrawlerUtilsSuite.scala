package com.pramati.scala.crawler

import java.io.File
import java.net.MalformedURLException

import com.pramati.scala.crawler.utils.{WebCrawlerUtils, WebCrawlerFileUtils}
import org.scalatest.FunSuite


/**
  * Created by babjik on 27/4/16.
  */
class WebCrawlerUtilsSuite extends FunSuite{
    test ("Testing Url connection ") {
      val content = WebCrawlerUtils.readDataFromURL("http://mail-archives.apache.org/mod_mbox/")
      if (content == null) {
        fail()
      }
      assert(true)
    }

  test ("Testing with Malformed Url") {
    intercept[MalformedURLException] {
      val content = WebCrawlerUtils.readDataFromURL("www.google.com")
    }
  }


  // checking for fileUtils
  test ("Testing file writing utitly") {
    val fileName = "/tmp/sample.txt"
    val fileContent = "Sample content taken for testing"

    WebCrawlerFileUtils.storeFile(fileName, fileContent)

    val contentInFile = scala.io.Source.fromFile(fileName).mkString
    if (fileContent.equals(contentInFile))
      assert(true)
    else
      fail("file content from file is not matching " + contentInFile)
  }


  test("Testing for isFileExists") {
    val fileName = "/tmp/sample.txt"

    if (WebCrawlerFileUtils.isFileExists(fileName)) {
      assert(true)
    } else {
      fail("file does not exist" + fileName)
    }
  }

  test("Testing for isFileExists for deleted file") {
    val fileName = "/tmp/sample.txt"
    new File(fileName).delete()

    if (WebCrawlerFileUtils.isFileExists(fileName)) {
      fail("file exists after delete")
    } else {
      assert(true)
    }
  }

  test("testing create directories") {
    val dirs = "/tmp/a/b/c"
    WebCrawlerFileUtils.createDirectories(dirs)

    if (WebCrawlerFileUtils.isFileExists(dirs))
      assert(true)
    else
      fail("unable to create dirs.. ")

  }
}
