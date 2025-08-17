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
  // Add later method implementation here
  if (!wifi.isOnline()) {
    return wifi.begin();
  }
  return true;
}

std::optional<Top2Items> APIController::getTasks(HTTPClient http) {
  Top2Items tasks;
  
  // Set up the internet connection
  if (!wifi.isOnline()) {
    if (!wifi.begin()) {
      // Return an error
      return std::nullopt;
    }
  }

  // We have internet, now start sending!
  http.begin("localhost:8080/taskApi/tasks?name=My%20Day");
  int httpCode = http.GET();
  if (httpCode != 200) {
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

