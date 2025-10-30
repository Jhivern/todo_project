#ifndef DEVICECONTROLLER_H
#define DEVICECONTROLLER_H
#include <LiquidCrystal.h>

class DeviceController {
  // Add later method definitions here
  public:
    DeviceController();
    bool begin();
    void writeLine1(const char* input);
    void writeLine2(const char* input);
    void clearLCD();

  private:
    LiquidCrystal lcd;
};

#endif