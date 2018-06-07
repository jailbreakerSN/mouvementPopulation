package com.sonatel.vikings.objects

import com.sonatel.vikings.utils.Utils

class Cluster(/*var id_cluster: String,*/ var members: List[Antenna], var centroid: Point) {

  def this() = {
    this(List[Antenna](), new Point())
  }

  /*
    def this(id_cluster: String) = {
      this(id_cluster, List[Antenna](), new Point())
    }*/


  def addAntenna(newAntenna: Antenna): Unit = {
    this.members = newAntenna :: this.members
    this.centroid = new Utils(this.centroid, newAntenna.point).centroidePond()
  }

  override def toString = s"Cluster($members, $centroid)"
}

object Cluster {
  /*  var counter = 0
    def newCluster() ={
      counter += 1
      new Cluster(counter.toString, List[Antenna](), new Point())
    }*/


}
