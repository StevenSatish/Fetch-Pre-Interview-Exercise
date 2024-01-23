package com.example.fetchexercise
import android.content.Context
import org.json.JSONArray
import java.io.IOException
import java.io.InputStream

class JsonFileReader {
    companion object {
        fun loadJSONFromAsset(context: Context, fileName: String): String? {
            var jsonString: String? = null
            try {
                val inputStream: InputStream = context.assets.open(fileName)
                val size: Int = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                jsonString = String(buffer, Charsets.UTF_8)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return jsonString
        }

        fun parseJSON(jsonString: String?): JSONArray? {
            try {
                return JSONArray(jsonString)

            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }
}
