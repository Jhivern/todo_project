#include "ConfigLoader.h"
#include <SPIFFS.h> // File manager, since we dont have an OS
#include <ArduinoJson.h>

ConfigLoader::ConfigLoader(const char* path) {
  // Initialize SPIFFS here
  if (!SPIFFS.begin()) {
    // Set enum code
    configStatus = CONFIG_SPIFFS_FAIL;
    return;
  }

  // Find the config
  File file = SPIFFS.open(path, "r");
  if (!file) {
    configStatus = CONFIG_FILE_NOT_FOUND; // File not found
    return;
  }

  // Deserialize JSON
  StaticJsonDocument<256> doc; // 64+32=86 + more bytes to store JSON internally (Might be lowered later if possible)
  DeserializationError error = deserializeJson(doc, file);
  file.close();

  if (error) {
    configStatus = CONFIG_PARSE_ERROR;
    return;
  }

  // Safely copy values into fixed buffers 
  const char* jsonSsid = doc["ssid"] | ""; // If doc["ssid"] does not exist, then default to ""
  const char* jsonPassword = doc["password"] | "";

  // Check for max sizes
  if (strlen(jsonSsid) >= 32 || strlen(jsonPassword) >= 64) {
    configStatus = CONFIG_PARSE_ERROR;
    return;
  }

  // Copy the strings into the buffers
  strncpy(ssid, jsonSsid, sizeof(ssid)); // Copy, and pad all unused space with null terminators
  ssid[sizeof(ssid) - 1] = '\0'; // Null termination just in case

  strncpy(password, jsonPassword, sizeof(password));
  password[sizeof(password) - 1] = '\0';

  configStatus = CONFIG_OK; // Confirm the loading went OK
}

// Get SSID
const char* ConfigLoader::getSsid() {
  return ssid;
}

// Get password
const char* ConfigLoader::getPassword() {
  return password;
}