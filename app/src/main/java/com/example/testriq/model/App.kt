package com.example.testriq.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import androidx.core.graphics.drawable.toBitmap
import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.Parceler


//data class App(val name: String, val icon: Drawable, var isSelected: Boolean = false)

//data class App(
//    val name: String,
//    val packageName: String,
//    var isSelected: Boolean = false
//)
@Parcelize
data class App(val name: String, val packageName: String, val icon: Bitmap, var isSelected: Boolean) :Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
//        parcel.readParcelable(Drawable::class.java.classLoader) ?: throw IllegalArgumentException("Icon cannot be null"),
        parcel.readParcelable(Bitmap::class.java.classLoader)!!,
          parcel.readByte() != 0.toByte()
    )
//    val app=App(name, packageName, icon, isSelected)
//    val context = applicationContext
//    val dataWithContext = DataWithContext(app, context)

    // Convert the Drawable to a Bitmap


    companion object : Parceler<App> {

        override fun App.write(parcel: Parcel, flags: Int) {
            parcel.writeString(name)
            parcel.writeString(packageName)
//            val drawableParcelable = DrawableParcelable(icon.b)
    //        val iconBitmap = (icon as BitmapDrawable).bitmap
    //        parcel?.writeParcelable(icon, flags)
    // Remove icon then we can run app woth string name and checkbox
            parcel.writeParcelable(icon, flags)

            parcel.writeByte(if (isSelected) 1 else 0)
        }

        override fun create(parcel: Parcel): App {
            return App(parcel)
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

