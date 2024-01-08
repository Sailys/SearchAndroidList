package com.searchandroidlist.data

data class SearchData(
    val name: NameData,
    val tld: List<String>,
    val independent: Boolean,
    val status: String,
    val unMember: Boolean,
    val capital: List<String>,
    val altSpellings: List<String>,
    val region: String,
    val subregion: String,
    val languages: Languages,
    val latlng: List<String>,
    val maps: Maps,
    val timezones: List<String>,
    val continents: List<String>,
    val flags: Flags
)

data class Flags(
    val png: String,
    val svg: String
)

data class Languages(
    val eng: String
)

data class Maps(
    val googleMaps: String,
    val openStreetMaps: String
)