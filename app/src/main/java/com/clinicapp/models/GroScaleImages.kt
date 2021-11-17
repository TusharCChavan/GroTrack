package com.clinicapp.models

import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
@Parcelize
@JsonClass(generateAdapter = true)
class GroScaleImages(
    val portrait:List<GroScaleImagesDetail>,
  val closeup:List<GroScaleImagesDetail>,
    val threex_closeup:List<GroScaleImagesDetail>,
) : Parcelable {
}