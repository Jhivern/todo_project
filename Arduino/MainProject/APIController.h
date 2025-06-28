#ifndef APICONTROLLER_H
#define APICONTROLLER_H
#include "Top2Items.h"

class APIController {
  // Add later method definitions here
  public:
    APIController();
    bool begin();
    Top2Items getTasks();
};

#endif