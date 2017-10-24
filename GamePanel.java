package com.example.kuba.repulsev001;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * The main class that manages the whole game (sets up, updates and draws everything).
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;

    /**
     * Says where in the app the player is (menu, game, etc.).
     * Leave 0 free as the "test state".
     */
    private int state;
    private static final int STATE_PLAYING=1;
    private static final int STATE_HEURA_MENU=12345;     //heuraMenu (temp)
    private GameMode match;

    private HeuraSlider[] sliders;     //heuraMenu (temp)
    private int sliderHold;     //heuraMenu (temp)
    private int t,w;     //heuraMenu (temp)




    public GamePanel(Context context){
        super(context);
        getHolder().addCallback(this);
        thread=new MainThread(getHolder(), this);
        setFocusable(true);

        sliderHold = -1;     //heuraMenu (temp)
        sliders=new HeuraSlider[11];     //heuraMenu (temp)
        t=(int) (Constants.screenHeight/(sliders.length*2.0+1.0));     //heuraMenu (temp)
        w=Constants.screenWidth;     //heuraMenu (temp)
        //here be setup
        DataBank.setUnit((float)((Constants.screenWidth+Constants.screenHeight)/(2000.0)));                    //TEMP
        DataBank.setGameField(new Rect(0,0,Constants.screenWidth,Constants.screenHeight-t)); //TEMP
        state=STATE_PLAYING;                                            //TEMP




        //float unit=DataBank.getUnit();




       resetHeuraSliders();     //heuraMenu (temp)




        match=new GMBouncer();                                          //TEMP
    }

    //heuraMenu (temp)
    void resetHeuraSliders(){
        sliders[ 0]=new HeuraSlider("Killing walls (1)"      ,1 ,0,1  ,2*t ,t,0,255,255);
        sliders[ 1]=new HeuraSlider("Player size (22)"       ,22,1,60 ,4*t ,t,255,0,127);
        sliders[ 2]=new HeuraSlider("Recoil speed (5)"       ,5 ,1,30 ,6*t ,t,255,0,127);
        sliders[ 3]=new HeuraSlider("Bullet speed (10)"      ,10,1,60 ,8*t ,t,255,191,0);
        sliders[ 4]=new HeuraSlider("Bullet size (7)"        ,7 ,1,40 ,10*t,t,255,191,0);
        sliders[ 5]=new HeuraSlider("Cooldown (25)"          ,25,0,100,12*t,t,127,0,255);
        sliders[ 6]=new HeuraSlider("Player gravity (0 or 5)",0 ,0,50 ,14*t,t,255,0,127);
        sliders[ 7]=new HeuraSlider("Bullet gravity (0 or 5)",0 ,0,50 ,16*t,t,255,191,0);
        sliders[ 8]=new HeuraSlider("Bullet lifetime (30)"   ,30,5,40 ,18*t,t,127,0,255);
        sliders[ 9]=new HeuraSlider("Killing bullets (1)"    ,1 ,0,1  ,20*t,t,0,255,255);
        sliders[10]=new HeuraSlider("Save/load file number"  ,1 ,1,30 ,22*t,t,255,127,0);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        thread=new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        while(true){
            try{
                thread.setRunning(false);
                thread.join();
            }catch(Exception e){
                e.printStackTrace();
            }
            retry = false;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        switch(event.getAction()){
            case MotionEvent.ACTION_UP: {
                mouseUp(event);
                return false;
            }
            case MotionEvent.ACTION_DOWN: {
                mouseDown(event);
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                mouseMoved(event);
                return false;
            }
            default:{
                //sliderHold=-1;
            }

        }

        return true;
        //return super.onTouchEvent(event);
    }



    public void update(){
        if(state==STATE_PLAYING){
            match.update();
        }
        else if(state==STATE_HEURA_MENU){
            //heuraMenu (temp)
        }
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        if(state==STATE_PLAYING){
            match.draw(canvas);
            Paint paint=new Paint();
            paint.setColor(Color.rgb(0,63,63));
            canvas.drawRect(0,Constants.screenHeight-t,w,Constants.screenHeight,paint);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize((float)(t*5.0/7));
            paint.setColor(Color.rgb(0,255,255));
            canvas.drawText(match.getScore()+"/"+match.getLastScore(),w/2,Constants.screenHeight-t/4,paint);
        }
        else if(state==STATE_HEURA_MENU){      //heuraMenu (temp)

            Paint paint=new Paint();
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize((float)(t*5.0/7));

            paint.setColor(Color.rgb(31,63,95));
            canvas.drawRect(0,0,w/4,t,paint);
            paint.setColor(Color.rgb(0,255,255));
            canvas.drawText("Play",w/8,t/4*3,paint);

            /*paint.setColor(Color.rgb(95,63,31));
            canvas.drawRect(w/4,0,w/4*2,t,paint);
            paint.setColor(Color.rgb(255,127,0));
            canvas.drawText("Load",w/8*3,t/4*3,paint);

            paint.setColor(Color.rgb(31,95,31));
            canvas.drawRect(w/4*2,0,w/4*3,t,paint);
            paint.setColor(Color.rgb(0,255,0));
            canvas.drawText("Save",w/8*5,t/4*3,paint);*/

            paint.setColor(Color.rgb(95,31,31));
            canvas.drawRect(w/4*3,0,w,t,paint);
            paint.setColor(Color.rgb(255,0,0));
            canvas.drawText("Reset",w/8*7,t/4*3,paint);

            //if(sliderHold>-1)
                //System.out.println("SliderHold: "+sliderHold);

            for(int i=0;i<sliders.length;i++){
                sliders[i].draw(canvas,sliderHold==i);
            }
        }
        //System.out.println("GamePanel");


    }

    void mouseDown(MotionEvent event){
        if(state==STATE_PLAYING){
            if(event.getY()<Constants.screenHeight-t) {
                match.mouseDown(event);
            }
            else{
                state=STATE_HEURA_MENU;     //heuraMenu (temp)
            }
        }
        else if(state==STATE_HEURA_MENU){     //heuraMenu (temp)
            if(event.getY()<t){
                if(event.getX()<w/4){
                    Player p=new Player(Constants.screenWidth/2,Constants.screenHeight/2,sliders[1].getVal(),sliders[5].getVal(),(float)(sliders[6].getVal()/50.0),sliders[0].getVal()==1,sliders[9].getVal()==1);
                    p.setShotParams(sliders[2].getVal(),sliders[3].getVal(),sliders[4].getVal(),(float)(sliders[7].getVal()/50.0));

                    match=new GMBouncer(p);
                    match.setBulletLifetime((float)(sliders[8].getVal()/40.0));
                    state=STATE_PLAYING;
                }
                else if(event.getX()<2*w/4) {
                    /*try {
                        Address address = ReadObject.deserialzeAddress("/repulse/address" + sliders[sliders.length - 1].getVal() + ".ser");
                        sliders = address.getSliders();
                    }catch (Exception e){

                    }
                    System.out.println("LOAD");*/
                }
                else if(event.getX()<3*w/4){
                    /*try {
                    Address address=new Address();
                    address.setSliders(sliders);
                    WriteObject.serializeAddress(address,sliders[sliders.length-1].getVal());
                    }catch (Exception e){

                    }
                    System.out.println("SAVE");*/
                }
                if(event.getX()>w/4*3){
                    resetHeuraSliders();
                }
            }
            else {
                for (int i = 0; i < sliders.length; i++) {
                    if (sliderHold < 0) {
                        if (sliders[i].in(event.getX(), event.getY())) {
                            sliderHold = i;

                            break;
                        }
                    }
                }
            }
        }
    }
//    void mouseMove(){
//
//    }
    void mouseUp(MotionEvent event){
        if(state==STATE_PLAYING){
            match.mouseUp(event);
        }
        else if(state==STATE_HEURA_MENU){     //heuraMenu (temp)
            sliderHold=-1;
            System.out.println("mouseUp!");
        }
    }
    private void mouseMoved(MotionEvent event) {
        if(state==STATE_PLAYING){

        }
        else if(state==STATE_HEURA_MENU){     //heuraMenu (temp)
            if(sliderHold>=0) {
                sliders[sliderHold].act(event.getX(), event.getY());
            }
        }
    }

}