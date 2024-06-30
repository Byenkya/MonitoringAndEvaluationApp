package com.example.monitoringandevaluationapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.locationtech.jts.io.WKTWriter


@Entity
data class PdmProjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val geom: String,
    val uuid: String,
    val groupId: Long,
    val groupName: String,
    val projNo: String,
    val projName: String,
    val projFocus: String,
    val projDesc: String,
    val startDate: String,
    val endDate: String,
    val fundedBy: String,
    val amount: Long,
    val teamLeader: String,
    val email: String,
    val telephone: String,
    val status: String,
    val createdBy: String,
    val dateCreated: String,
    val updatedBy: String,
    val dateUpdated: String,
    val lat: Double,
    val longitude: Double,
    val uploaded: Boolean
)
