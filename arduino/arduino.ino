#include <SPI.h>
#include <WiFi101.h>
#include <Wire.h>

// Default WiFi status

int status = WL_IDLE_STATUS;

WiFiClient client;


typedef enum {
  EXPR, SWITCH
} PACKET_TYPE;


typedef struct {
  unsigned char pin, previousValue;
} device;

unsigned char analogDevicesSize = 1;
device analogDevices[1] = {
  {
    A1, 0
  }
};

unsigned char numericDevicesSize = 2;
device numericDevices[2] = {
  {
    0, 0
  }, {
    1, 0
  }
};

int CONFIG_PIN = 10;
int EEPROM = 0x50;

void writeEEPROM(int deviceaddress, unsigned int eeaddress, byte data )  {
  Wire.beginTransmission(deviceaddress);
  Wire.write((int)(eeaddress >> 8));   // MSB
  Wire.write((int)(eeaddress & 0xFF)); // LSB
  Wire.write(data);
  Wire.endTransmission();
 
  delay(5);
}

byte readEEPROM(int deviceaddress, unsigned int eeaddress )  {
  byte rdata = 0xFF;
 
  Wire.beginTransmission(deviceaddress);
  Wire.write((int)(eeaddress >> 8));   // MSB
  Wire.write((int)(eeaddress & 0xFF)); // LSB
  Wire.endTransmission();
 
  Wire.requestFrom(deviceaddress,1);
 
  if (Wire.available()) rdata = Wire.read();

  return rdata;
}

void writeStringToEEPROM(int deviceAddress, unsigned int startAddress, String data) {
  writeEEPROM(deviceAddress, startAddress, data.length() + 1);
  for(int i = 0; i < data.length() + 1; i++) {
    writeEEPROM(deviceAddress, i + 1 + startAddress, data.charAt(i));
  }
}

String readStringFromEEPROM(int deviceAddress, unsigned int startAddress) {
  int size = readEEPROM(deviceAddress, startAddress);
  char data[size];
  
  for(int i = 0; i < size; i++) {
    data[i] = (char)readEEPROM(deviceAddress, i + 1 + startAddress);
  }

  return data;
}
 
void config() {
  bool running = true;
  String data;
  digitalWrite(LED_BUILTIN, HIGH); 

  delay(1000);

  while(running) {
    if(Serial.available()) {
      data = Serial.readStringUntil('#');
      if(data == "PING")
        Serial.print("PONG");
      else if(data == "WIFI") {
        String ssid = Serial.readStringUntil('#');
        String password = Serial.readStringUntil('#');
        String server = Serial.readStringUntil('#');
        
        unsigned int address = 0;
        writeStringToEEPROM(EEPROM, address, ssid);
        address += ssid.length() + 2;
        writeStringToEEPROM(EEPROM, address, password);
        address += password.length() + 2;
        writeStringToEEPROM(EEPROM, address, server);
        
      } else if(data == "QUIT") {
        running = false;
      } else if(data == "WIFITEST") {
        String ssid = Serial.readStringUntil('#');
        String password = Serial.readStringUntil('#');
        String serverString = Serial.readStringUntil('#');
        IPAddress server;
        server.fromString(serverString);

        int statusTest = WL_IDLE_STATUS;
        int attempts = 0;
        while (statusTest != WL_CONNECTED) {
          // Connect to WPA/WPA2 network. Change this line if using open or WEP network:
          statusTest = WiFi.begin(ssid, password);
          attempts++;
          if(attempts > 3) {
            goto ko;
          }
          delay(2000);
        }
        client.connect(server, 14123);
        ko:
        client.stop();
        WiFi.disconnect();
      }
      Serial.flush();
    }
  }
  digitalWrite(LED_BUILTIN, LOW); 
}

void setup() {
  
  Serial.begin(9600);
  Wire.begin();
    
  pinMode(CONFIG_PIN, INPUT);
  if(digitalRead(CONFIG_PIN)) 
    config();

  String ssid = readStringFromEEPROM(EEPROM, 0);
  String password = readStringFromEEPROM(EEPROM, ssid.length()+2);
  String serverString = readStringFromEEPROM(EEPROM, ssid.length()+2 + password.length()+2);
  IPAddress server;
  server.fromString(serverString);

  // check for the presence of the shield:
  if (WiFi.status() == WL_NO_SHIELD) {
    Serial.println("WiFi shield not present");
    while (true);
  }

  // attempt to connect to Wifi network:
  while (status != WL_CONNECTED) {
    Serial.print("Attempting to connect to SSID: ");
    Serial.println(ssid);
    // Connect to WPA/WPA2 network. Change this line if using open or WEP network:
    status = WiFi.begin(ssid, password);

    delay(2000);
  }
  Serial.println("Connected to wifi");

  Serial.println("\nStarting connection to server...");

  if (client.connect(server, 14123)) {
    Serial.println("connected to server");
  }

  analogReadResolution(7);

  for(int i = 0; i < numericDevicesSize; i++) {
    pinMode(numericDevices[i].pin, INPUT_PULLUP);
  }

  for(int i = 0; i < numericDevicesSize; i++)
    sendPacket(SWITCH, digitalRead(numericDevices[i].pin), numericDevices[i].pin);

  for(int i = 0; i < analogDevicesSize; i++)
    sendPacket(EXPR, analogRead(analogDevices[i].pin), analogDevices[i].pin);

}

void sendPacket(PACKET_TYPE type, unsigned char data, unsigned char pin) {
  uint8_t packet[2];

  packet[0] = ((type << 7) | pin);
  packet[1] = data;

  client.write(packet, 2);
  client.flush();
}

void loop() {

  while(true) {

    for(int i = 0; i < analogDevicesSize; i++) {
      unsigned char data = (unsigned char) analogRead(analogDevices[i].pin);
      if(data == analogDevices[i].previousValue)
        continue;
      sendPacket(EXPR, data, analogDevices[i].pin);
      analogDevices[i].previousValue = data;
    }

    for(int i = 0; i < numericDevicesSize; i++) {
      unsigned char data = (unsigned char) digitalRead(numericDevices[i].pin);
      Serial.println(data);
      if(data == numericDevices[i].previousValue)
        continue;
      sendPacket(SWITCH, data, numericDevices[i].pin);
      numericDevices[i].previousValue = data;
    }
    
    delay(20);
  }
}





