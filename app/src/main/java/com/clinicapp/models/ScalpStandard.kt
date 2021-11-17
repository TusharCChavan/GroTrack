package com.clinicapp.models
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
@Parcelize
@JsonClass(generateAdapter = true)
class ScalpStandard(
    val id:Long,
    val image_path:String,
    val description:String
) : Parcelable {
}