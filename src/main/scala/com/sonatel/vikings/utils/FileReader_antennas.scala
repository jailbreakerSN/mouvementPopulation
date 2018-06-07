package com.sonatel.vikings.utils

import java.util

import com.sonatel.vikings.objects.Point
import org.apache.spark.sql.Row
import org.apache.spark.sql.hive.HiveContext

case class AntennaInfo(id_cellule: String, nom_cellule: String, bsc_rnc: String, bts_nodeb: String, region: String,
                       departement: String, arrondissement: String, commune: String, quartier: String, zone: String,
                       x: String, y: String, plaque: String, zone_drv: String, longitude: String, latitude: String,
                       azimut: String, zone_dvri: String)

case class GPS(var latitude: String, var longitude: String)

/**
  * Read Antenna's information from the file
  *
  * @param hiveContext SparkSession
  */
class FileReader_antennas(hiveContext: HiveContext) {
  val database = customConfig.DATABASE
  val antennaDF = hiveContext.sql("select * from algorithmisation.antennas")

  def reader(): List[AntennaInfo] = {

    var AI = List[AntennaInfo]()
    var antennaList: util.List[Row] = antennaDF.collectAsList() //return a java.util.List[Row]
    var size = antennaList.size() //List size

    for (i <- 0 until (size)) {
      var cr = antennaList.get(i) // get Current Line data
      var antennaInfo = new AntennaInfo(cr.getString(0), cr.getString(1), cr.getString(2), cr.getString(3), cr.getString(4),
        cr.getString(5), cr.getString(6), cr.getString(7), cr.getString(8), cr.getString(9), cr.getString(10),
        cr.getString(11), cr.getString(12), cr.getString(13), cr.getString(14), cr.getString(15), cr.getString(16), cr.getString(17))

      AI = antennaInfo :: AI // Ajout de cette ligne dans la Liste
    }

    AI
  }

  def getCoordinates(bts_nodeb: String): GPS = {
    var P = new GPS("", "")
    var antennaDF = hiveContext.sql("" +
      "select latitude, longitude " +
      "from algorithmisation.antennas " +
      "where bts_nodeb='" + bts_nodeb + "' limit 1")
    var pointList: util.List[Row] = antennaDF.collectAsList()
    if (pointList.size() > 0) {
      P.latitude = pointList.get(0).getString(0)
      P.longitude = pointList.get(0).getString(1)
    }
    P
  }


  def getPoints(): List[Point] = {
    var LP = List[Point]()
    var pointList: util.List[Row] = antennaDF.collectAsList() //return a java.util.List[Row]
    var size = pointList.size() //List size

    for (i <- 0 until (size)) {
      import java.text.NumberFormat
      import java.util.Locale
      val nf = NumberFormat.getInstance(Locale.GERMAN)

      var cr = pointList.get(i) // get Current Line data
      val latitude = nf.parse(cr.getString(15)).doubleValue
      val longitude = nf.parse(cr.getString(14)).doubleValue
      var point = new Point(latitude, longitude, 0)
      //println(point)

      LP = point :: LP
    }

    LP
  }
}
