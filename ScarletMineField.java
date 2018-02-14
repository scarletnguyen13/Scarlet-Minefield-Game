import java.applet.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

/**
 * Class MineField - write a description of the class here
 * 
 * @Scarlet Nguyen 
 * @version2
 */
public class ScarletMineField extends JApplet implements KeyListener, ActionListener {
    //declare variables 
    Image [][] field;
    Image [][] uncovered;
    Image [] heart, light;
    Image ball;
    Image goodPic, badPic, neutralPic;
    Image goodPicCurrent, badPicCurrent, neutralPicCurrent;
    Image lives, livesLost;
    Image lightbulb, lightbulbBright;
    Image arrows, spacebar;

    Rectangle [][] tile;
    Rectangle [] heartTile;
    Rectangle [] lightTile;

    int randX, randY, numbsGood, numbsBad;
    int userCols, userRows;
    int tileWidth, tileHeight;
    int heartWidth, heartHeight;
    int lightWidth, lightHeight;
    int losePoint, winPoint;

    Boolean[][] space;
    Boolean dug;
    Boolean [][] dugTile;
    Boolean [][] pressedTile;
    Boolean [][] occupied;
    Boolean moved;
    Boolean won;
    Boolean lost;

    Button start;
    Button restart;
    Button giveup;

    String buttonPressed;

    AudioClip good;
    AudioClip move;
    AudioClip bad;
    AudioClip win;
    AudioClip lose;
    public void init(){
        winPoint = 0;
        losePoint = 3;
        
        //import sound effects
        good=getAudioClip(getCodeBase(),"sounds/good.wav");
        move=getAudioClip(getCodeBase(),"sounds/move.wav");
        bad=getAudioClip(getCodeBase(),"sounds/bad.wav");
        win=getAudioClip(getCodeBase(),"sounds/applause.wav");
        lose=getAudioClip(getCodeBase(),"sounds/lose.wav");
        
        //import images
        ball = getImage(getCodeBase(),"images/ball.jpg");

        goodPic =  getImage(getCodeBase(),"images/heart.jpg");
        badPic = getImage(getCodeBase(),"images/poop.jpg");
        neutralPic = getImage(getCodeBase(),"images/sleepy.jpg");

        goodPicCurrent = getImage(getCodeBase(),"images/heartCurrent.jpg");
        badPicCurrent= getImage(getCodeBase(),"images/poopCurrent.jpg");
        neutralPicCurrent = getImage(getCodeBase(),"images/sleepyCurrent.jpg");
        
        lives = getImage(getCodeBase(),"images/lives - Copy.jpg");
        livesLost = getImage(getCodeBase(),"images/lives - Copy - Copy.jpg");

        lightbulb = getImage(getCodeBase(),"images/lightbulb.jpg");
        lightbulbBright = getImage(getCodeBase(),"images/lightbulbBright.jpg");

        arrows = getImage(getCodeBase(),"images/keyboard-arrows.jpg");
        spacebar = getImage(getCodeBase(),"images/spacebar.jpg");
        
        //import and setup factors for buttons
        start = new Button("START");
        setLayout(null);
        start.setBounds(570,255,50,35);
        start.setBackground(Color.black);
        start.setForeground(Color.white);
        start.addActionListener(this);
        add(start);

        restart = new Button ("RESTART");
        setLayout(null);
        restart.setBounds(560,230,70,35);
        restart.setBackground(Color.green);
        restart.setForeground(Color.black);
        restart.addActionListener(this);

        giveup = new Button ("GIVE UP");
        setLayout(null);
        giveup.setBounds(560,285,70,35);
        giveup.setBackground(Color.red);
        giveup.setForeground(Color.white);
        giveup.addActionListener(this);
        
        //set up uncovered field
        //Initially, all images in the uncovered field are the sleepy face (before clicking the "START" button)
        uncovered=new Image[10][10];
        for (int a = 0; a <10;a++) {
            for (int b = 0; b<10; b++) {
                uncovered[a][b]=neutralPic;
            }
        }
        
        //set up field
        //Import 100 images for the field and set them up by using the image array
        field=new Image[10][10];
        for (int a = 0; a <10;a++) {
            for (int b = 0; b<10; b++) {
                String fileLocation="images/["+a+"]["+b+"].jpeg";
                field[a][b]=getImage(getCodeBase(),fileLocation);
            }
        }
        
        //import and set up image array images for the heart(lives) and light (# of Lightbulb = # of heart achieved)
        heart = new Image[3];
        for (int a = 0; a < 3; a++) {
            heart[a] = lives;
        }

        light = new Image[5];
        for (int a = 0; a < 5; a++) {
            light[a] = lightbulb;
        }

        //set width and height for each tile on the field
        tileWidth= 50;
        tileHeight= 50;
        
        tile=new Rectangle[10][10];
        for (int a=0;a<10;a++) {
            for (int b=0;b<10;b++) {
                tile[a][b]=new Rectangle();
                tile[a][b].width=tileWidth;
                tile[a][b].height=tileHeight;
                tile[a][b].x=b*55; //create spaces between
                tile[a][b].y=a*55; // all the tiles
            }
        }
        
        //set width and height for the hearts (lives)
        heartWidth = 100;
        heartHeight = 40;
       
        heartTile = new Rectangle [3];
        for (int a = 0; a <3; a++) {
            heartTile[a]=new Rectangle();
            heartTile[a].width = heartWidth;
            heartTile[a].height = heartHeight;
            heartTile[a].x=550;   // x starts at 550
            heartTile[a].y= a*40; //y starts at 0 and shifts down 40 everytime go throught a loop
        }
        
        //set width and height for the lights (heart achieved)
        lightWidth = 20;
        lightHeight = 20;

        lightTile = new Rectangle [5];
        for (int a = 0; a <5; a++) {
            lightTile[a]=new Rectangle();
            lightTile[a].width = lightWidth;
            lightTile[a].height = lightHeight;
            lightTile[a].x=a*20+550; // x starts at 550 and shifts to the right 20 everytime it goes throught a loop
            lightTile[a].y=120; // y starts at 120
        }
        
        //add KeyListener and Request Focus
        addKeyListener(this); 
        requestFocus();

    }

