class Life {
  int x,y;
  PImage heart;
  
  //------------------------------------------------------------> CONSTRUCTOR 
  
  Life (int x, int y)
  {
    this.x = x;
    this.y = y;
    
    heart = loadImage("heart.png");
  }
  
  
  void render()
  {
    image (heart,x,y);
  }
  
} // END LIFE CLASS
