package com.clinicapp.models
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
@Parcelize
@JsonClass(generateAdapter = true)
class GroScaleImagesDetail(
 val image_path:String,
    val mainType:String,
   val subType:String

) : Parcelable {
}