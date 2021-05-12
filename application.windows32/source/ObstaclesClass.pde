class Obstacles
{
  int x,y;      //Initial position of the obstacles
  int ySpeed;   //Y speed of the obstacles
  
  int counter=0;     
  ArrayList<PImage> obstacleImages = new ArrayList<PImage>();
  
  Obstacles (int x, int y, int ySpeed, String imageName, int imageCount, int resizeX, int resizeY)
  {
    this.x=x;
    this.y=y;
    this.ySpeed=ySpeed;
    
    for (int i=0; i<imageCount; i++)
    {
        PImage newImage = loadImage(imageName+i+".png");
        newImage.resize(resizeX,resizeY);
        obstacleImages.add(newImage);
    }
  }
  
  
  //------------------------------------------------------------> RENDER 
 void render ()
  {
    int i=(int) counter/10;
    image (obstacleImages.get(i),x,y);

    if(counter >= (obstacleImages.size() * 10)-1)
    { 
      counter =0;
    }
    counter = counter+1;
  }
  
  
 //------------------------------------------------------------> MOVE 
  boolean move ()
   {
     y=y-ySpeed; //Move up a bit
     return y>=0;
   }
 
 //------------------------------------------------------------> COLLISION DETECTION 
  boolean isHit(Player player)
  {
    ArrayList<PImage> playerImages = player.getPlayerImages();
    if (abs((player.y+playerImages.get(1).height/2)-(this.y+obstacleImages.get(0).height/2))<(playerImages.get(1).height+obstacleImages.get(0).height)/2)
    {
      if (abs((player.x+playerImages.get(1).width/2)-(this.x+obstacleImages.get(0).width/2))<(playerImages.get(1).width+obstacleImages.get(0).width)/2)
      {
          return true;
      }
    }
    return false;
  } 

} //------------------------------------------------------------> END OBSTACLES CLASS
