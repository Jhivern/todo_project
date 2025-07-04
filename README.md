# todo_project
Quick note: In order to use this code, you will need to create two JSON config files. Below are the steps detailing the process:

1. Create a file with the name ```wifi_config.json```. This file goes in this folder: ```todo_project\arduino```.
2. Inside the JSON file, add the following code:
```json /
{
  "ssid": "YourWifiName",
  "password": "YourPassword"
} 
```
In addition, the Java backend also requires a JSON config file for authentication with the MS Azure App:
1. Create a file with the name ```auth.json```. This file goes in this folder: ```todo_project\java-backend```.
2. Inside the JSON file, add the following code:
```json /
{
  "clientId": "your-client-id",
  "clientSecret": "your-client-secret",
  "tenantId": "your-tenant-id",
  "accessToken": "your-access-token",
  "refreshToken": "your-refresh-token"
}
```

