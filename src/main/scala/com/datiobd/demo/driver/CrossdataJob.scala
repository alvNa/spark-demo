package com.datiobd.demo.driver

import com.typesafe.config.ConfigFactory
import org.apache.spark._
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.crossdata._

/**
 * Created by anavarro on 25/08/16.
 */
class CrossdataJob(val sc: SparkContext) {

  private var xdContext: XDContext = null
  private lazy val config = ConfigFactory.load("application.conf")
  private val path = config.getString("spark.demo.input")

  def loadFiles() = {
    println(">> 1. Load files using xD ..")

    val parquetFile = xdContext.read.format("com.databricks.spark.csv").option("header", "true").option("inferSchema", "true").load(path + "users.csv")
    parquetFile.registerTempTable("users")
    parquetFile.write.partitionBy("last_name", "gender").mode(SaveMode.Overwrite).save("users")
    xdContext.sql("select * from users").show()
    xdContext.sql("SHOW TABLES").show(false)
  }

  def run() = {
    xdContext = new XDContext(sc)
    loadFiles()
  }
}