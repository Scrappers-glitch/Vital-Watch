//sketch created by Akshay Joseph
char inputByte;
int heartRate = 70;
float temp = 37.5;
int x = 0;
int max = 30;

void sensorSimulator(int);

void setup() {
  Serial.begin(9600);
}

void loop() {
   // update the sensor and send data on serial each 10 seconds
   sensorSimulator(10000);
}

void sensorSimulator(int milliSeconds) {
    String rateLabel = "HR:";
    heartRate += x;
    
    String tempLabel = "TEMP:";
    temp += x;
    
    String bloodPressure = "125/85";
    String pressureLabel = "BP:";
    
    Serial.println(rateLabel + heartRate);
    Serial.println(tempLabel + temp);
    Serial.println(pressureLabel + bloodPressure);
    ++x;
    delay(milliSeconds);
}
