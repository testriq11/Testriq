package com.example.testriq.day_analyzer_module.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trace_locations")
data class TraceLocation(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long,
    var isSelected: Boolean = false

)  : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeLong(timestamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TraceLocation> {
        override fun createFromParcel(parcel: Parcel): TraceLocation {
            return TraceLocation(parcel)
        }

        override fun newArray(size: Int): Array<TraceLocation?> {
            return arrayOfNulls(size)
        }
    }
}

