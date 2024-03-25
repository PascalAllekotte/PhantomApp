package de.syntax.androidabschluss.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import de.syntax.androidabschluss.data.model.open.Data

@JsonClass(generateAdapter = true)
data class ImageResponse(
    val created: Int,
    @Json(name = "data")
    val data: List<Data>
)

