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

void setup() {
  size(900,1100);
  initialState();
}

//------------------------------------------------------------> START OF DRAW 
void draw ()
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
void startMode()
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

void initialState()
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
void createLives()
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
int getMonstersSpeed(int level)
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
void createMonsters()
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
void createStars()
{
  for (int i=0; i<stars.length;i++)
  {
    stars[i] = new Star ( (int)random(0,width-40), 1100,starSpeed);
  }
}


//------------------------------------------------------------> CHANGE LEVEL (CHANGE LEVEL TEXTURE BASED ON THE VARIABLE LEVEL, DEFAULT LEVEL = 1)
void changeLevelTexture(int level)
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
void checkLevel ()
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
void levelsActions()
{
  checkLevel();
  changeLevelTexture(level);
}


//------------------------------------------------------------> DRAW BACKGROUND 

void drawBackground()
{
  image(background,0,bgY); //draw background twice adjacent
  image(background,0,bgY+background.height);
  bgY-=5;
  if (bgY==-background.height)
  bgY=0;  //wrap background
}

//------------------------------------------------------------> DRAW CHARACTERS 

void drawCharacters ()
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
void drawLives ()
{
  for (int i=0;i<3;i++)
  {
    if (lives[i] != null)
       lives[i].render();
  }
}


//------------------------------------------------------------> SHOW SCORE 

void showScore()
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

void keyPressed()
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
void moveStar()
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
void starCollision ()
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
void starActions()
{
  moveStar();
  starCollision();
}


//------------------------------------------------------------> MOVE MONSTERS
void moveMonsters()
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
void monstersCollision () //<>//
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
void monstersActions()
{
 moveMonsters();
 monstersCollision();
}




//------------------------------------------------------------> CRASH MODE (IF THERE HAS BEEN A CRASH, DO THE ISHIT PLAYERS METHOD)
void crashMode()
{
  player.isHit();
}

//------------------------------------------------------------> KEEP TRACK OF LIVES LEFT
void isGameOver()
{
  if (lifeCounter<=0)
    gameMode = Status.GAMEOVER;
}


//------------------------------------------------------------> GAME OVER MODE (IF THERE HAVE BEEN THREE CRASH)
void gameOverMode()
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
void calculateBestScore()
{
  if (score > bestScore)
    bestScore = score;
}
