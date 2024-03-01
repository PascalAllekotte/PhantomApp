package de.syntax.androidabschluss.data.model

data class TranslationRequest(
    val text: List<String>,
    val target_lang: String
)