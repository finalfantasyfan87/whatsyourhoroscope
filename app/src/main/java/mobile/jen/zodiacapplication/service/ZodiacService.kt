package mobile.jen.zodiacapplication.service

import android.widget.TextView
import com.google.gson.Gson
import mobile.jen.zodiacapplication.model.Prediction
import org.springframework.http.*
import org.springframework.web.client.RestTemplate

class ZodiacService {
     fun callAztroAPI(apiUrl: String, sign: String, date: String): Any {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-rapidapi-host", "sameer-kumar-aztro-v1.p.rapidapi.com")
        headers.set("x-rapidapi-key", "8b9d003124msh378df7fcd812012p1617a9jsn6ac2783301cb")
        val request: HttpEntity<*> = HttpEntity<Any?>(headers)
        val response: Any =
            restTemplate.exchange(
                "$apiUrl?sign=$sign&day=$date",
                HttpMethod.POST,
                request,
                String::class.java
            )
        return response
    }

     fun parseData(result: ResponseEntity<Any>, sunSign:String): String {
         val predictionData = Gson().fromJson(result.body.toString(), Prediction::class.java)
         var prediction = predictionData.current_date +" prediction for "+sunSign+"("+predictionData.date_range + ")" +"\n" +"\n"
         prediction += predictionData.description
         return prediction
     }

}