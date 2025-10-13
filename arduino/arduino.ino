#include "DeviceController.h"
#include "WifiManager.h"
#include "APIController.h"
#include <HTTPClient.h>

WifiManager wifiManager("SSID", "Password");
APIController apiController(wifiManager);
DeviceController deviceController;
HTTPClient client;
int timeThen;

void setup() {
  // put your setup code here, to run once:
  // Initialize objects
  wifiManager.begin();
  deviceController.begin();
  
  if (apiController.begin()) {
    Serial.begin(115200);
    std::optional<Top2Items> items = apiController.getTasks(client);
    Serial.println("Tasks sent!");
    Top2Items tasks = items.value_or(Top2Items{"Failed", "Failed"});
    Serial.println(tasks.task1);
    Serial.println(tasks.task2);

    // Send to screen!
    
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
    deviceController.clearLCD();
    deviceController.writeLine0(tasks.task1);
    deviceController.writeLine1(tasks.task2);
  }
}
