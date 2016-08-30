# spark-demo - Demo to show the use of some Spark features
========

- load files in csv, avro
- save tables with partitioning using parquet and avro
- Executing SparkSQL queries using dataframes
- Use simple udf in SparkSql queries
- Use UDAF (also referred as user defined aggregate function) extending UserDefinedAggregateFunctions class

========
1) Install Spark in your machine from http://spark.apache.org/downloads.html
2) Copy users file from /src/resources/users.csv to your input directory
3) Modify application.properties with your paths
4) In the terminal go to $SPARK_HOME/bin
5) sudo su (to allow write in your filesystem)
6) To load and execute the full demo with spark-submit execute the following command in the shell changing the path of your app
./spark-submit --master local --packages com.databricks:spark-csv_2.10:1.4.0,com.databricks:spark-avro_2.10:2.0.1 --class com.datiobd.demo.App {YOUR_PATH}/spark-demo/target/spark-demo-1.0-SNAPSHOT.jar