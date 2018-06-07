package com.sonatel.vikings.utils

import com.sonatel.vikings.objects.Point

class Utils(point_A: Point, point_B: Point) {

  val pond_A = (point_A.weight / totPond)
  val pond_B = (point_B.weight / totPond)
  var A_rad = toRadian(point_A)
  var B_rad = toRadian(point_B)
  var totPond = point_A.weight + point_B.weight

  def this() = {
    this(new Point(), new Point())
  }

  /*
  Fonction qui convertit un Point GPS (Degré) en Radian
   */
  def toRadian(location: Point): Point = {
    new Point(Math.toRadians(location.latitude), Math.toRadians(location.longitude), location.weight)
  }

  def calculDistance(pA: Point, pB: Point): Double = {
    var u = new Utils(pA, pB)
    return u.calculDistance()
  }

  def calculDistance(): Double = {

    val latDistance = A_rad.latitude - B_rad.latitude
    val lngDistance = A_rad.longitude - B_rad.longitude

    val sinLat = Math.sin(latDistance / 2)
    val sinLng = Math.sin(lngDistance / 2)

    val a = sinLat * sinLat +
      (Math.cos(A_rad.latitude)
        * Math.cos(B_rad.latitude)
        * sinLng * sinLng)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    val AVERAGE_RADIUS_OF_EARTH_KM = 6371
    AVERAGE_RADIUS_OF_EARTH_KM * c
  }

  def centroidPond(pA: Point, pB: Point): Point = {
    var u = new Utils(pA, pB)
    u.centroidePond()
  }

  def centroidePond(): Point = {

    // Conversion des coordonnées du Point A en coordonnées cartésiennes
    var x1 = Math.cos(A_rad.latitude) * Math.cos(A_rad.longitude)
    var y1 = Math.cos(A_rad.latitude) * Math.sin(A_rad.longitude)
    var z1 = Math.sin(A_rad.latitude)
    // Conversion des coordonnées du Point B en coordonnées cartésiennes
    var x2 = Math.cos(B_rad.latitude) * Math.cos(B_rad.longitude)
    var y2 = Math.cos(B_rad.latitude) * Math.sin(B_rad.longitude)
    var z2 = Math.sin(B_rad.latitude)

    // Calcul des coordonnées du centroide pondéré par le poids de chaque point
    var xm = x1 * pond_A + x2 * pond_B
    var ym = y1 * pond_A + y2 * pond_B
    var zm = z1 * pond_A + z2 * pond_B
    // Recupération des coordonnées latitude et longitude du centroide en radian
    var lon = Math.atan2(ym, xm)
    var hyp = Math.sqrt(xm * xm + ym * ym)
    var lat = Math.atan2(zm, hyp)

    toDegree(new Point(lat, lon, point_A.weight + point_B.weight))
  }

  /*
    Fonction qui convertit un Point GPS (Radian) en Degré
   */
  def toDegree(location: Point): Point = {
    new Point(Math.toDegrees(location.latitude), Math.toDegrees(location.longitude), location.weight)
  }

  def centroide(pA: Point, pB: Point): Point = {
    var u = new Utils(pA, pB)
    u.centroide()
  }

  def centroide(): Point = {
    val Bx = Math.cos(B_rad.latitude) * Math.cos(B_rad.longitude - A_rad.longitude)
    val By = Math.cos(B_rad.latitude) * Math.sin(B_rad.longitude - A_rad.longitude)

    val latMid = Math.atan2(Math.sin(A_rad.latitude) + Math.sin(B_rad.latitude), Math.sqrt((Math.cos(A_rad.latitude) + Bx) * (Math.cos(A_rad.latitude) + Bx) + By * By))
    val lonMid = A_rad.longitude + Math.atan2(By, Math.cos(A_rad.latitude) + Bx)

    toDegree(new Point(latMid, lonMid, A_rad.weight + B_rad.weight))
  }

}
