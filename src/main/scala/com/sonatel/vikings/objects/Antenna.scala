package com.sonatel.vikings.objects

import com.sonatel.vikings.utils.Utils

case class antennaSorted(var pointA: Antenna, var pointB: Antenna, var distance: Double)

class Antenna(var bts_nodeb: String, var point: Point) {
  def this() {
    this("", new Point())
  }

  override def toString = s"Antenna($bts_nodeb, $point)"

  def listSorted(listAntenna: List[Antenna]): List[Antenna] = {
    var distance = 0.0
    var listDistance = List[antennaSorted]()
    for (i <- 0 until (listAntenna.size)) {
      distance = new Utils(this.point, listAntenna(i).point).calculDistance()
      var listSorted = new antennaSorted(this, listAntenna(i), distance)
      listDistance = listSorted :: listDistance
    }
    listDistance = listDistance.sortWith(_.distance < _.distance)
    listDistance.map(p => (p.pointB))
  }
}
