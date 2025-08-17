#ifndef APICONTROLLER_H
#define APICONTROLLER_H
#include "Top2Items.h"
#include "WifiManager.h"
#include <optional>
#include <HTTPClient.h>


class APIController {
  // Add later method definitions here
  public:
    APIController(WifiManager& wifi); // Initialize with a WifiManager object field
    bool begin(); // Make sure internet connection is set up
    std::optional<Top2Items> getTasks(HTTPClient http); // Perform the GET request and get the data
  private:
    WifiManager& wifi;
};

#endif