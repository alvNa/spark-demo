# spark-demo - Demo to show some Spark features


- Load files in csv format
- Save tables with partitioning using parquet and avro formats
- Executing SparkSQL queries using dataframes
- Use simple UDF(user defined function) in SparkSQL queries
- Use UDAF(user defined aggregate function) extending UserDefinedAggregateFunctions class

# Steps
1. Install Spark in your machine from http://spark.apache.org/downloads.html
- Copy csv files from /src/resources/*.csv to your input directory
- Modify application.properties with your local paths
- In the terminal go to {$SPARK_HOME}/bin
    -  cd {$SPARK_HOME}/bin     
- Change user to allow write in your filesystem 
    -  sudo su
- Run the following command in the shell (replacing your local path) to execute the full demo with spark-submit
    - ./spark-submit --master local --packages com.databricks:spark-csv_2.10:1.4.0,com.databricks:spark-avro_2.10:2.0.1,com.stratio.crossdata:crossdata-core:1.2.2 --class com.datiobd.demo.App {YOUR_PATH}/spark-demo/target/spark-demo-1.0-SNAPSHOT.jar
