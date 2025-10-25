#include "DeviceController.h"
#include "WifiManager.h"
#include "APIController.h"
#include <HTTPClient.h>
#include "wifi_config.h"
enum TaskState {
  DISPLAY_TOP,
  DISPLAY_BOTTOM,
  TASK_COMPLETED,
  SYNCING,
  IDLE
};

// Replace configLoader with wifi_config.h
WifiManager wifiManager(WIFI_SSID, WIFI_PASSWORD);
APIController apiController(wifiManager);
DeviceController deviceController;
HTTPClient client;
int timeThen;

void setup() {
  // put your setup code here, to run once:
  // Initialize objects
  Serial.begin(115200);
  Serial.println(WIFI_SSID);
  wifiManager.begin();
  deviceController.begin();
  
  if (apiController.begin()) {
    Serial.begin(115200);
    std::optional<Top2Items> items = apiController.getTasks(client);
    Serial.println("Tasks sent!");
    Top2Items tasks = items.value_or(Top2Items{"Failed", "Failed"});
    Serial.println(tasks.task1);
    Serial.println(tasks.task2);

    Send to screen!
    
    deviceController.writeLine0(tasks.task1);
    deviceController.writeLine1(tasks.task2);
    
  }
  timeThen = millis();
  // client.end();
}

void loop() {
  // put your main code here, to run repeatedly:
  if (millis() - timeThen > 5000) {
    timeThen = millis();
    std::optional<Top2Items> items = apiController.getTasks(client);
    Top2Items tasks = items.value_or(Top2Items{"Failed", "Failed"});
    deviceController.clearLCD();
    deviceController.writeLine0(tasks.task1);
    deviceController.writeLine1(tasks.task2);
  }
}
