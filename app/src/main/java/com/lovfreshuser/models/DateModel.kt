package com.lovfreshuser.models

import java.util.*

data class DateModel(
    val month: String,
    val day_str: String,
    val day_int: String,
    val is_selected: Boolean,
    val date: String,
     var time: List<TimeModel>
)

data class ListOfDate(
    val listOfSlot: List<DateModel>
)