    public void paint(Graphics g){
        g.setColor(Color.black);
        g.fillRect(550,145, 100, 270);
        // add Instructions for player
        g.drawString("- Arrows to Move",550,432);
        g.drawString("- Spacebar to Dig",550,448);
        //draw Arrows and Spacebar on keyboard as demonstration
        g.drawImage(arrows, 560, 450, 80, 70, this);
        g.drawImage(spacebar, 560, 520, 80, 20, this);
        
        //draw 3 hears 
        for (int a = 0; a < 3; a++) {
            g.drawImage(heart[a],heartTile[a].x,heartTile[a].y,heartTile[a].width,heartTile[a].height,this);
        }
        // draw 5 lightbulbs
        for (int a = 0; a< 5; a++) {
            g.drawImage(light[a],lightTile[a].x,lightTile[a].y,lightTile[a].width,lightTile[a].height,this);
        }
        
        //a lightbulb will bright everytime the player achieves a heart
        if (winPoint == 1) {
            light[0] = lightbulbBright;
        }
        else if (winPoint == 2) {
            light[1] = lightbulbBright;
        }
        else if (winPoint == 3) {
            light[2] = lightbulbBright;
        }
        else if (winPoint == 4) {
            light[3] = lightbulbBright;
        }
        else if (winPoint == 5) {
            light[4] = lightbulbBright;
        }
        
        // a heart will be empty everytime the player gets a poop
        if (losePoint == 2) {
            heart[0] = livesLost;
        }
        else if (losePoint == 1) {
            heart[1] = livesLost;
        }
        else if (losePoint == 0) {
            heart[2] = livesLost;
        }
        
        //draw field using setup-tiles
        for (int a=0;a<10;a++) {
            for (int b=0;b<10;b++) {
                //g.drawImage(uncovered[a][b],tile[a][b].x,tile[a][b].y,tile[a][b].width,tile[a][b].height,this);
                // the statement above is kept for the purpose of testing
                g.drawImage(field[a][b],tile[a][b].x,tile[a][b].y,tile[a][b].width,tile[a][b].height,this);                                      
            }
        }
        
        //when the player lost all their lives
        if (losePoint == 0) {
            //play sound
            lose.play();
            //set states 
            lost = true;
            moved = true;
            dug = false;
            //draw String to annouce that the player has lost
            //set color, font, font size and location of the text
            g.setColor(Color.white);
            g.setFont(new Font("Calisto MT", Font.BOLD, 18));
            g.drawString("YOU LOSE!", 550, 370);
        }

        if (winPoint == 5) {
            win.play();
            won = true;
            moved = true;
            dug = false;
            //draw String to annouce that the player has won
            //set color, font, font size and location of the text
            g.setColor(Color.white);
            g.setFont(new Font("Calisto MT", Font.BOLD, 18));
            g.drawString("YOU WON!", 550, 370);
        }
        
        //Request focus
        requestFocus();
    }

