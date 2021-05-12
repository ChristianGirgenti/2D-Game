import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class gameMain extends PApplet {

final int numOfMonsters=5;  
final int numOfStars=3;  
final int numOfLives=3;   

PImage background;  
Player player;      
int playerX=10;     
int playerY=10;     
int playerYSpeed=5; 
Star[] stars;       
Monsters[] monsters;
Life[] lives;      
int bgY=0;          
int score;          
int bestScore = -1; 
int monstersSpeed;  
Status gameMode;    
int lifeCounter;    
int level;          
int starSpeed;      


//------------------------------------------------------------> SETUP TO INITIALSTATE = LEVEL1 

public void setup() {
  
  initialState();
}

//------------------------------------------------------------> START OF DRAW 
public void draw ()
{
  drawBackground();
  drawCharacters();
  showScore();
  
  // FOR DEFAULT GAMEMODE IS START. 
  // IF GAMEMODE IS EQUAL TO CRASH, IT MEANS THERE HAS BEEN A CRASH BETWEEN PLAYER AND SHARK OR OCTOPUS. IN THAT CASE GO TO CRASH MODE.
  // IF GAME MODE IS GAMEOVER , CALCULATE THE BEST SCORE AND DISPLAY THE GAME OVER PROMPT
  // IF GAME MODE IS OK, RUN THE GAME
  // IF GAME MODE IS DEFAULT (START) SHOW THE START PROMPT
  switch (gameMode){
     case CRASH:
         crashMode();
     break;
     case GAMEOVER:
         calculateBestScore();
         gameOverMode();
     break;
     case OK:
          levelsActions();                  //LEVELS MANAGEMENT
          starActions();                    //STAR MOVE AND DETECT COLLISION
          monstersActions();                //MONSTERS MOVE AND DETECT COLLISION
          isGameOver();                     //CHECK IF IS GAMEOVER
          drawLives();                      //DRAW LIVES ON SCREEN
     break;
     default:    
     {
         startMode();
     }
  }
  
}

//------------------------------------------------------------> START MODE
public void startMode()
{
  background(0);
  fill (255);
  textSize(24);
  text ("Avoid the sharks and collect the stars to increase your score",10,200);
  text ("Each star gives you 10 points. When your score is 50 you pass to level 2",10,250);
  text ("When your score goes over 100, you pass to level 3.", 10, 300);
  text ("For each level the shark's speed will increase", 10, 350);
  text ("If you hit a shark you lose a life. After three life lost it's game over",10,400);
  text ("Enjoy :)",10,450);
  text ("Press enter to START",10,500);
}



//------------------------------------------------------------> SET INITIAL STATE (LEVEL 1)

public void initialState()
{
  background=loadImage("bg1.png");
  background.resize(width,height);
  score=0;                            //SCORE SET TO 0 AT THE BEGINNING
  starSpeed=5;                        
  player = new Player(playerX, playerY, playerYSpeed);        
  stars = new Star [numOfStars];      
  createStars();                      
  monsters = new Monsters[numOfMonsters];    
  createMonsters();                  
  lives = new Life[numOfLives];      
  createLives();                      
  gameMode = Status.START;            
  lifeCounter = 3;                    
  level=1;                            
}

//------------------------------------------------------------> CREATE LIVES
public void createLives()
{
  for (int i=0; i<3;i++)
  {
    if (i==0)
      lives[i] = new Life (750,30);
    else if (i==1)
      lives[i] = new Life (800,30);
    else if (i==2)
      lives[i] = new Life (850,30);
  }
}
//------------------------------------------------------------> RETURN A SHARK SPEED BASED ON THE LEVEL
public int getMonstersSpeed(int level)
{
  switch (level)
   {
     case 2:
     {
       return (int) ( random (6,8));
     }
     case 3:
     {
       return (int) ( random (9,11));
     }
     default :
     {
        return (int) (random (3,5));
     }  
   }
}

//------------------------------------------------------------> CREATE MONSTERS
public void createMonsters()
{
  for (int i=0; i<monsters.length;i++)
  {
    monstersSpeed=getMonstersSpeed(level); 
    if(i%2 == 0)
    {
      monsters[i] = new Shark ( (int)random(0,width-100), 1100, monstersSpeed);
    }
    else
    {
      monsters[i] = new Octopus ( (int)random(0,width-40), 1100, monstersSpeed);
    }
  }
}

//------------------------------------------------------------> CREATE STARS
public void createStars()
{
  for (int i=0; i<stars.length;i++)
  {
    stars[i] = new Star ( (int)random(0,width-40), 1100,starSpeed);
  }
}


//------------------------------------------------------------> CHANGE LEVEL (CHANGE LEVEL TEXTURE BASED ON THE VARIABLE LEVEL, DEFAULT LEVEL = 1)
public void changeLevelTexture(int level)
{
   switch (level)
   {
     case 2:
     {
        textSize(32);
        fill(255,0,0);
        text ("Level 2",350,50);
     }
     break;
     case 3:
     {
       textSize(32);
       fill(255,0,0);
       text ("Level 3",350,50);
     }
     break;
     default :
     {
        textSize(32);
        fill(255,0,0);
        text ("Level 1",350,50);
     }  
   }
}

//------------------------------------------------------------> ASSIGN THE RIGHT LEVEL BASED ON THE SCORE WE HAVE
public void checkLevel ()
{  
  if ((score>50) && (score<=100))
  {
      level=2;
  }  
  else if (score>100)
  {
      level=3;
  }
}

//------------------------------------------------------------> ACTIONS DONE TO MANAGE THE LEVELS
public void levelsActions()
{
  checkLevel();
  changeLevelTexture(level);
}


//------------------------------------------------------------> DRAW BACKGROUND 

public void drawBackground()
{
  image(background,0,bgY); //draw background twice adjacent
  image(background,0,bgY+background.height);
  bgY-=5;
  if (bgY==-background.height)
  bgY=0;  //wrap background
}

//------------------------------------------------------------> DRAW CHARACTERS 

public void drawCharacters ()
{
  if (player != null)
    player.render();
    
  for (int i=0 ; i < stars.length; i++)
  { 
    if (stars[i] != null)
      stars[i].render();
  }
  
  for (int i=0 ; i < monsters.length; i++)
  { 
    if (monsters[i] != null)
      monsters[i].render();
  }
}

//------------------------------------------------------------> DRAW LIVES
public void drawLives ()
{
  for (int i=0;i<3;i++)
  {
    if (lives[i] != null)
       lives[i].render();
  }
}


//------------------------------------------------------------> SHOW SCORE 

public void showScore()
{
  textSize(32);
  fill(255,0,0);
  if (score<10)
    text("00000"+score,5,50);
  else if (score<100 && score >=10)
    text("0000"+score,5,50);
  else if (score<1000 && score >=100)
    text("000"+score,5,50);
  else if (score<10000 && score >= 1000)
    text("00"+score,5,50);
  else
    text("0"+score,5,50);
}



//------------------------------------------------------------> KEY FUNCTIONALITY

public void keyPressed()
{
  switch (gameMode)
  {
    case GAMEOVER :  //IF GAME MODE IS GAME OVER PRESS ENTER TO START AGAIN (DON'T SHOW THE INITIAL PROMPT AGAIN)
    {
      if (key==ENTER)
      {
        initialState();
      }
    }
    case START:    //IF GAME MODE IS START PRESS ENTER TO PLAY
    {
      if (key==ENTER)
      {
        gameMode=Status.OK;
      }
    }
    default:      // MOVE PLAYER IF GAMEMODE IS DEFAULT (OK)
        player.keyPressed();
  }
}


//------------------------------------------------------------> MOVE STAR
public void moveStar()
{
   for (int i=0; i<stars.length; i++)
   {
     if (stars[i].move() == false)
     {
       stars[i] = new Star ( (int)random(0,width-40), 1100,starSpeed);
     }
   }
}

//------------------------------------------------------------> COLLISION STAR
public void starCollision ()
{
  for (int i=0; i<stars.length; i++)
  {
    if (stars[i]!=null)
    {
      if (stars[i].isHit(player))
     {
       stars[i] = null;
       score=score+10;
       stars[i] = new Star ( (int)random(0,width), 1100,starSpeed);
     }
    }
  }
}

//------------------------------------------------------------> ACTIONS DONE BY STAR
public void starActions()
{
  moveStar();
  starCollision();
}


//------------------------------------------------------------> MOVE MONSTERS
public void moveMonsters()
{
   for (int i=0 ; i < monsters.length; i++)
   {
     if (monsters[i].move() == false)
     {
        monstersSpeed=getMonstersSpeed(level);
        if(i%2 == 0)
        {
          monsters[i] = new Shark ( (int)random(0,width-100), 1100, monstersSpeed);
        }
        else
        {
          monsters[i] = new Octopus ( (int)random(0,width-40), 1100, monstersSpeed);
        }
     }
   }
}

//------------------------------------------------------------> COLLISION MONSTERS (IF THERE HAS BEEN A COLLISION CREATE A NEW SHARK OR OCTOPUS AND ASSIGN CRASH TO GAMEMODE)
public void monstersCollision ()
{
  for (int i=0 ; i < monsters.length; i++)
  {
    if (monsters[i]!=null)
    {
      if (monsters[i].isHit(player))
     {
       monsters[i] = null;
       monstersSpeed=getMonstersSpeed(level);
       if(i%2 == 0)
       {
         monsters[i] = new Shark ( (int)random(0,width-100), 1100, monstersSpeed);
       }
       else
       {
         monsters[i] = new Octopus ( (int)random(0,width-40), 1100, monstersSpeed);
       }
       lifeCounter -=1;
       switch (lifeCounter)
       {
         case 2: lives[0]=null;
         break;
         case 1: lives[1]=null;
         break;
       }
       gameMode = Status.CRASH;
     }
    }
  }
}


//------------------------------------------------------------> ACTIONS DONE BY MONSTERS
public void monstersActions()
{
 moveMonsters();
 monstersCollision();
}




//------------------------------------------------------------> CRASH MODE (IF THERE HAS BEEN A CRASH, DO THE ISHIT PLAYERS METHOD)
public void crashMode()
{
  player.isHit();
}

//------------------------------------------------------------> KEEP TRACK OF LIVES LEFT
public void isGameOver()
{
  if (lifeCounter<=0)
    gameMode = Status.GAMEOVER;
}


//------------------------------------------------------------> GAME OVER MODE (IF THERE HAVE BEEN THREE CRASH)
public void gameOverMode()
{
  background(0);
  fill (255,0,0);
  textSize(32);
  text ("Game Over",380,550);
  textSize(30);
  text ("Score :"+score,400,600);
  text ("Best Score :"+bestScore,400,650);
  textSize(24);
  text ("Press ENTER to try again", 320, 700);
}

//------------------------------------------------------------> CALCULATE BEST SCORE
public void calculateBestScore()
{
  if (score > bestScore)
    bestScore = score;
}
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
  
  
  public void render()
  {
    image (heart,x,y);
  }
  
} // END LIFE CLASS
class Monsters extends Obstacles
{
  //------------------------------------------------------------> CONSTRUCTOR 

