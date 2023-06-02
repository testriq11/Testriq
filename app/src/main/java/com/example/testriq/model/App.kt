package com.example.testriq.model

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.os.Parcel
import android.os.Parcelable
import com.example.testriq.R

//data class App(val name: String, val icon: Drawable, var isSelected: Boolean = false)

//data class App(
//    val name: String,
//    val packageName: String,
//    var isSelected: Boolean = false
//)

data class App(val name: String, val packageName: String,val icon: Drawable,var isSelected: Boolean) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readParcelable(Drawable::class.java.classLoader)!!,
        parcel.readByte() != 0.toByte()
    )
//    val app=App(name, packageName, icon, isSelected)
//    val context = applicationContext
//    val dataWithContext = DataWithContext(app, context)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(packageName)
//        val iconBitmap = (icon as BitmapDrawable).bitmap

// Remove icon then we can run app woth string name and checkbox
        parcel.writeParcelable(icon, flags)
//            parcel.writeParcelable(icon, flags)
        parcel.writeByte(if (isSelected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<App> {
        override fun createFromParcel(parcel: Parcel): App {
            return App(parcel)
        }

        override fun newArray(size: Int): Array<App?> {
            return arrayOfNulls(size)
        }
    }
}

//data class App(val name: String, val packageName: String, val isSelected: Boolean) : Parcelable {
//    constructor(parcel: Parcel) : this(
//        parcel.readString()!!,
//        parcel.readString()!!,
//        parcel.readByte() != 0.toByte()
//    )
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(name)
//        parcel.writeString(packageName)
//        parcel.writeByte(if (isSelected) 1 else 0)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<App> {
//        override fun createFromParcel(parcel: Parcel): App {
//            return App(parcel)
//        }
//
//        override fun newArray(size: Int): Array<App?> {
//            return arrayOfNulls(size)
//        }
//    }
//}

