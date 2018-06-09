/*
 * Copyright (c) 2018. Arash Hatami
 */
package util.extensions

import java.text.Normalizer

/**
 * Strip the accents from a string
 */
fun CharSequence.removeAccents() = Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace(Regex("[^\\p{ASCII}]"), "")