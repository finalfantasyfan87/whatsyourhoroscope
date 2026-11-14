package mobile.jen.zodiacapplication.service

import com.google.gson.Gson
import mobile.jen.zodiacapplication.model.Prediction
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class ZodiacService {

    // Returns the raw JSON body. Throws IOException on any non-2xx response.
    fun callAztroAPI(apiUrl: String, sign: String, date: String): String {
        val query = "sign=${URLEncoder.encode(sign, "UTF-8")}&day=${URLEncoder.encode(date, "UTF-8")}"
        val connection = URL("$apiUrl?$query").openConnection() as HttpURLConnection
        try {
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("x-rapidapi-host", "sameer-kumar-aztro-v1.p.rapidapi.com")
            connection.setRequestProperty("x-rapidapi-key", "8b9d003124msh378df7fcd812012p1617a9jsn6ac2783301cb")
            val code = connection.responseCode
            val stream = if (code in 200..299) connection.inputStream else connection.errorStream
            val body = stream?.bufferedReader()?.use { it.readText() } ?: ""
            if (code !in 200..299) throw IOException("HTTP $code: $body")
            return body
        } finally {
            connection.disconnect()
        }
    }

    fun parseData(json: String, sunSign: String): String {
        val predictionData = Gson().fromJson(json, Prediction::class.java)
        var prediction =
            "${predictionData.current_date} prediction for $sunSign(${predictionData.date_range})\n\n"
        prediction += predictionData.description
        return prediction
    }
}
