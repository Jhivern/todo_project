#ifndef WIFIMANAGER_H
#define WIFIMANAGER_H

class WifiManager {
  // Add later method definitions here
  public:
    WifiManager();
    bool begin();
    bool isOnline();
};

#endif