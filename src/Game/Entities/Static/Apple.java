package Game.Entities.Static;

import Game.Entities.Dynamic.Player;
import Main.Handler;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Apple {

    private Handler handler;

    public int xCoord;
    public int yCoord;

    public Apple(Handler handler,int x, int y){
        this.handler=handler;
        this.xCoord=x;
        this.yCoord=y;
    }

	public static boolean isGood() {
		boolean goodApple = true;				//If the snake takes (x) steps, apples rot.
		
		if (Player.stepToEatCounter > 150) {
			goodApple = false;
		}
		return goodApple;
	}

}
