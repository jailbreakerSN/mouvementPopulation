package com.sonatel.vikings.main

import com.sonatel.vikings.utils._
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}


object Main {
  def main(args: Array[String]): Unit = {
    var configFile = ""
    if (args.length == 0) {
      println("Configuration file required")
      configFile = "C:\\Users\\stg_ndiaye8517\\IdeaProjects\\vikings\\src\\main\\resources\\application.conf"
    } else {
      configFile = args(0)
    }


    //Create a SparkSession to initialize Spark
    //System.setProperty("hadoop.home.dir", "C:\\winutil")
    val sparkConf = new SparkConf()
    val sparkContext = new SparkContext(sparkConf)

    val hiveContext = new HiveContext(sparkContext)
    val sqlContext = new org.apache.spark.sql.SQLContext(sparkContext)

    customConfig.load(configFile)
    val filename = customConfig.CDR_DATAS
    val antennafile = customConfig.ANTENNAS_DATAS
    val database = customConfig.DATABASE
    val antennas = new FileReader_antennas(hiveContext)
    val datas = new fileReader_testData(hiveContext)

    //var getGPS= antennas.getCoordinates("ACAD")
    //println(getGPS)
    import sqlContext.implicits._
    //var perUsers = datas.readerPerUser()
    var lcPersist = datas.persistData(datas.readerPerUser())


    /*val mapLcPersist = lcPersist.map{case List(a:String, b:String, c:String, d:String, e:String) => (a,b,c,d,e) }
    var toDF = sparkContext.parallelize(mapLcPersist).toDF("cluster_id", "caller_msisdn", "bts_nodeb", "centroid_lat", "centroid_long")
    toDF.show()*/

    var lcPersistRDD = sparkContext.parallelize(lcPersist, 100).
      map {
        case f => (f.caller_msisdn, f.bts_nodeb, f.cluster_id, f.centroid_lat, f.centroid_long)
      }

    var myDF = hiveContext.createDataFrame(lcPersistRDD).toDF("caller_msisdn", "bts_nodeb", "cluster_id", "centroid_lat", "centroid_long")
    //myDF.show()
    myDF.registerTempTable("cluster_test_")
    hiveContext.sql("use "+database)
    hiveContext.sql("create table clusters_jul17 as select * from cluster_test_")


    sparkContext.stop()
  }


}
