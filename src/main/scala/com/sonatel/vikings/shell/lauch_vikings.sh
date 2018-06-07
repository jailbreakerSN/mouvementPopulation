#!/bin/bash

export SPARK_MAJOR_VERSION=2

spark-submit
--jars /root/sonatel/poc_geoloc/jars/config-1.3.1.jar
--class com.sonatel.poc_geoloc.objects.Generator
--master local
--executor-memory 2g
--total-executor-cores 2
/root/poc_geoloc_vip_2.11-0.1.jar
/root/sonatel/poc_geoloc/conf