    public void keyPressed(KeyEvent event) { 
        // called in response to player key press
        //create local variable
        int k;
        //set k equal to the Key Code that player pressed
        k = event.getKeyCode();
        if (k == 37) /*left arrow*/ { 
            int a=0;
            while (a<10&&moved==false)
            {
                int b=0;
                while (b<10&moved==false)
                {
                    if (space[a][b]==true)
                    {
                        //(In uncovered field) make the red square looks like it moving around
                        if (uncovered [a][b-1] == neutralPic) {
                            uncovered [a][b-1] = neutralPicCurrent;
                        }
                        else if (uncovered [a][b-1] == goodPic) {
                            uncovered [a][b-1] = goodPicCurrent;
                        }
                        else if (uncovered [a][b-1] == badPic) {
                            uncovered [a][b-1] = badPicCurrent;
                        }
                        
                        if (uncovered [a][b] == neutralPicCurrent) {
                            uncovered [a][b] = neutralPic;
                        }
                        else if (uncovered [a][b] == goodPicCurrent) {
                            uncovered [a][b] = goodPic;
                        }
                        else if (uncovered [a][b] == badPicCurrent) {
                            uncovered [a][b] = badPic;
                        }
                        
                        //If the next tile the ball will move into is an uncovered-field tile, the ball will look like it becomes a red square 
                        if (dugTile[a][b-1] == false) {
                            field[a][b-1]=uncovered[a][b-1];
                        }
                        else {
                            field[a][b-1]=ball;
                        }
                        
                        //Set up statements so the uncovered field tile doesn't disappear after the ball passed by
                        if (dug == true || dugTile[a][b] == false) {
                            field[a][b]=uncovered[a][b];
                        }
                        else {
                            String fileLocation="images/["+a+"]["+b+"].jpeg";
                            field[a][b]=getImage(getCodeBase(),fileLocation);
                        }
                        
                        //play sound effect
                        move.play();
                        space[a][b]=false;
                        space[a][b-1]=true;
                        moved=true;
                        userCols -= 1;
                    }
                    b++;
                }
                a++;
            }
            dug = false;
        }
        else if (k == 39) /*right arrow*/ {
            int a=0;
            while (a<10&&moved==false)
            {
                int b=0;
                while (b<10&moved==false)
                {
                    if (space[a][b]==true)
                    {
                        //(In uncovered field) make the red square looks like it moving around
                        if (uncovered [a][b+1] == neutralPic) {
                            uncovered [a][b+1] = neutralPicCurrent;
                        }
                        else if (uncovered [a][b+1] == goodPic) {
                            uncovered [a][b+1] = goodPicCurrent;
                        }
                        else if (uncovered [a][b+1] == badPic) {
                            uncovered [a][b+1] = badPicCurrent;
                        }

                        if (uncovered [a][b] == neutralPicCurrent) {
                            uncovered [a][b] = neutralPic;
                        }
                        else if (uncovered [a][b] == goodPicCurrent) {
                            uncovered [a][b] = goodPic;
                        }
                        else if (uncovered [a][b] == badPicCurrent) {
                            uncovered [a][b] = badPic;
                        }
                        
                        //If the next tile the ball will move into is an uncovered-field tile, the ball will look like it becomes a red square
                        if (dugTile[a][b+1] == false) {
                            field[a][b+1]=uncovered[a][b+1];
                        }
                        else {
                            field[a][b+1]=ball;
                        }
                        
                        //Set up statements so the uncovered field tile doesn't disappear after the ball passed by
                        if (dug == true || dugTile[a][b] == false) {
                            field[a][b]=uncovered[a][b];
                        }
                        else {
                            String fileLocation="images/["+a+"]["+b+"].jpeg";
                            field[a][b]=getImage(getCodeBase(),fileLocation);
                        }
                        
                        //play sound effect
                        move.play();
                        userCols += 1;
                        space[a][b]=false;
                        space[a][b+1]=true;
                        moved=true;
                    }
                    b++;
                }
                a++;
            }
            dug = false;
        }
        else if (k == 38) /*up arrow*/ {
            int a=0;
            while (a<10&&moved==false)
            {
                int b=0;
                while (b<10&moved==false)
                {
                    if (space[a][b]==true)
                    {
                        //(In uncovered field) make the red square looks like it moving around
                        if (uncovered [a-1][b] == neutralPic) {
                            uncovered [a-1][b] = neutralPicCurrent;
                        }
                        else if (uncovered [a-1][b] == goodPic) {
                            uncovered [a-1][b] = goodPicCurrent;
                        }
                        else if (uncovered [a-1][b] == badPic) {
                            uncovered [a-1][b] = badPicCurrent;
                        }

                        if (uncovered [a][b] == neutralPicCurrent) {
                            uncovered [a][b] = neutralPic;
                        }
                        else if (uncovered [a][b] == goodPicCurrent) {
                            uncovered [a][b] = goodPic;
                        }
                        else if (uncovered [a][b] == badPicCurrent) {
                            uncovered [a][b] = badPic;
                        }
                        
                        //If the next tile the ball will move into is an uncovered-field tile, the ball will look like it becomes a red square
                        if (dugTile[a-1][b] == false) {
                            field[a-1][b]=uncovered[a-1][b];
                        }
                        else {
                            field[a-1][b]=ball;
                        }
                        
                        //Set up statements so the uncovered field tile doesn't disappear after the ball passed by
                        if (dug == true || dugTile[a][b] == false) {
                            field[a][b]=uncovered[a][b];
                        }
                        else {
                            String fileLocation="images/["+a+"]["+b+"].jpeg";
                            field[a][b]=getImage(getCodeBase(),fileLocation);
                        }
                        
                        //play sound effect
                        move.play();
                        userRows -= 1;
                        space[a][b]=false;
                        space[a-1][b]=true;
                        moved=true;
                    }
                    b++;
                }
                a++;
            }
            dug = false;
        }
        else if (k == 40) /*down arrow*/ {
            int a=0;
            while (a<10&&moved==false)
            {
                int b=0;
                while (b<10&moved==false)
                {
                    if (space[a][b]==true)
                    {
                        //(In uncovered field) make the red square looks like it moving around
                        if (uncovered [a+1][b] == neutralPic) {
                            uncovered [a+1][b] = neutralPicCurrent;
                        }
                        else if (uncovered [a+1][b] == goodPic) {
                            uncovered [a+1][b] = goodPicCurrent;
                        }
                        else if (uncovered [a+1][b] == badPic) {
                            uncovered [a+1][b] = badPicCurrent;
                        }
                        
                        if (uncovered [a][b] == neutralPicCurrent) {
                            uncovered [a][b] = neutralPic;
                        }
                        else if (uncovered [a][b] == goodPicCurrent) {
                            uncovered [a][b] = goodPic;
                        }
                        else if (uncovered [a][b] == badPicCurrent) {
                            uncovered [a][b] = badPic;
                        }
                        
                        //If the next tile the ball will move into is an uncovered-field tile, the ball will look like it becomes a red square
                        if (dugTile[a+1][b] == false) {
                            field[a+1][b]=uncovered[a+1][b];
                        }
                        else {
                            field[a+1][b]=ball;
                        }
                        
                        //Set up statements so the uncovered field tile doesn't disappear after the ball passed by
                        if (dug == true || dugTile[a][b] == false) {
                            field[a][b]=uncovered[a][b];
                        }
                        else {
                            String fileLocation="images/["+a+"]["+b+"].jpeg";
                            field[a][b]=getImage(getCodeBase(),fileLocation);
                        }
                        
                        //play sound effect
                        move.play();
                        userRows += 1;
                        space[a][b]=false;
                        space[a+1][b]=true;
                        moved=true;
                    }
                    b++;
                }
                a++;
            }
            dug = false;
        }
        else if (k == 32) /*spacebar*/ {
            dug = true;
            int a=0;
            while (a<10&&moved==false)
            {
                int b=0;
                while (b<10&moved==false)
                {
                    if (space[a][b]==true)
                    {
                        //import field images so their order doesn't mess up when we press spacebar
                        String fileLocation="images/["+a+"]["+b+"].jpeg";
                        field[a][b]=getImage(getCodeBase(),fileLocation);
                        //field tile is replaced by uncovered field tile
                        field[a][b]=uncovered[a][b];
                        //set up state for the dugTile array
                        dugTile[userRows][userCols] = false;
                        space[a][b]=true;
                        //There are 2 conditions to avoid player to gain more points by pressing more spacebar
                        if (uncovered[a][b] == goodPicCurrent && pressedTile[a][b] == false) {
                            winPoint +=1;
                            good.play();
                        }
                        else if (uncovered[a][b] == badPicCurrent && pressedTile[a][b] == false) {
                            losePoint -=1;
                            bad.play();
                        }
                        pressedTile[a][b] = true;
                    }
                    b++;
                }
                a++;
            }

        }
       
        moved=false;
        //Call repaint
        repaint();
    }

