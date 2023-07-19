package com.hasan.jetfasthub.screens.main.profile.model.gist_model.dto

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.hasan.jetfasthub.screens.main.profile.model.gist_model.File
import com.hasan.jetfasthub.screens.main.profile.model.gist_model.Files
import java.lang.reflect.Type


class Files(){

    val gson = Gson()
    val jsonData = "{\n" +
            "            \"refactoring.kt\": {\n" +
            "                \"filename\": \"refactoring.kt\",\n" +
            "                \"type\": \"text/plain\",\n" +
            "                \"language\": \"Kotlin\",\n" +
            "                \"raw_url\": \"https://gist.githubusercontent.com/kirich1409/330eaf442c77f93889ec1d6dd53a70a1/raw/af5f317baff34ada3d03171ce7f54506ec6aa29e/refactoring.kt\",\n" +
            "                \"size\": 420\n" +
            "            }\n" +
            "        }"

    var listType: Type = object : TypeToken<Map<String?, File?>?>() {}.type

    val myList: Map<String, File> = gson.fromJson(jsonData, listType)

    val parser = JsonParser()

//    var myObj: DemoFiles = gson.fromJson(
//        jsonData,
//        object : TypeToken<DemoFiles?>() {}.type
//    )


}

class sd(){

    val jsonData = "{\n" +
            "            \"refactoring.kt\": {\n" +
            "                \"filename\": \"refactoring.kt\",\n" +
            "                \"type\": \"text/plain\",\n" +
            "                \"language\": \"Kotlin\",\n" +
            "                \"raw_url\": \"https://gist.githubusercontent.com/kirich1409/330eaf442c77f93889ec1d6dd53a70a1/raw/af5f317baff34ada3d03171ce7f54506ec6aa29e/refactoring.kt\",\n" +
            "                \"size\": 420\n" +
            "            }\n" +
            "        }"

    private var deserializer: JsonDeserializer<Files> =
        JsonDeserializer<Files> { json, typeOfT, context ->
            val jsonObject = json.asJsonObject
            val file = File(
                jsonObject["filename"].asString,
                jsonObject["language"].asString,
                jsonObject["raw_url"].asString,
                jsonObject["size"].asInt,
                jsonObject["type"].asString,

                )
            Files(
                file
            )
        }

    private var gsonBuilder: GsonBuilder = GsonBuilder().registerTypeAdapter(Files::class.java, deserializer)

    var customGson: Gson = gsonBuilder.create()
    var customObject: File = customGson.fromJson(jsonData, File::class.java)



}


