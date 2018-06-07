package com.sonatel.vikings.utils

import java.text.ParseException
import java.util

import com.sonatel.vikings.objects.{Antenna, Cluster, ClusterPersist, UserData}
import org.apache.spark.sql.Row
import org.apache.spark.sql.hive.HiveContext

case class UserInfo(caller_msisdn: String, ms_location: String, latitude: String, longitude: String, tot_days: String, tot_towers: String, rank: String)

/**
  * Read user's information from the file
  *
  * @param hiveContext SparkSession
  */
class fileReader_testData(hiveContext: HiveContext) {
  val database = customConfig.DATABASE
  val userDF = hiveContext.sql("select * from " + database + ".person_tower_days_final")

  val antennas = new FileReader_antennas(hiveContext)

  def reader(): List[UserInfo] = {
    var UI = List[UserInfo]()

    var userList: util.List[Row] = userDF.collectAsList() //return a java.util.List[Row]
    var size = userList.size() //List size


    for (i <- 0 until (size)) {
      var cr = userList.get(i) // get Current Line data
      var userInfo = new UserInfo(cr.getString(0), cr.getString(1), cr.getString(3), cr.getString(2), cr.getString(4), cr.getString(5), cr.getString(6))

      UI = userInfo :: UI // Ajout de cette ligne dans la Liste
    }

    UI
  }

  def readerPerUser(): List[UserData] = {

    var UD = List[UserData]()
    var indic = 0

    var userList: util.List[Row] = userDF.collectAsList() //return a java.util.List[Row]
    var size = userList.size() //List size
    var userData = new UserData("", List[Antenna]())
    userData.msisdn = userList.get(0).getString(0)
    for (i <- 0 until (size)) {
      var P = new Antenna()
      var cr = userList.get(i) // get Current Line data
      //var userInfo = new UserInfo(cr.getString(0), cr.getString(1), cr.getString(2), cr.getString(3), cr.getString(4), cr.getString(5), cr.getString(6))
      try {
        import java.text.NumberFormat
        import java.util.Locale
        val nf = NumberFormat.getInstance(Locale.GERMAN)
        //GPS = antennas.getCoordinates(cr.getString(1))
        P.bts_nodeb = cr.getString(1)
        P.point.latitude = nf.parse(cr.getString(3)).doubleValue()
        P.point.longitude = nf.parse(cr.getString(2)).doubleValue()
        P.point.weight = cr.getLong(4).toDouble
        if (indic == 0) {
          userData.listLocation = P :: userData.listLocation
          if ((i + 1) < size) {
            if (cr.getString(0) != userList.get(i + 1).getString(0)) {
              UD = userData :: UD
              indic = 1
            }
          } else {
            UD = userData :: UD
          }
        } else {
          userData = new UserData(userList.get(i).getString(0), List[Antenna]())
          userData.listLocation = P :: userData.listLocation
          if ((i + 1) < size) {
            if (cr.getString(0) == userList.get(i + 1).getString(0)) {
              indic = 0
            } else {
              UD = userData :: UD
            }
          } else {
            UD = userData :: UD
          }
        }
      } catch {
        case ex: NullPointerException => {
          println("NullllllllPoint")
        }
        case ex: ParseException => {
          println("ViiiiiideSttring")
        }
      }

    }

    UD
  }

  def persistData(listUD: List[UserData]): List[ClusterPersist] = {
    var lcPersist = List[ClusterPersist]()
    var clusterPersist = new ClusterPersist()
    var currentCluster = new Cluster()
    var antenna = new Antenna()
    for (i <- 0 until (listUD.size)) {
      clusterPersist.caller_msisdn = listUD(i).msisdn
      for (j <- 0 until (listUD(i).getCluster().size)) {
        currentCluster = listUD(i).getCluster()(j)
        //println(currentCluster)
        clusterPersist.cluster_id = clusterPersist.caller_msisdn.toString + "_" + j
        for (k <- 0 until (currentCluster.members.size)) {
          antenna = currentCluster.members(k)
          clusterPersist.bts_nodeb = antenna.bts_nodeb
          clusterPersist.centroid_lat = currentCluster.centroid.latitude
          clusterPersist.centroid_long = currentCluster.centroid.longitude
          //println(clusterPersist)
          val cp = new ClusterPersist(listUD(i).msisdn.trim + "_" + j, listUD(i).msisdn.trim, antenna.bts_nodeb, currentCluster.centroid.latitude, currentCluster.centroid.longitude)
          println(cp)
          lcPersist = cp :: lcPersist
        }
      }
    }
    lcPersist
  }

}

