package com.sonatel.vikings.objects

class ClusterPersist(var cluster_id: String, var caller_msisdn: String, var bts_nodeb: String, var centroid_lat: Double, var centroid_long: Double) extends Serializable {

  def this() = {
    this("", "", "", 0.0, 0.0)
  }

  override def toString = s"ClusterPersist($cluster_id, $caller_msisdn, $bts_nodeb, $centroid_lat, $centroid_long)"
}