    public void keyReleased(KeyEvent event) {

    }

    public void keyTyped(KeyEvent event) {

    }

    public void actionPerformed(ActionEvent e) {
        buttonPressed=e.getActionCommand();
        if (buttonPressed == "START") {           
            //set up 100 space boolean with all false status
            space = new Boolean[10][10];
            for (int a=0;a<10;a++) {
                for (int b=0;b<10;b++) {
                    space[a][b]=false;
                }
            }
            
            //set up 100 dugTile boolean with all true status
            dugTile = new Boolean[10][10];
            for (int a=0;a<10;a++) {
                for (int b=0;b<10;b++) {
                    dugTile[a][b]=true;
                }
            }
            
            //set up 100 pressedTile boolean with all false status
            pressedTile = new Boolean[10][10];
            for (int a=0;a<10;a++) {
                for (int b=0;b<10;b++) {
                    pressedTile[a][b]=false;
                }
            }
            
            //set up 100 occupied boolean with all false status
            occupied = new Boolean[10][10];
            for (int a=0;a<10;a++) {
                for (int b=0;b<10;b++) {
                    occupied[a][b]=false;
                }
            }
            
            //set up status for each Boolean
            moved = false;
            won = false;
            lost = false;
            dug = false;
            
            // set location of the ball everytime pressed "start"
            //player can't see or move the ball if he/she didn't press "start"
            field[9][9] = ball;
            userRows = 9;
            userCols = 9;
            //set space [9][9] = true so the ball can be able to move
            space[9][9] = true;
            
            //set up initial points 
            losePoint = 3;
            winPoint = 0;
            
            //"remove" the START button after the player clicked 
            start.setVisible(false);
            remove(start);
            
            //replace it with 2 new buttons: RESTART and GIVE UP
            add(restart);
            add(giveup);     
            
            //randomize # of hearts and poops
            numbsGood = (int)(Math.random()*5+10);
            numbsBad = (int)(Math.random()*11+10); //times 11 instead of 5 to make sure # of poop is always larger than # of hearts
                                                   // -> make the game more challenging to win
                                                   
            //randomize the locations of the hearts
            for (int a = 0; a <= numbsGood; a++) {
                randX=(int)(Math.random()*10);
                randY=(int)(Math.random()*10);
                uncovered[randX][randY]=goodPic;
            }
            //randomize the locations of the poops
            for (int a = 0; a <= numbsBad; a++) {
                randX=(int)(Math.random()*10);
                randY=(int)(Math.random()*10);
                uncovered[randX][randY]=badPic;
            }
            
            //if the player pressed "spacebar" at [9][9] location at the very beginning, the red square will appear right the way on uncovered field tiles
            if (uncovered[9][9] == neutralPic) {
                uncovered[9][9] = neutralPicCurrent;
            }
            else if (uncovered[9][9] == goodPic) {
                uncovered[9][9] = goodPicCurrent;
            }
            else if (uncovered[9][9] == badPic) {
                uncovered[9][9] = badPicCurrent;
            }

        }
        else if (buttonPressed == "RESTART") {
            //repaint everything to be the field
            for (int a = 0; a < 10;a++) {
                for (int b = 0; b<10; b++) {
                    String fileLocation="images/["+a+"]["+b+"].jpeg";
                    field[a][b]=getImage(getCodeBase(),fileLocation);
                }
            }
            
            //re-setup status for all the booleans and boolean arrays
            for (int a=0;a<10;a++) {
                for (int b=0;b<10;b++) {
                    space[a][b]=false;
                }
            }

            for (int a=0;a<10;a++) {
                for (int b=0;b<10;b++) {
                    dugTile[a][b]=true;
                }
            }

            pressedTile = new Boolean[10][10];
            for (int a=0;a<10;a++) {
                for (int b=0;b<10;b++) {
                    pressedTile[a][b]=false;
                }
            }

            occupied = new Boolean[10][10];
            for (int a=0;a<10;a++) {
                for (int b=0;b<10;b++) {
                    occupied[a][b]=false;
                }
            }

            for (int a = 0; a < 3; a++) {
                heart[a] = lives;
            }

            for (int a = 0; a < 5; a++) {
                light[a] = lightbulb;
            }

            moved = false;
            won = false;
            lost = false;
            dug = false;

            field[9][9] = ball;
            userRows = 9;
            userCols = 9;
            space[9][9] = true;

            losePoint = 3;
            winPoint = 0;

            for (int a = 0; a <10;a++) {
                for (int b = 0; b<10; b++) {
                    uncovered[a][b]=neutralPic;
                }
            }

            numbsGood = (int)(Math.random()*5+10);
            numbsBad = (int)(Math.random()*11+10);

            for (int a = 0; a <= numbsGood; a++) {
                randX=(int)(Math.random()*10);
                randY=(int)(Math.random()*10);
                uncovered[randX][randY]=goodPic;
            }

            for (int a = 0; a <= numbsBad; a++) {
                randX=(int)(Math.random()*10);
                randY=(int)(Math.random()*10);
                uncovered[randX][randY]=badPic;
            }

            if (uncovered[9][9] == neutralPic) {
                uncovered[9][9] = neutralPicCurrent;
            }
            else if (uncovered[9][9] == goodPic) {
                uncovered[9][9] = goodPicCurrent;
            }
            else if (uncovered[9][9] == badPic) {
                uncovered[9][9] = badPicCurrent;
            }

        }

        else if (buttonPressed == "GIVE UP") {
            //play sound effect
            lose.play();
            //replace all the field tiles with uncovered tiles
            for (int a = 0; a <10;a++) {
                for (int b = 0; b<10; b++) {
                    field[a][b]=uncovered[a][b];
                }
            }
            //set space array status all to be false so the player can't no longer be able to move
            for (int a=0;a<10;a++) {
                for (int b=0;b<10;b++) {
                    space[a][b]=false;
                }
            }
            //reset boolean status 
            moved = true;
            dug = false;
            lost = true;
        }
        //call Repaint
        repaint();
    }
}
