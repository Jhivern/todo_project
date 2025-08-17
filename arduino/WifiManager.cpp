#include "WifiManager.h"
#include <WiFi.h>
// Include the config file later
WifiManager::WifiManager(const char* ssid, const char* password) 
  : ssid(ssid), password(password)
{
}

// Initializes the WifiManager to connect to a predetermined access point
bool WifiManager::begin() {
  // Wifi is already connected
  if (WiFi.status() == WL_CONNECTED) {
    return true;
  }

  Serial.begin(9600);
  Serial.print("Connecting to wifi");
  WiFi.begin(ssid, password);

  // Try to connect
  short retries = 0;
  while (WiFi.status() != WL_CONNECTED && retries < 20) {
    retries++;
    delay(500);
    Serial.print(".");
  }

  if (WiFi.status() != WL_CONNECTED) {
    Serial.println("\nConnection failed.");
    Serial.end();
    return false;
  }

  Serial.println("\nSuccessfully connected!");
  Serial.print("Your IP: ");
  Serial.println(WiFi.localIP());

  // Close serial
  Serial.end();

  return true;
}

bool WifiManager::isOnline() {
  return WiFi.status() == WL_CONNECTED;
}