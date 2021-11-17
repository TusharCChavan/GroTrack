package com.clinicapp.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
class MDAddress(
    val street:String,
    val suite:String,
  val city:String,
    val state:String,
    val zip:String,

) : Parcelable {
}