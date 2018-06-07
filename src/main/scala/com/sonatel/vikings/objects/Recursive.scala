package com.sonatel.vikings.objects

import com.sonatel.vikings.utils.Utils

import scala.annotation.tailrec

object Recursive {

  // (2) tail-recursive solution
  def sum2(ints: List[Int]): Int = {
    @tailrec
    def sumAccumulator(ints: List[Int], accum: Int): Int = {
      ints match {
        case Nil => accum
        case x :: tail => sumAccumulator(tail, accum + x)
      }
    }

    sumAccumulator(ints, 0)
  }

  def createCluster(listPoints: List[Antenna]): List[Cluster] = {
    var cl = new Cluster()
    var utils = new Utils()
    var indic = 0
    var forThreesold = listPoints

    @tailrec
    def clusterAccumulator(listPoints: List[Antenna], accum: List[Cluster]): List[Cluster] = {
      var nextPoint = new Antenna()
      listPoints match {
        case Nil => accum
        case x :: tail => {
          if (indic == 0) {
            cl = new Cluster()
          }
          var p = x
          if (!tail.isEmpty) {
            nextPoint = tail.head
          }
          cl.addAntenna(p)

          println("First Point" + p + " Next Point" + nextPoint)
          if (utils.calculDistance(p.point, nextPoint.point) > 2.5) {
            indic = 0
            clusterAccumulator(tail, cl :: accum)
          } else {
            indic = 1
            //cl.addPoint(nextPoint)
            clusterAccumulator(tail, accum)
          }
        }
      }
    }

    clusterAccumulator(listPoints, List[Cluster]())
  }

  def main(args: Array[String]): Unit = {

    var P1 = new Point(14.712240, -17.455130, 10) //C1
    var P2 = new Point(14.711078, -17.454701, 8) //C1
    var P3 = new Point(14.710081, -17.455817, 7) //C1
    var P4 = new Point(14.712738, -17.462426, 5) //C1

    var P5 = new Point(14.720333, -17.447877, 2) //C2

    var P6 = new Point(14.727721, -17.461825, 12) //C3
    var P7 = new Point(14.727389, -17.461525, 8) //C3

    var Ptest = new Point(14.696537, -17.448955, 1)
    var Ptest1 = new Point(14.705296, -17.456594, 1)
    var milieuSveta = new Point(14.7009165, -17.4527745, 2)
    var util = new Utils()

    var dtot = util.calculDistance(Ptest, Ptest1)
    var dbyMil = util.calculDistance(Ptest, milieuSveta) + util.calculDistance(milieuSveta, Ptest1)

    println("Distance P P1 " + dtot)
    println("Distance by milieu" + dbyMil)
  }

}


