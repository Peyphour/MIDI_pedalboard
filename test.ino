#include <SPI.h>
#include <WiFi101.h>

char ssid[] = "*******"; //  your network SSID (name)
char pass[] = "*******";    // your network password (use for WPA, or use as key for WEP)

int status = WL_IDLE_STATUS;

IPAddress server(192, 168, 1, 121);  // Server IP

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

void setup() {
  Serial.begin(9600);

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
    status = WiFi.begin(ssid, pass);

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





