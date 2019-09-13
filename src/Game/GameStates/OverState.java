package Game.GameStates;

import Main.Handler;
import Resources.Images;
import UI.UIImageButton;
import UI.UIManager;

import java.awt.*;

/**
 * Created by AlexVR on 7/1/2018.
 */
public class OverState extends State {

    private int count = 0;
    private UIManager uiManager;

    public OverState(Handler handler) {
        super(handler);
        uiManager = new UIManager(handler);
        handler.getMouseManager().setUimanager(uiManager);

        uiManager.addObjects(new UIImageButton(150, 20, 128, 64, Images.Restart, () -> { //location of restart button - Ademir
            handler.getMouseManager().setUimanager(null);
            handler.getGame().reStart();
            State.setState(handler.getGame().gameState);
           
        }));

        uiManager.addObjects(new UIImageButton(350, 20, 128, 64, Images.BTitle, () -> {  //location of title button at over state -Ademir
            handler.getMouseManager().setUimanager(null);
            State.setState(handler.getGame().menuState);
        }));

 



    }

    @Override
    public void tick() {
        handler.getMouseManager().setUimanager(uiManager);
        uiManager.tick();
        count++;
        if( count>=30){
            count=30;
        }
        if(handler.getKeyManager().pbutt && count>=30){
            count=0;

            State.setState(handler.getGame().gameState);
        }


    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Images.gameover,0,0,600,600,null);   //game over image size and location - Ademir
        uiManager.Render(g);
    }
}