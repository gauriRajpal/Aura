package com.example.aura

fun normalizeNumber(input: String): String? {

    val clean = input.replace(" ", "")
        .replace("-", "")
        .trim()

    return when {
        clean.startsWith("+91") && clean.length == 13 -> clean
        clean.length == 10 -> "+91$clean"
        else -> null
    }
}
