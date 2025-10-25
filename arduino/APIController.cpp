#include <HTTPClient.h>
#include "APIController.h"
#include "Top2Items.h"
#include <optional>
// Might wanna remove later
#include <ArduinoJson.h>

APIController::APIController(WifiManager& wifi) : wifi(wifi) {
  // Add later method implementations here
}

bool APIController::begin() {
  if (!wifi.isOnline()) {
    return wifi.begin();
  }
  return true;
}

std::optional<Top2Items> APIController::getTasks(HTTPClient& http) {
  Top2Items tasks;
  
  // Set up the internet connection
  Serial.begin(115200);
  Serial.println("Checking WiFi status");
  if (!wifi.isOnline()) {
    Serial.println("Trying to reconnect...");
    if (!wifi.begin()) {
      Serial.println("Wifi connection failed");
      // Return an error
      return std::nullopt;
    }
  }

  // We have internet, now start sending!
  Serial.println("Begin sending the Request!");
  http.begin("http://192.168.1.3:8080/taskApi/tasks?name=My%20Day");
  Serial.println("Finished request/response sequence");
  int httpCode = http.GET();
  Serial.println(httpCode);
  if (httpCode != 200) {
    Serial.println("Code was not good :(");
    Serial.println(httpCode);
    return std::nullopt;
  }
  else {
    String body = http.getString();
    StaticJsonDocument<150> jsonDoc;
    DeserializationError error = deserializeJson(jsonDoc, body);

    if (error) {
      Serial.print("JSON parse failed: ");
      Serial.println(error.c_str());
      return std::nullopt;
    }

    JsonArray arr = jsonDoc.as<JsonArray>();
    if (arr.size() < 2) {
      Serial.println("Error: Less than 2 tasks received");
      return std::nullopt;
    }

    // Copy strings into char[] buffers
    // Check for off-by-one size errors!
    strncpy(tasks.task1, arr[0].as<const char*>(), sizeof(tasks.task1) - 1);
    tasks.task1[sizeof(tasks.task1) - 1] = '\0';  // Add null terminator

    strncpy(tasks.task2, arr[1].as<const char*>(), sizeof(tasks.task2) - 1);
    tasks.task2[sizeof(tasks.task2) - 1] = '\0';  // Add null terminator

    // Debug
    Serial.println(tasks.task1);
    Serial.println(tasks.task2);

  }

  return tasks;
}