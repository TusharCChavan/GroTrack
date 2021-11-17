package com.clinicapp.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
class ClientAnalysis(
    val id:Long,
    val created_at:Long,
    val images:GroScaleImages,
    val data:DataIndex
) : Parcelable {
}