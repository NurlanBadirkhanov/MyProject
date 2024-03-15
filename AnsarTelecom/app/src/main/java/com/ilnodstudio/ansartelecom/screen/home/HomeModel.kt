package com.ilnodstudio.ansartelecom.screen.home

data class HomeModel(
    val brandName: String = "",
    val category: String = "",
    val mainCategory: String = "",
    val subCategory: String = "",
    val model:String = "",
    val title: String = "",
    val Price: Int = 0,
    val description: String = "",
    val itemKey:String = "",
    val imgUrlList: List<String> = emptyList() // Добавьте это поле для списка URL-ов изображений
): java.io.Serializable
