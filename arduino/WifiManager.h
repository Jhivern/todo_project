#ifndef WIFIMANAGER_H
#define WIFIMANAGER_H

class WifiManager {
  private:
    // Reverse order means we get a pointer that cannot be reassigned, but value is mutable.
    const char* const ssid;
    const char* const password;

  public:
    WifiManager(const char* ssid, const char* password);
    bool begin();
    bool isOnline();
};

#endif