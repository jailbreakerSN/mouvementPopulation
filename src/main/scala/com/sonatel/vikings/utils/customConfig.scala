package com.sonatel.vikings.utils

import java.io.File

import com.typesafe.config.ConfigFactory

/**
  * This object read configuration file parameters values
  */
object customConfig {

  var CDR_DATAS = ""
  var ANTENNAS_DATAS = ""
  var DATABASE = ""

  /**
    * loader : loading configuration file
    *
    * @param inputPath String configuration file path
    */

  def load(inputPath: String): Unit = {
    val myConfigFile = new File(inputPath)
    val configFile = ConfigFactory.parseFile(myConfigFile)

    CDR_DATAS = configFile.getString("VIKINGS.DATAS.CDR_TEST")
    ANTENNAS_DATAS = configFile.getString("VIKINGS.DATAS.ANTENNAS_TEST")
    DATABASE = configFile.getString("VIKINGS.DATA.DATABASE")

  }

}
