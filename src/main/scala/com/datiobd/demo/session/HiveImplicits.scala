package com.datiobd.demo.session

import com.hortonworks.hwc.HiveWarehouseSession
import com.hortonworks.spark.sql.hive.llap.HiveWarehouseBuilder
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * A more elegant way with an implicit class
 */
object HiveImplicits {

  implicit class CustomSparkSession(spark: SparkSession) {
    private val allowedSqlStatements = Seq("SELECT", "UPDATE", "DELETE", "INSERT")
    private val hive: HiveWarehouseSession = HiveWarehouseBuilder.session(spark).build()

    def sqlWithHive(sqlText: String): DataFrame = {
      sqlText.split(" ").head.toUpperCase match {
        case x if (!isValidSqlStatement(x)) => throw new Exception(s"$x is not a valid statement")
        case "SELECT" => hive.executeQuery(sqlText).asInstanceOf[DataFrame]
        case _ => spark.emptyDataFrame
      }
    }

    private def isValidSqlStatement(statement: String): Boolean = {
      allowedSqlStatements.contains(statement.toUpperCase)
    }
  }
}