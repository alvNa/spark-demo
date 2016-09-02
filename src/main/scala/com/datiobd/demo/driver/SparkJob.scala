package com.datiobd.demo.driver

import java.io.Serializable

import com.datiobd.demo.AppConf
import org.apache.spark.sql.{DataFrame, SQLContext, SaveMode}

/**
  * Created by anavarro on 4/08/16.
 */
class SparkJob(val sqlContext: SQLContext) extends Serializable {

  private var df: DataFrame = null

  def loadFiles(): Unit = {
    println(">> 1. Loading csv ...")
    val pathFile = AppConf.get("spark.demo.input")
    df = sqlContext.read.format("com.databricks.spark.csv").option("header", "true").option("inferSchema", "true").load(pathFile + "users.csv")
    df.show()
  }

  def saveTables(): Unit = {
    println(">> 2. Partitioning table ...")
    val path = AppConf.get("spark.demo.output")
    df.write.partitionBy("gender", "first_name").mode(SaveMode.Overwrite).save(path + "users1")
    df.write.partitionBy("last_name", "gender").mode(SaveMode.Overwrite).save(path + "users2")
    //Saving in avro format
    df.write.partitionBy("gender", "first_name").format("com.databricks.spark.avro").mode(SaveMode.Overwrite).save(path + "users3")

    val users = sqlContext.read.load("users1")
    users.registerTempTable("users1")
    val users2 = sqlContext.read.load("users2")
    users2.registerTempTable("users2")
    val users3 = sqlContext.read.format("com.databricks.spark.avro").load("users3")
    users3.registerTempTable("users3")
    df.printSchema()
  }

  def loadPartitionedFiles(): Unit = {
    println(">> 3. Load partitioned files")
    val path = AppConf.get("spark.demo.output")

    val usersAll = sqlContext.read.load(path + "users1/")
    usersAll.count

    val usersFem = sqlContext.read.load(path + "users1/gender=Female/**/*.parquet")
    usersFem.show
    usersFem.count

    val usersMale = sqlContext.read.load(path + "users2/**/gender=Male/*.parquet")
    usersMale.show
    usersMale.count
  }

  def executeQueries(): Unit = {
    println(">> 4. Execute Query over partitioned table...")
    val q1 = sqlContext.sql(Queries.SELECT_USERS1)
    q1.show()

    val q2 = sqlContext.sql(Queries.SELECT_USERS2)
    q2.show()
  }

  def useUDF(): Unit = {
    println(">> 5. Use simple UDF")

    def doTag(input: String): String = {
      "<" + input.trim().toLowerCase() + ">"
    }

    sqlContext.udf.register("doTag", doTag _)
    sqlContext.sql("select id, first_name, doTag(email) from users1").show()
  }

  def useUDAF(): Unit = {
    println(">> 6. Use UDAF")
    val customMean = new AvgUDAF()
    // Calculate average value for each group
    df.groupBy("gender").agg(customMean(df.col("id")).as("avg")).show()
  }

  def run(): Unit = {
    loadFiles()
    saveTables()
    loadPartitionedFiles()
    executeQueries()
    useUDF()
    useUDAF()
  }
}