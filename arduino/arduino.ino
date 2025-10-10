#include "DeviceController.h"
#include "WifiManager.h"
#include "APIController.h"
#include <HTTPClient.h>

WifiManager wifiManager("SSID", "Password");
APIController apiController(wifiManager);


void setup() {
  HTTPClient client;
  // put your setup code here, to run once:
  // Initialize objects
  wifiManager.begin();
  
  if (apiController.begin()) {
    Serial.begin(115200);
    std::optional<Top2Items> items = apiController.getTasks(client);
    Serial.println("Tasks sent!");
    Top2Items tasks = items.value_or(Top2Items{"Failed", "Failed"});
    Serial.println(tasks.task1);
    Serial.println(tasks.task2);
  }
  // client.end();
}

void loop() {
  // put your main code here, to run repeatedly:

}
