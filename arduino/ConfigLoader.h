#ifndef CONFIGLOADER_H
#define CONFIGLOADER_H

// Store state of config efficiently in an enum
enum ConfigStatus {
  CONFIG_OK,
  CONFIG_SPIFFS_FAIL,
  CONFIG_FILE_NOT_FOUND,
  CONFIG_PARSE_ERROR
};

class ConfigLoader {
  
  public:
    ConfigLoader(const char* path); // Do SPIFFS initialization in the constructor and read data
    const char* getSsid();
    const char* getPassword();
    ConfigStatus getConfigStatus();

  private:
    // The config information
    char ssid[32]; // Arbitrary amount, we expect names to be shorter than 32 chars, make higher if its problematic
    char password[64]; // 63 + 1 for null termination
    ConfigStatus configStatus = CONFIG_PARSE_ERROR; // Default to error
};

#endif