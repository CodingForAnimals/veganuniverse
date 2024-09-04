package org.codingforanimals.veganuniverse.commons.data.utils

object DataUtils {

    /**
     * Splits all searchable fields into substrings and returns them as list.
     * This method is used specifically for creating keywords for our text search implementations.
     * As an example, inputs ['Titulo producto', 'Subtitulo producto'] will return ['tit', 'titu', 'titul', 'titulo', 'titulo p', 'titulo pr', 'titulo pro', 'titulo prod', 'titulo produ', 'titulo produc', 'titulo product', 'titulo producto', 'pro', 'prod', 'produ', 'produc', 'product', 'producto', 'sub', 'subt', 'subti', 'subtit', 'subtitu', 'subtitul', 'subtitulo', 'subtitulo p', 'subtitulo pr', 'subtitulo pro', 'subtitulo prod', 'subtitulo produ', 'subtitulo produc', 'subtitulo product', 'subtitulo producto']'
     */
    fun createKeywords(vararg searchableFields: String?): List<String> {
        val keywords = mutableListOf<String>()
        fun addToKeywordsIfLongEnough(string: String, index: Int) {
            if (index >= 2) {
                val stringToAdd = string.substring(0, index + 1).lowercase().trim()
                if (!keywords.contains(stringToAdd)) {
                    keywords.add(stringToAdd)
                }
            }
        }
        searchableFields.forEach { string ->
            if (string == null) return@forEach

            // Split full string in substrings and add them to keywords "pepe argento" -> "pep" "pepe" "pepe " "pepe a" "pepe ar" ...
            string.forEachIndexed { index, _ ->
                addToKeywordsIfLongEnough(string, index)
            }

            // Split full string in words, and repeat process "pepe argento" -> "pep" "pepe" - "arg" "arge" "argen" "argent" "argento"
            string.split(" ", "-", "_").takeIf { it.size > 1 }?.forEachIndexed { index, word ->
                if (index >= 1) {
                    word.forEachIndexed { wordIndex, _ ->
                        addToKeywordsIfLongEnough(word, wordIndex)
                    }
                }
            }
        }
        return keywords
    }
}
