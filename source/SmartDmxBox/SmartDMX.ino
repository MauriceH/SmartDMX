#include <Conceptinetics.h>
#include <SoftwareSerial.h>

#define DMX_MASTER_CHANNELS   20 
#define RXEN_PIN                2
#define ledPin 13
#define rxPin 10
#define txPin 11
DMX_Master        dmx_master ( DMX_MASTER_CHANNELS, RXEN_PIN );
 
SoftwareSerial btSerial(rxPin, txPin);
byte btData[10];
int readPos;

void setup() {
  btSerial.begin(9600);
  dmx_master.enable ();
  dmx_master.setChannelRange ( 1,13,0 );
  dmx_master.setChannelValue ( 8,255 );
  dmx_master.setChannelValue ( 9,33 );
  dmx_master.setChannelValue ( 10,255 );
  readPos = 0;  
}

void loop() {
   int cnt = btSerial.available();
   if(cnt == 0){
    delay(100);
    return;
   }
   for(int i = 0; i < cnt;i++) {
    btData[readPos] = btSerial.read(); 
    if(readPos > 1) {
      if((btData[0] ^ btData[1]) != btData[2]) {
         btData[0] = btData[1];
         btData[1] = btData[2];
      } else {
        readPos = 0;
        dmx_master.setChannelValue ( btData[0], btData[1] );
      }
    } else {
      readPos++;
    }
   }
   delay ( 100 );
}

