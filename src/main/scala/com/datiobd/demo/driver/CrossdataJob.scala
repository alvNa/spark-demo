package com.datiobd.demo.driver

import org.apache.spark._
import org.apache.spark.sql.crossdata._

/**
  * Created by anavarro on 25/08/16.
  */
class CrossdataJob(val sc: SparkContext) {

  private var xdContext : XDContext = null

  def test() ={
    xdContext.sql("SHOW TABLES").show(false)
    val df = xdContext.sql("SELECT count(1) FROM users1")
    df.show()
  }

  def test2() ={
      println(">> writing..")
      val parquetFile = xdContext.read.format("com.databricks.spark.csv").option("header", "true").option("inferSchema", "true").load("/home/anavarro/entorno/spark/data/users.csv")
      parquetFile.registerTempTable("users")
      //def df = parquetFile.write.partitionBy("last_name", "gender").save("users")
      xdContext.sql("select * from users").show()
  }

  def run() = {
    xdContext = new XDContext(sc)
    test()
    //test2()
  }
}