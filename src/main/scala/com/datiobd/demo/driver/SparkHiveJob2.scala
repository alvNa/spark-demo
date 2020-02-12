package com.datiobd.demo.driver

import java.io.Serializable

import com.datiobd.demo.session.HiveImplicits._
import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * Created by anavarro on 4/08/16.
 */
class SparkHiveJob2(val spark: SparkSession) extends Serializable {

  private var df: DataFrame = null
  private lazy val config = ConfigFactory.load("application.conf")
  private val pathFile = config.getString("spark.demo.input")
  private val path = config.getString("spark.demo.output")

  def loadFiles(): Unit = {

    //This works because it's the same implementation
    //We are only wrapping the spark session add more funcionallity
    import spark.implicits._

    //Regular sql functionality
    spark.sql("SELECT * FROM USERS")

    //New functionality from HiveImplicits
    spark.sqlWithHive("SELECT * FROM USERS")

    println(">> 1. Loading csv ...")
    df = spark.read.format("com.databricks.spark.csv").option("header", "true").option("inferSchema", "true").load(pathFile + "users.csv")
    df.show()
  }

  def run(): Unit = {
    loadFiles()
  }
}