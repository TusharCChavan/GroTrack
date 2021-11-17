package com.clinicapp.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
class Admin_Profile(
    val id:Long?,
    val practiceName:String?,
    val physicianName:String?,
  val address:MDAddress?,

) : Parcelable {
}