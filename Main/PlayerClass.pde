class Player extends Obstacles
{
 
  int xSpeed=5;    // Player xSpeed
  PImage[] playerHitImages = new PImage[2];
 
  int counterHit=0;   //Counter used to change the player images when hits the shark
   
  //------------------------------------------------------------> CONSTRUCTOR 

  Player (int x, int y, int ySpeed)
  {
    super(x,y,ySpeed, "Player", 4, 150, 100);
    
    for (int i=0; i<playerHitImages.length; i++)
    {
        playerHitImages[i]=loadImage("hit"+i+".png");
        playerHitImages[i].resize(150,100);
    }
   
  }

  //------------------------------------------------------------> KEY COMMAND 

  void keyPressed()
  {
    if (gameMode == Status.OK)
    {
      if (key==CODED)
      {
        switch (keyCode)
        {
          case UP:
            if (y>=10)
            {
              y-=ySpeed;
            }
          break;
        
          case DOWN:
            if (y<=height-obstacleImages.get(1).height)
            {
               y+=ySpeed;
            }
          break;
        
          case RIGHT:
            if (x<=width-obstacleImages.get(1).width)
            {
               x+=ySpeed;
            }
          break;
          
          case LEFT:
            if (x>10)
            {
               x-=ySpeed;
            }
          break;    
        }
      } 
    }
  }
  
  
  //------------------------------------------------------------> COLLISION PLAYER AND SHARK, PLAYER PROSPECTIVE (AFTER THE IMAGE SHOWS THE HIT, GAMEMODE IS BACK TO OK)
  void isHit()
  {  
    int i=(int) counterHit/10;
    if (counterHit >=0 && counterHit <=19)
    { 
      image (playerHitImages[i],x,y);
    }
    else
    { 
      counterHit =0;
      gameMode = Status.OK;
    }
    counterHit = counterHit+1;
  }

  ArrayList<PImage> getPlayerImages()
  {
    return obstacleImages;
  }

}  //------------------------------------------------------------> END PLAYER CLASS
