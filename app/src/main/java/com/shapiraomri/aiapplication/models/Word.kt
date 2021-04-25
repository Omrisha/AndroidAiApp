package com.shapiraomri.aiapplication.models

data class Word(
        val text: String,
        val probability: Int,
        val top: Float,
        val bottom: Float,
        val left: Float,
        val right: Float) {
}