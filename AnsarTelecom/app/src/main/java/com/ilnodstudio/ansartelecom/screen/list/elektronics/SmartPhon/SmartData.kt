package com.ilnodstudio.ansartelecom.screen.list.elektronics.SmartPhon

import java.io.Serializable

data class SmartData(
    val category: String = "",
    val model: String = "",
    val brandName: String = "", // Добавили поле для названия бренда
    val title: String = "",
    val Price: Int = 0,
    val description: String = "",
    val imgUrlList: List<String> = emptyList()
): Serializable
