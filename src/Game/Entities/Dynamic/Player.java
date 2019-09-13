package Game.Entities.Dynamic;

import java.lang.Math;

import Main.Handler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

import Game.Entities.Static.Apple;
import Game.GameStates.State;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Player {

    public int lenght;
    public boolean justAte;
    private Handler handler;
    public double i= 3 + 1 ; 					//ID LAST DIGIT = 3 (DIEGO) (Variable used to control speed) -Ademir
    public int xCoord;
    public int yCoord;

    public int moveCounter;
    public static int stepToEatCounter;  	// Variable that counts steps the snake takes between Eats. -Diego
    public static int scoreCounter = 0; 	// Variable that keeps track of the score -Ademir
    public double getScoreCounter() {
		return scoreCounter;
    }
    public static String toString(int i) {  // Returns a string of the current score (used to print a 0 upon restart) 
    	return "" + i;
    }
    
    public String direction;//is your first name one?

    public Player(Handler handler){
        this.handler = handler;
        xCoord = 0;
        yCoord = 0;
        moveCounter = 0;
        direction= "Right";
        justAte = false;
        lenght= 1;

    }

    public void tick(){
        moveCounter++;
        
        if(moveCounter>=5 + i) {
            checkCollisionAndMove();
            stepToEatCounter++;
            moveCounter=0;
        }
        
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP) && direction != "Down"){        //Implemented anti backtracking for the snake -Ademir
            direction="Up";
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN) && direction != "Up"){
            direction="Down";
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT) && direction != "Right"){
            direction="Left";
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT) && direction != "Left"){
            direction="Right";
            
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_MINUS)){// Decreases speed -Ademir
        	i++;
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_EQUALS)){// Increases speed -Ademir
        	i--;
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ESCAPE)){   //Pause the game -Ademir
        	State.setState(handler.getGame().pauseState);
        }
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_N)){  //if you press N the Snake Increases in length without adding an apple at a random location -Ademir
        	lenght++;
            Tail tail= new Tail (this.xCoord, this.yCoord, handler);
            handler.getWorld().body.addLast(tail);
        }
       
    }

    public void checkCollisionAndMove(){
        handler.getWorld().playerLocation[xCoord][yCoord]=false;
        int x = xCoord;
        int y = yCoord;
        switch (direction){
            case "Left":
                if(xCoord==0){
                    xCoord = handler.getWorld().GridWidthHeightPixelCount-1; //Snake teleports to the other side of the screen when hitting the edge. -Diego
                }else{
                    xCoord--;
                }
                break;
            case "Right":
                if(xCoord==handler.getWorld().GridWidthHeightPixelCount-1){
                	xCoord = 0;
                }else{
                    xCoord++;
                }
                break;
            case "Up":
                if(yCoord==0){
                    yCoord = handler.getWorld().GridWidthHeightPixelCount-1;
                }else{
                    yCoord--;
                }
                break;
            case "Down":
                if(yCoord==handler.getWorld().GridWidthHeightPixelCount-1){
                    yCoord = 0;
                }else{
                    yCoord++;
                }
                break;
        }
        handler.getWorld().playerLocation[xCoord][yCoord]=true;
        LinkedList<Tail> b = handler.getWorld().body;
        for(int i =0; i<b.size(); i++){
        	Tail t = b.get(i);
        	if(xCoord == t.x){
        		if(yCoord == t.y){
        			kill();
        			Player.scoreCounter = 0;
        			State.setState(handler.getGame().overState);
        			//handler.getGame().stop(); //Kills the snake when impacting itself can be updated -Ademir
        		}
        	}
        }

        if(handler.getWorld().appleLocation[xCoord][yCoord]){
            Eat();
            stepToEatCounter = 0;
        }

        if(!handler.getWorld().body.isEmpty()) {
            handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
            handler.getWorld().body.removeLast();
            handler.getWorld().body.addFirst(new Tail(x, y,handler));
        }

    }

    public void render(Graphics g,Boolean[][] playeLocation){
        Random r = new Random();
        for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
            for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {

                if(playeLocation[i][j]){							//Colors are now different for snake and apple. -Diego
                	g.setColor(Color.green);
                    g.fillRect((i*handler.getWorld().GridPixelsize),
                            (j*handler.getWorld().GridPixelsize),
                            handler.getWorld().GridPixelsize,
                            handler.getWorld().GridPixelsize);
                }
                if(handler.getWorld().appleLocation[i][j]){
                	if (!Apple.isGood()) {							//Apple changes color when it rots. -Diego
                	g.setColor(new Color(89,0,0));
                    g.fillRect((i*handler.getWorld().GridPixelsize),
                            (j*handler.getWorld().GridPixelsize),
                            handler.getWorld().GridPixelsize,
                            handler.getWorld().GridPixelsize);
                	}else {
                		g.setColor(Color.red);
                        g.fillRect((i*handler.getWorld().GridPixelsize),
                                (j*handler.getWorld().GridPixelsize),
                                handler.getWorld().GridPixelsize,
                                handler.getWorld().GridPixelsize);
                	}
                }

            }
        }


    }

    public void Eat(){
    	if (Apple.isGood()) {
    		scoreCounter += (Math.round(Math.sqrt(2* scoreCounter+ 1)*100)/100.0); //Score equation implemented and rounded to two digits -Ademir
    		lenght++;
        	i = (i - 0.4);  //Speed Increased if apple is eaten -Ademir
        }else {
        	scoreCounter -= (Math.round(Math.sqrt(2* scoreCounter+ 1)*100)/100.0); //Score equation implemented and rounded to two digits -Ademir
            lenght--;
        	i = (i + 0.4);  //Speed Decreased if bad apple is eaten (CREATIVE DIRECTION). -Diego
        }
    	handler.getGame().score = scoreCounter+"";
    	
    	System.out.println(scoreCounter);
         
        Tail tail= null;
        handler.getWorld().appleLocation[xCoord][yCoord]=false;
        handler.getWorld().appleOnBoard=false;
        switch (direction){
            case "Left":
                if( handler.getWorld().body.isEmpty()){
                    if(this.xCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail = new Tail(this.xCoord+1,this.yCoord,handler);
                    }else{
                        if(this.yCoord!=0){
                            tail = new Tail(this.xCoord,this.yCoord-1,handler);
                        }else{
                            tail =new Tail(this.xCoord,this.yCoord+1,handler);
                        }
                    }
                }else{
                    if(handler.getWorld().body.getLast().x!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail=new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler);
                    }else{
                        if(handler.getWorld().body.getLast().y!=0){
                            tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler);
                        }else{
                            tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler);

                        }
                    }

                }
                break;
            case "Right":
                if( handler.getWorld().body.isEmpty()){
                    if(this.xCoord!=0){
                        tail=new Tail(this.xCoord-1,this.yCoord,handler);
                    }else{
                        if(this.yCoord!=0){
                            tail=new Tail(this.xCoord,this.yCoord-1,handler);
                        }else{
                            tail=new Tail(this.xCoord,this.yCoord+1,handler);
                        }
                    }
                }else{
                    if(handler.getWorld().body.getLast().x!=0){
                        tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
                    }else{
                        if(handler.getWorld().body.getLast().y!=0){
                            tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
                        }else{
                            tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
                        }
                    }

                }
                break;
            case "Up":
                if( handler.getWorld().body.isEmpty()){
                    if(this.yCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail=(new Tail(this.xCoord,this.yCoord+1,handler));
                    }else{
                        if(this.xCoord!=0){
                            tail=(new Tail(this.xCoord-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(this.xCoord+1,this.yCoord,handler));
                        }
                    }
                }else{
                    if(handler.getWorld().body.getLast().y!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
                    }else{
                        if(handler.getWorld().body.getLast().x!=0){
                            tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
                        }
                    }

                }
                break;
            case "Down":
                if( handler.getWorld().body.isEmpty()){
                    if(this.yCoord!=0){
                        tail=(new Tail(this.xCoord,this.yCoord-1,handler));
                    }else{
                        if(this.xCoord!=0){
                            tail=(new Tail(this.xCoord-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(this.xCoord+1,this.yCoord,handler));
                        } System.out.println("Tu biscochito");
                    }
                }else{
                    if(handler.getWorld().body.getLast().y!=0){
                        tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
                    }else{
                        if(handler.getWorld().body.getLast().x!=0){
                            tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
                        }
                    }

                }
                break;
        }
        
        if (Apple.isGood()) {
        	handler.getWorld().body.addLast(tail);
        	handler.getWorld().playerLocation[tail.x][tail.y] = true;
        } else {
        	if (handler.getWorld().body.isEmpty()){
        		kill();
    			Player.scoreCounter = 0;
    			State.setState(handler.getGame().overState);
        	} else {
        		handler.getWorld().body.removeLast();          //Remove last if apple is bad
            	handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
        	}
        }
    }

    public void kill(){
        lenght = 0;
        for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
            for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
            	
                handler.getWorld().playerLocation[i][j]=false;

            }
        }
    }

    public boolean isJustAte() {
        return justAte;
    }

    public void setJustAte(boolean justAte) {
        this.justAte = justAte;
    }
}
