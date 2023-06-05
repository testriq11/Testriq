package com.example.testriq.model

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable

class DrawableParcelable(private val drawable: Drawable) : Parcelable {

    // Convert the Drawable to a Bitmap
    private val bitmap: Bitmap
        get() {
            val bitmapDrawable = drawable as BitmapDrawable
            return bitmapDrawable.bitmap
        }

    // Implement Parcelable interface


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest?.writeParcelable(bitmap, flags)
    }

    companion object CREATOR : Parcelable.Creator<DrawableParcelable> {
        override fun createFromParcel(parcel: Parcel): DrawableParcelable {
            val bitmap = parcel.readParcelable<Bitmap>(Bitmap::class.java.classLoader)
            val drawable = BitmapDrawable(bitmap)
            return DrawableParcelable(drawable)
        }

        override fun newArray(size: Int): Array<DrawableParcelable?> {
            return arrayOfNulls(size)
        }
    }
}