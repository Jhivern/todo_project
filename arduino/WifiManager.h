#ifndef WIFIMANAGER_H
#define WIFIMANAGER_H

namespace WifiManager {
  // Add later method definitions here
  public:
    WifiManager(char* ssid, char* password);
    bool begin();
    bool isOnline();
};

#endif