package mobile.jen.zodiacapplication.view

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import mobile.jen.zodiacapplication.R
import mobile.jen.zodiacapplication.service.ZodiacService
import org.springframework.http.ResponseEntity


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
        val zodiacService = ZodiacService();
        try {
            val result = withContext(Dispatchers.Default) {
                zodiacService.callAztroAPI(
                    "https://sameer-kumar-aztro-v1.p.rapidapi.com/",
                    sunSign,
                    "today"
                )
            }
            val response = zodiacService.parseData(result as ResponseEntity<Any>, this.sunSign)
            setText(this.resultView, response)

        } catch (e: Exception) {
            e.printStackTrace()
            this.resultView!!.text = "An error has occurred. Please try again."
        }
    }


    private fun setText(text: TextView?, value: String) {
        runOnUiThread { text!!.text = value }
    }
}


