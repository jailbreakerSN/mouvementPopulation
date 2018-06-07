package com.sonatel.vikings.objects

import com.sonatel.vikings.methods.Create_Clusters

class UserData(var msisdn: String, var listLocation: List[Antenna]) {
  def getCluster(): List[Cluster] = {
    new Create_Clusters(5).getClusters(listLocation)
  }
}
