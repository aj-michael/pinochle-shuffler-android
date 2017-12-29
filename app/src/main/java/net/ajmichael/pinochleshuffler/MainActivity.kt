package net.ajmichael.pinochleshuffler

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.URL

class MainActivity : AppCompatActivity() {

  companion object {
    val JSON = MediaType.parse("application/json; charset=utf-8")
    val SHUFFLER_SERVER = URL("https://ajmichael.net/pinochle")
  }

  private val okhttp = OkHttpClient()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  fun deal(view: View) {
    val phoneNumbers =
        intArrayOf(
            R.id.phone1EditText, R.id.phone2EditText, R.id.phone3EditText, R.id.phone4EditText)
        .map { phoneId -> findViewById<EditText>(phoneId) }
        .map { editText -> editText.text.toString() }
    val pinochleButton = findViewById<RadioButton>(R.id.radio_pinochle)
    val gameMode = if (pinochleButton.isChecked) GameMode.PINOCHLE else GameMode.EUCHRE
    sendDealRequest(phoneNumbers, gameMode)
  }

  private fun sendDealRequest(phoneNumbers: List<String>, gameMode: GameMode) {
    val request =
        JSONObject()
            .put("phone", JSONArray(phoneNumbers))
            .put("euchre", if (gameMode == GameMode.EUCHRE) "on" else "off")
            .toString()
    okhttp.newCall(
        Request.Builder()
            .url(SHUFFLER_SERVER)
            .post(RequestBody.create(JSON, request))
            .build())
        .enqueue(object: Callback {
          override fun onResponse(call: Call?, response: Response?) {
          }

          override fun onFailure(call: Call?, e: IOException?) {
          }
        })
  }
}
