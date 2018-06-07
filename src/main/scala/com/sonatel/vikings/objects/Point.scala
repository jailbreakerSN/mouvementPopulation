package com.sonatel.vikings.objects

import com.sonatel.vikings.utils.Utils


case class listSorted(var pointA: Point, var pointB: Point, var distance: Double)

/**
  * This class aims to instantiate a GPS coordinate with his Weight (latitude,longitude)
  *
  * @param latitude
  * @param longitude
  * @param weight
  */
class Point(var latitude: Double, var longitude: Double, var weight: Double) {

  def this() {
    this(0.0, 0.0, 0.0)
  }

  override def toString = s"Point($latitude, $longitude, $weight)"

  def calculateThreesold(listPoint: List[Point], variable_rank: Int): Double = {
    var distance = 0.0
    var listDistance = List[Double]()
    for (i <- 0 until (listPoint.size)) {
      distance = new Utils(this, listPoint(i)).calculDistance()
      listDistance = distance :: listDistance
    }
    listDistance = listDistance.sorted
    listDistance(variable_rank)
  }

  def listSorted(listPoint: List[Point]): List[Point] = {
    var distance = 0.0
    var listDistance = List[listSorted]()
    for (i <- 0 until (listPoint.size)) {
      distance = new Utils(this, listPoint(i)).calculDistance()
      var listSorted = new listSorted(this, listPoint(i), distance)
      listDistance = listSorted :: listDistance
    }
    listDistance = listDistance.sortWith(_.distance < _.distance)
    listDistance.map(p => (p.pointB))
  }
}

object Point {

}
