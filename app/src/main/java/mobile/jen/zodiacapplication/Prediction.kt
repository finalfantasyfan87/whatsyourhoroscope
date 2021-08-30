package mobile.jen.zodiacapplication

import com.fasterxml.jackson.annotation.JsonProperty


data class Prediction (
        val date_range : String,
        val current_date : String,
        val description : String,
        val compatibility : String,
        val mood : String,
        val color : String,
        val lucky_number : Int,
        val lucky_time : String
    ) {
        constructor() : this("","","","","","",0,"")
}