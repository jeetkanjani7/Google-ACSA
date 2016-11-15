package com.example.suchandd.board;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Suchand d on 11/13/2016.
 */

public class GameView extends View {


    int screenHeight;
    int screenWidth;
    int level;
    int rows,columns;
    int cellSize;
    int totalQueens;
    int movesMade;
    int n;
    static int startY;
    static int startX;

    boolean drawQueen[][];
    boolean redMark[][];
   int mat[][];
    Context context;
    Drawable queenIcon;
    Paint paint;
    Random r = new Random();
    int turn;

    public GameView(Context context,AttributeSet attrs) {
        super(context,attrs);
        this.context = context;
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;

        this.screenHeight = height;
        this.screenWidth = width-1;
        this.level = 4;


        movesMade = 0;
        totalQueens = rows = columns =4;
        n=rows;
        mat=new int[rows][columns];
        redMark = new boolean[rows][columns];
        drawQueen = new boolean[rows][columns];

        for(int i = 0 ; i < rows ; i++) {
            for( int j = 0 ; j < columns ; j++) {
                drawQueen[i][j] = false;
                redMark[i][j] = false;
                mat[i][j]=0;
            }
        }

        queenIcon = getResources().getDrawable(R.drawable.queen);


        if( screenHeight/rows < screenWidth/columns) {

            cellSize = screenHeight/rows;
            startX = 0;
            startY = 0;

        } else {

            cellSize = screenWidth/columns;
            startX = 0;
            startY = 0;

        }


        paint = new Paint();

        turn = r.nextInt(2);
        if(turn==0)
        {
            PlayComputer();
        }
    }
    boolean isGameOver(int  mat[][],int N){
        //terminating condition

        for(int i=0;i<N;i++)
            for(int j=0;j<N;j++)
               if(mat[i][j]==0)return false;
        return true;
    }
    boolean isBoundry(int x,int y,int N){
        return x>=0 && x<N && y>=0 && y<N;
    }
    public void blockCellUtil(int N, int mat[][],int x, int y,int delX,int delY){
        for(int i=x,j=y;;i+=delX,j+=delY)
            if(isBoundry(i,j,N)==false)
                return;
            else
                mat[i][j]=1;//filled
    }
    public void blockCell(int N, int mat[][],int x, int y){
        for(int delX=-1;delX<=1;delX++)
            for(int delY=-1;delY<=1;delY++)
                if(delX==0 && delY==0)
                    continue;
                else
                    blockCellUtil(N,mat,x,y,delX,delY);
    }
    public void print(int mat[][],int n){
        for(int i=0;i<n;i++)
        {
                for(int j=0;j<n;j++) {
                    System.out.printf("%d  ", mat[i][j]);
                }
             System.out.println();
        }
    }

    int toggle(int turn){
        return turn == 1 ? 0 : 1 ;
    }
    int solve(int n,int turn,int mat[][] ){
        for(int i=0;i<n;i++)  {
            for(int j=0;j<n;j++) {
                if(mat[i][j]!=1){
                   int temp[][]=new int[n][n];
                    for(int x=0;x<n;x++)
                    {
                        for(int y=0;y<n;y++)
                        {
                            temp[x][y]=mat[x][y];
                        }
                    }
                    temp[i][j]=1;
                    blockCell(n,temp,i,j);
                    if(solve(n,toggle(turn),temp)==toggle(turn)){
                        return toggle(turn);
                    }
                }

            }

        }
        return turn;

    }
    public void PlayComputer()
    {
        System.out.printf("COMPUTER\n");
        boolean isPlayed=false;

        for(int i=0;i<n&&isPlayed==false;i++){
            for(int j=0;j<n&&isPlayed==false;j++){
                if(mat[i][j]!=1){
                    int temp[][]=new int[n][n];
                    for(int x1=0;x1<n;x1++)
                    {
                        for(int y1=0;y1<n;y1++)
                        {
                            temp[x1][y1]=mat[x1][y1];
                        }
                    }
                    temp[i][j]=1;
                    blockCell(n,temp,i,j);
                    if(solve(n,turn,temp)==1){
                        isPlayed=true;

                        if(drawQueen[i][j]==false) {
                            drawQueen[i][j] = true;
                            totalQueens++;

                            mat[i][j]=1;
                            blockCell(rows,mat,i,j);

                            invalidate();
                            if(isGameOver(mat,rows))
                            {
                                Toast.makeText(this.context,"Computer won",Toast.LENGTH_SHORT).show();
                                reset();
                            }

                        }
                    }
                }
            }
        }

    }

    public void reset()
    {

        for(int i = 0 ; i < rows ; i++) {
            for( int j = 0 ; j < columns ; j++) {
                drawQueen[i][j] = false;
                redMark[i][j] = false;
                mat[i][j]=0;
            }


        }
      invalidate();
        turn = r.nextInt(2);
        if(turn==0)
        {
            PlayComputer();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {



        int X = startX;
        int Y = startY;

        int k = 0;

        for(int i = 0 ; i < rows ; i++) {

            for( int j = 0 ; j < columns ; j++) {

                paint.setStyle(Paint.Style.FILL);

                if(k % 2 == 0) {

                    paint.setColor(Color.GRAY);
                    canvas.drawRect( X, Y, X+cellSize, Y+cellSize, paint);
                }
                else {

                    paint.setColor(Color.WHITE);
                    canvas.drawRect( X, Y, X+cellSize, Y+cellSize, paint);
                }

                if(redMark[i][j])
                    paint.setColor(Color.RED);
                else
                    paint.setColor(Color.BLACK);

                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(5);
                canvas.drawRect( X, Y, X+cellSize, Y+cellSize, paint);

                if(drawQueen[i][j]) {
                  queenIcon.setBounds(X, Y, X+cellSize, Y+cellSize);
                    queenIcon.draw(canvas);
                }


                k++;
                X += cellSize;
            }
            X = startX;
            Y += cellSize;
            if(rows % 2 == 0)
                k++;
        }



    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch(event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                markCell(event.getX(),event.getY());


                break;

        }

        return true;
    }

    public void markCell(float X, float Y) {

        int x = startX;
        int y = startY;
        int I=-1,J=-1;
        for(int i = 0 ; i < rows ; i++) {

            for( int j = 0 ; j < columns ; j++) {


                if(X > x && X < x+cellSize && Y > y && Y < y+cellSize ) {


                   if(mat[i][j]==0)
                   {
                       I=i;
                       J=j;
                   }


                }


                x += cellSize;
            }
            x = startX;
            y += cellSize;
        }
       if(I==-1||J==-1)
       {
           System.out.printf("computer won");
           Toast.makeText(this.context,"Computer won",Toast.LENGTH_SHORT).show();
           reset();
       }
        else
       {
           int i=I;
           int j=J;

               drawQueen[i][j] = true;
               totalQueens++;

               mat[i][j]=1;
               blockCell(rows,mat,i,j);

               invalidate();

           if(isGameOver(mat,rows))
           {
               Toast.makeText(this.context,"user won",Toast.LENGTH_SHORT).show();
               reset();
           }
           else
           {
                PlayComputer();

           }
       }

    }

}
