package com.sonatel.vikings.methods

import com.sonatel.vikings.objects.{Antenna, Cluster}
import com.sonatel.vikings.utils.Utils

import scala.annotation.tailrec

/**
  * Class which will create Clusters based on the rank
  *
  * @param rnk Threesold's radius rank
  */
class Create_Clusters(rnk: Int) {
  /**
    * The main function which will return, based on a list of Points, a list of Cluster
    *
    * @param listAntennas a list of Points
    * @return List of Clusters
    */
  def getClusters(listAntennas: List[Antenna]): List[Cluster] = {
    var cl = new Cluster()
    var utils = new Utils()
    var indic = 0
    //var forThreesold = listAntennas

    /**
      * Tail recursive function which will browse a list of points and put each of them in the appropriated cluster, based on the weighted distance
      *
      * @param listAntennas a list of points
      * @param accum        accumulator which will store all clusters
      * @return a list of Clusters
      */
    @tailrec
    def clusterAccumulator(listAntennas: List[Antenna], accum: List[Cluster]): List[Cluster] = {
      var nextPoint = new Antenna()
      listAntennas match {
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
          var newTail = p.listSorted(tail)
          //println("First Point" + p + " Next Point" + nextPoint)
          if (utils.calculDistance(p.point, nextPoint.point) > 2.5 /* p.calculateThreesold(forThreesold, rnk)*/ ) {
            indic = 0
            clusterAccumulator(newTail, cl :: accum)
          } else {
            indic = 1
            clusterAccumulator(newTail, accum)
          }
        }
      }
    }

    clusterAccumulator(listAntennas, List[Cluster]())
  }
}
