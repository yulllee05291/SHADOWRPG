package com.example.shadowrpg;
import android.app.*; import android.os.*; import android.graphics.*; import android.view.*; import android.content.*; import java.util.*;
public class MainActivity extends Activity { public void onCreate(Bundle b){super.onCreate(b);getWindow().setFlags(1024,1024);setContentView(new GameView(this));}}
class GameView extends View{
 Paint p=new Paint(3); Random r=new Random(); int hp=100,max=100,lv=1,xp=0,gold=0,ehp=35,emax=35; boolean boss=false,dead=false; String msg="잊혀진 숲에 들어왔다!";
 GameView(Context c){super(c);p.setTypeface(Typeface.DEFAULT_BOLD);}
 void t(Canvas c,String s,float x,float y,float z){p.setColor(Color.WHITE);p.setTextSize(z);c.drawText(s,x,y,p);}
 void bar(Canvas c,float x,float y,float w,int v,int m,int col){p.setColor(Color.DKGRAY);c.drawRect(x,y,x+w,y+18,p);p.setColor(col);c.drawRect(x,y,x+w*Math.max(0,v)/(float)m,y+18,p);}
 void btn(Canvas c,float x,float y,float w,String s){p.setColor(Color.rgb(45,50,65));c.drawRoundRect(x,y,x+w,y+60,12,12,p);t(c,s,x+25,y+38,19);}
 protected void onDraw(Canvas c){int w=getWidth(),h=getHeight();c.drawColor(Color.rgb(10,15,22));p.setColor(Color.rgb(25,55,35));c.drawRect(0,h*.62f,w,h,p);p.setColor(Color.LTGRAY);c.drawCircle(w*.82f,h*.18f,55,p);
 t(c,"SHADOW RPG",25,35,25);t(c,"Lv."+lv+"  EXP "+xp+"/100  GOLD "+gold,25,68,17);bar(c,25,82,230,hp,max,Color.rgb(200,45,55));t(c,"HP "+hp+"/"+max,32,97,13);
 if(!dead){p.setColor(boss?Color.rgb(120,30,130):Color.rgb(150,60,45));c.drawCircle(w*.58f,h*.43f,boss?70:45,p);t(c,boss?"FOREST WARDEN":"SHADOW SLIME",w*.47f,h*.29f,17);bar(c,w*.47f,h*.52f,190,ehp,emax,Color.RED);}
 else {t(c,hp==0?"DEFEATED":"VICTORY!",w*.45f,h*.40f,36);}
 int y=h-75;btn(c,20,y,140,"공격");btn(c,175,y,150,"강공격");btn(c,w-315,y,135,"포션");btn(c,w-165,y,145,dead?"다시 시작":"다음 적");t(c,msg,25,h-92,14);}
 public boolean onTouchEvent(MotionEvent e){if(e.getAction()!=1)return true;float x=e.getX(),y=e.getY();int w=getWidth(),h=getHeight();if(y<h-100)return true;
 if(x<160)atk(10,16);else if(x<340)atk(18,28);else if(x<w-170){hp=Math.min(max,hp+35);msg="포션으로 HP를 회복했다!";enemy();}else if(dead)reset();else next();invalidate();return true;}
 void atk(int a,int b){if(dead)return;int d=a+r.nextInt(b-a+1);ehp-=d;msg=d+" 데미지!";if(ehp<=0){xp+=35;gold+=20;if(xp>=100){lv++;xp-=100;max+=15;hp=max;msg="레벨 업!";}dead=true;return;}enemy();}
 void enemy(){int d=5+r.nextInt(boss?11:7);hp-=d;if(hp<=0){hp=0;dead=true;msg="쓰러졌다...";}else msg+=" 적의 반격!";}
 void next(){boss=lv>=3&&gold>=60;emax=boss?150:35+lv*8;ehp=emax;dead=false;msg=boss?"숲의 수호자 등장!":"새로운 몬스터 등장!";}
 void reset(){hp=max=100;lv=1;xp=gold=0;boss=false;emax=ehp=35;dead=false;msg="모험을 다시 시작한다!";}
}