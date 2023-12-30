String screenText;

void setup() {
  //przypisać tu piny do urządzeń odpowiednich
  
  initialState();

}

void loop() {

  if(moveSensorDistance()< 2cm){
    
    beeperOn(1);
    lcdScreenInfo("Authorizing...");
    delay(1000); //czas potrzebny na ustabilizowanie karty wstępu w czytniku
    rgbReaderBegin();
    sendDataToServer();
    screenText = receiveStringFromServer();
    finalTask();
    initialState(); //powrót do stanu pierwotnego
   
  }
  delay(1000);
}

void initialState(){

  lcdScreenInfo("Insert card");
  redLedOn();
  greenLedOff();
}


  void finalTask(){

    if(screenText != "Unauthorized"){
      greenLedOn():
      redLedOff(); 
    } 
    
    lcdScreenInfo(screenText);
    delay(5000); //aby dłużej wyświetlał text końcowy
  }

}
