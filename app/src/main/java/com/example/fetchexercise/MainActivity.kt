package com.example.fetchexercise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fetchexercise.ui.theme.FetchExerciseTheme
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FetchExerciseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val fileName = "hiring.json"
                    var jsonArray: JSONArray? = null
                    val jsonString = JsonFileReader.loadJSONFromAsset(this, fileName)
                    if (jsonString != null) {
                        jsonArray = JsonFileReader.parseJSON(jsonString)
                        jsonArray = OrganizeList(jsonArray)
                    }
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)) {
                        Row (modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically){
                            Text(
                                text = "Fetch Rewards Exercise",
                                fontSize = 18.sp,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),

                            )
                        }
                        DisplayList(jsonArray)
                    }

                }
            }
        }
    }

    @Composable
    fun DisplayList(jsonArray: JSONArray?) {
        LazyColumn() {
            items(jsonArray?.length() ?: 0) { index ->
                val jsonObject = jsonArray?.getJSONObject(index)
                val name = jsonObject?.optString("name", "")
                val listId = jsonObject?.optInt("listId", 0)
                val id = jsonObject?.optInt("id", 0)
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "ID: $id ListId: $listId name: $name"
                    )
                }
                Spacer(modifier = Modifier.padding(5.dp))
            }
        }
    }

    fun OrganizeList(inputArray: JSONArray?): JSONArray {
        val filteredList = mutableListOf<JSONObject>()

        if (inputArray != null) {
            // Filter out blank or null names
            for (i in 0 until inputArray.length()) {
                val jsonObject = inputArray.getJSONObject(i)
                val name = jsonObject.optString("name", "")
                if (name != "" && name != "null") {
                    filteredList.add(jsonObject)
                }
            }
        }

        // Sort by listID, then by the numberical part of the name
        filteredList.sortWith(compareBy({ it.getInt("listId") }, { extractNumericPart(it.optString("name", "")) }))

        // Create a new JSONArray with the filtered and sorted items
        return JSONArray(filteredList)
    }

    private fun extractNumericPart(name: String): Int {
        val numericPart = name.replace(Regex("[^0-9]"), "")
        return if (numericPart.isNotBlank()) numericPart.toInt() else Int.MAX_VALUE
    }



}