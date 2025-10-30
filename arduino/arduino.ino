#include "DeviceController.h"
#include "WifiManager.h"
#include "APIController.h"
#include <HTTPClient.h>
#include "wifi_config.h"
enum TaskState {
    OFFLINE,
    IDLE,
    SYNC
};

WifiManager wifiManager(WIFI_SSID, WIFI_PASSWORD);
APIController apiController(wifiManager);
DeviceController deviceController;
HTTPClient client;
TaskState currentState;
TaskState nextState;
unsigned long timeThen;

void setup() {
    currentState = TaskState::OFFLINE;
    deviceController.begin();
    wifiManager.begin();
    timeThen = millis();
}

void loop() {
    // Check connectivity first
    if (!wifiManager.isOnline() && currentState != TaskState::OFFLINE) {
        nextState = OFFLINE;
    } else {
        switch (currentState) {
            case OFFLINE: handleOffline(); break;
            case IDLE: handleIdle(); break;
            case SYNC: handleSync(); break;
            default: nextState = TaskState::OFFLINE; break;
        }
    }

    // Update state at the very end of loop
    currentState = nextState;
}

void handleOffline() {
    static unsigned long lastAttempt = 0;
    if (millis() - lastAttempt > 3000) {
        wifiManager.begin();
        lastAttempt = millis();
    }
    nextState = wifiManager.isOnline() ? IDLE : OFFLINE;
}

void handleIdle() {
    if (millis() - timeThen > 5000) {
        nextState = SYNC;
        timeThen = millis();
    } else {
        nextState = IDLE;
    }
}

void handleSync() {
    static char* lastTask1 = "";
    static char* lastTask2 = "";
    std::optional<Top2Items> items = apiController.getTasks(client);
    Top2Items tasks = items.value_or(Top2Items{"Failed", "Failed"});

    // Check if data has changed, and if so write it to screen
    if (strcmp(lastTask1, tasks.task1) != 0) {
        lastTask1 = tasks.task1;
        deviceController.writeLine1(tasks.task1);
    }

    if (strcmp(lastTask2, tasks.task2) != 0) {
        lastTask2 = tasks.task2;
        deviceController.writeLine2(tasks.task2);
    }

    // Set the next state
    nextState = IDLE;
}
