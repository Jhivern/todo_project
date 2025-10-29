#ifndef DEVICECONTROLLER_H
#define DEVICECONTROLLER_H
#include <LiquidCrystal.h>

class DeviceController {
  // Add later method definitions here
  public:
    DeviceController();
    bool begin();
    void writeLines(const char* input1, const char* input2);
    void clearLCD();

  private:
    LiquidCrystal lcd;
};

#endif