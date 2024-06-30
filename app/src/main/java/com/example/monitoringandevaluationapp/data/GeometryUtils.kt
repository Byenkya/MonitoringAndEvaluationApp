package com.example.monitoringandevaluationapp.data
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.locationtech.jts.io.WKTWriter
import org.locationtech.jts.io.WKBWriter

object GeometryUtils {
    private val geometryFactory = GeometryFactory()
    private val wkbWriter = WKBWriter()

    fun createGeomFromLatLng(latitude: Double, longitude: Double): ByteArray {
        val point = createPoint(latitude, longitude)
        return wkbWriter.write(point)
    }

    private fun createPoint(latitude: Double, longitude: Double): Point {
        val coordinate = Coordinate(longitude, latitude)
        return geometryFactory.createPoint(coordinate)
    }

    fun byteArrayToHexString(byteArray: ByteArray): String {
        return byteArray.joinToString("") { byte -> "%02X".format(byte) }
    }
}