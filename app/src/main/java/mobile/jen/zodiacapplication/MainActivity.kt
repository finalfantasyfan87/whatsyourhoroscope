package mobile.jen.zodiacapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.springframework.http.*
import org.springframework.web.client.RestTemplate


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var sunSign = "Aries"
    var resultView: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var buttonView: Button = findViewById(R.id.button)
        button.setOnClickListener {
            GlobalScope.async {
                getPredictions(buttonView)
            }

        }

        val spinner = findViewById<Spinner>(R.id.spinner)
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.sunsigns,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter;
        spinner.onItemSelectedListener = this

        resultView = findViewById(R.id.resultView)

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            sunSign = parent.getItemAtPosition(position).toString();
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        sunSign = "Aries"
    }

    public suspend fun getPredictions(view: android.view.View) {
        try {
            val result = withContext(Dispatchers.Default) {
                callAztroAPI("https://sameer-kumar-aztro-v1.p.rapidapi.com/", sunSign, "today")
            }
            parseData(result as ResponseEntity<Any>)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun callAztroAPI(apiUrl: String, sign: String, date: String): Any {
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


    private fun parseData(result: ResponseEntity<Any>) {
        try {
            val predictionData = Gson().fromJson(result.body.toString(), Prediction::class.java)
            Log.d("parseData", predictionData.description)



            var prediction = "Today's prediction for " + this.sunSign + " is" + "\n"


            prediction += predictionData.description

            setText(this.resultView, prediction)

        } catch (e: Exception) {
            e.printStackTrace()
            this.resultView!!.text = "Oops!! something went wrong, please try again"
        }
    }

    private fun setText(text: TextView?, value: String) {
        runOnUiThread { text!!.text = value }
    }
}