package de.syntax.androidabschluss.data.model

import com.aallam.openai.api.audio.Translation

data class DeepLResponse(val translation: List<Translation>)
