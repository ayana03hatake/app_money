package com.example.money_add_app;

import java.io.FileReader;
import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class App {
    public static void main(String[] args) {
        try (FileReader reader = new FileReader("data.json")) {
            
            JsonElement jsonElement = JsonParser.parseReader(reader);
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            // JSONデータのフィールドを取得!!!!!確認
            String name = jsonObject.get("name").getAsString();
            int age = jsonObject.get("age").getAsInt();

            System.out.println("Name: " + name);
            System.out.println("Age: " + age);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}