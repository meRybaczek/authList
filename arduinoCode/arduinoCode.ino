#include <LiquidCrystal.h>

//LCD
LiquidCrystal lcd(2, 3, 4, 5, 6, 7);

//LED
int ON = 1;
int OFF = 0;
#define RED_LED 999
#define GREEN_LED 999
//define RED AND GREEN pins to do later

//TCS3200 sensor
#define S0 9
#define S1 10
#define S2 11
#define S3 12
#define sensorOut 8

//sound sensor
#define trigPin 9999
#define echoPin 9999

//buzzer
#define BUZZER 9999


void setup() {
//TCS3200 setup
  pinMode(S0, OUTPUT);
  pinMode(S1, OUTPUT);
  pinMode(S2, OUTPUT);
  pinMode(S3, OUTPUT);
  pinMode(sensorOut, INPUT);
  //TCS output frequency scaling set to 20%
  digitalWrite(S0, HIGH);
  digitalWrite(S1, LOW);

  //LED
  pinMode(LED_RED, OUTPUT);
  pinMode(GREEN_RED, OUTPUT);

  //sound sensor
   pinMode(trigPin, OUTPUT);
   pinMode(echoPin, INPUT);

   //buzzer
   pinMode(BUZZER, OUTPUT);

  initialState();

  Serial.begin(9600);
  lcd.begin(16, 2);


}

void loop() {

  if(cardInserted()){
    
    beeperOn(1000);
    lcdScreenInfo("Authorizing...");
    delay(2000); //czas potrzebny na ustabilizowanie karty wstępu w czytniku

    String rgbRead = rgbReaderBegin();
    sendDataToServer(rgbRead);
    String screenText = receiveStringFromServer();
    finalTask(screenText);
    delay(5000);

    lcdScreenInfo("Remove card");
    while(cardInserted()){
    }
    initialState(); //powrót do stanu pierwotnego
   
  }
  delay(1000);
}

void initialState(){
  lcdScreenInfo("Insert card");
  ledsState(RED_LED,ON);
  ledsState(GREEN_LED,OFF);
}


  String finalTask(String message){
    if(message != "Unauthorized"){
      ledsState(RED_LED,ON);
      ledsState(GREEN_LED,OFF);
    } else {
      ledsState(RED_LED,OFF);
      ledsState(GREEN_LED,ON);
    }

  }

  void ledsState(ledName, int state){
    if(state == OFF){
        digitalWrite(ledName, LOW)
    } else{
        digitalWrite(ledName, HIGH)
    }

  }

  void lcdScreenInfo(String message){
    lcd.clear();
    lcd.setCursor(0, 0);
    lcd.print(message);
  }

  String rgbReaderBegin(){
    int redReading;
    int greenReading;
    int blueReading;

    digitalWrite(S2, LOW);
    digitalWrite(S3, LOW);
    redReading = pulseIn(sensorOut, LOW);
    delay(200);

    digitalWrite(S2, HIGH);
    digitalWrite(S3, HIGH);
    greenReading = pulseIn(sensorOut, LOW);
    delay(200);

    digitalWrite(S2, LOW);
    digitalWrite(S3, HIGH);
    blueReading = pulseIn(sensorOut, LOW);
    delay(200);

    redReading = map(redReading, 343, 172, 0, 255);   //(old_red_reading_for_blackCard,old_red_reading_for_whiteCard,reference_from,reference_to); old_reading is measured for mapping disabled
    greenReading = map(greenReading, 354, 173, 0, 255);
    blueReading = map(blueReading, 100, 51, 0, 255);

    return (String(redReading) + "," + String(greenReading) + "," + String(blueReading));

  }

  int countDistanceFromCardToSensor(){
    int SOUND_SPEED = 58;
    digitalWrite(trigPin, LOW);
    delayMicroseconds(2);
    digitalWrite(trigPin, HIGH);
    delayMicroseconds(10);
    digitalWrite(trigPin, LOW);

    long time = pulseIn(echoPin, HIGH);
    long distance = time/SOUND_SPEED;

    return distance;
  }

  void beeperOn(int beepLength_ms){
    digitalWrite(BUZZER, HIGH);
    delay(beepLength_ms);
    digitalWrite(BUZZER, HIGH);

  }

  boolean cardInserted(){
    int REF_DISTANCE_CM = 10;
    return countDistanceFromCardToSensor() <= REF_DISTANCE_CM;
  }

  void sendDataToServer(String upload){
    Serial.begin(upload);

  }

}
