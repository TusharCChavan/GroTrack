package com.clinicapp.models
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
@Parcelize
@JsonClass(generateAdapter = true)
class GroScale(
    val front:String,
    val crown:String,
    val vertex:String,
  val left:String,
val right:String
) : Parcelable {
}