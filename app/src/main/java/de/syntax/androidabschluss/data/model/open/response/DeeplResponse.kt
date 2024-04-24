package de.syntax.androidabschluss.data.model.open.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class DeeplResponse(
    @Json(name = "translations")  val translations: List<Translation>
)