  Monsters (int x, int y, int ySpeed, String imageName, int imageCount, int resizeX, int resizeY)
  {
    super(x,y,ySpeed, imageName,  imageCount,  resizeX,  resizeY);
  }

  
} // END MONSTER CLASS
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
 public void render ()
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
  public boolean move ()
   {
     y=y-ySpeed; //Move up a bit
     return y>=0;
   }
 
 //------------------------------------------------------------> COLLISION DETECTION 
  public boolean isHit(Player player)
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
class Octopus extends Monsters
{                             
   
  //------------------------------------------------------------> CONSTRUCTOR 

  Octopus (int x, int y, int ySpeed)
  {
    super(x,y,ySpeed, "octo", 3, 40, 80);
  }
  
} // END OCTOPUS CLASS 
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

  public void keyPressed()
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
  public void isHit()
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

  public ArrayList<PImage> getPlayerImages()
  {
    return obstacleImages;
  }

}  //------------------------------------------------------------> END PLAYER CLASS
class Shark extends Monsters 
{
                         
  //------------------------------------------------------------> CONSTRUCTOR 

  Shark (int x, int y, int ySpeed)
  {
    super(x,y,ySpeed,"shark",4,100,130);
  }

} // END SHARK CLASS 
class Star extends Obstacles
{
   
  //------------------------------------------------------------> CONSTRUCTOR 
  Star (int x, int y, int ySpeed)
  {
    super(x,y,ySpeed,"star",1,40,40);
  }

} // END STAR CLASS
//GAME STATUS == OK ---> THE GAME RUNS NORMALLY
//GAME STATUS == CRASH ---> IT MEANS THERE HAS BEEN A CRASH BETWEEN SHARK AND PLAYER
//GAME STATUS == GAMEOVER --->YOU LOST ALL THE LIVES
//GAME STATUS == START ---> JUST STARTING THE GAME

enum Status {OK, CRASH, GAMEOVER, START}
  public void settings() {  size(900,1100); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#cccccc", "gameMain" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
