#ifndef APICONTROLLER_H
#define APICONTROLLER_H
#include "Top2Items.h"
#include "WifiManager.h"


class APIController {
  // Add later method definitions here
  public:
    APIController(WifiManager& wifiRef);
    bool begin();
    Top2Items getTasks();
  private:
    WifiManager& wifi;
};

#endif