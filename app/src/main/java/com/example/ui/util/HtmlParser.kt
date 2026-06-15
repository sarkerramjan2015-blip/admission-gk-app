package com.example.ui.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

fun String.parseHtml(): AnnotatedString {
    return buildAnnotatedString {
        var remaining = this@parseHtml
        while (remaining.isNotEmpty()) {
            if (remaining.startsWith("<font color=\"#D32F2F\"><b>")) {
                val endTag = "</b></font>"
                val endIndex = remaining.indexOf(endTag)
                if (endIndex != -1) {
                    val content = remaining.substring(27, endIndex)
                    withStyle(style = SpanStyle(color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold)) {
                        append(content)
                    }
                    remaining = remaining.substring(endIndex + endTag.length)
                } else { append(remaining); remaining = "" }
            } else if (remaining.startsWith("<font color=\"#1976D2\">")) {
                val endTag = "</font>"
                val endIndex = remaining.indexOf(endTag)
                if (endIndex != -1) {
                    val content = remaining.substring(23, endIndex)
                    withStyle(style = SpanStyle(color = Color(0xFF1976D2))) {
                        append(content)
                    }
                    remaining = remaining.substring(endIndex + endTag.length)
                } else { append(remaining); remaining = "" }
            } else {
                val nextTag = remaining.indexOf("<")
                if (nextTag == -1) {
                    append(remaining)
                    remaining = ""
                } else {
                    append(remaining.substring(0, nextTag))
                    remaining = remaining.substring(nextTag)
                }
            }
        }
    }
}
