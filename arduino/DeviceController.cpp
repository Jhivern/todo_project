#include "DeviceController.h"
#include <LiquidCrystal.h>

// Refactor later for clean pin integration
DeviceController::DeviceController() : lcd(22, 23, 32, 27, 25, 33) {
  // Add later method implementation here
}

bool DeviceController::begin() {
  // Add later method implementation here
  lcd.begin(16, 2);  // 16 columns, 2 rows
  lcd.clear();
  return true;
}

// Write content to top line of LCD display
void DeviceController::writeLine0(const char* input) {
    lcd.setCursor(0, 0);
    lcd.print(input);
}

// Write content to bottom lone of LCD display
void DeviceController::writeLine1(const char* input) {
    lcd.setCursor(0, 1);
    lcd.print(input);
}

// Clear the LCD screen
void DeviceController::clearLCD() {
  lcd.clear();
}