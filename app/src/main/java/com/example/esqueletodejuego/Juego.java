package com.example.esqueletodejuego;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class Juego extends SurfaceView implements SurfaceHolder.Callback {
    private Bitmap bmp;
    private SurfaceHolder holder;
    private BucleJuego bucle;

    private int x=0,y=0; //Coordenadas x e y para desplazar

    private static final int bmpInicialx=500;
    private static final int bmpInicialy=500;
    private static final int rectInicialx=450;
    private static final int rectInicialy=450;
    private static final int arcoInicialx=50;
    private static final int arcoInicialy=20;
    private static final int textoInicialx=50;
    private static final int textoInicialy=20;

    private int maxX=0;
    private int maxY=0;
    private int contadorFrames=0;
    private boolean hacia_abajo=true;
    private static final String TAG = Juego.class.getSimpleName();


    public Juego(Activity context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        Display mdisp = context.getWindowManager().getDefaultDisplay();
        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);
        maxX = mdispSize.x;
        maxY = mdispSize.y;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // se crea la superficie, creamos el game loop

        // Para interceptar los eventos de la SurfaceView
        getHolder().addCallback(this);

        // creamos el game loop
        bucle = new BucleJuego(getHolder(), this);

        // Hacer la Vista focusable para que pueda capturar eventos
        setFocusable(true);

        //comenzar el bucle
        bucle.start();

    }

    /**
     * Este m??todo actualiza el estado del juego. Contiene la l??gica del videojuego
     * generando los nuevos estados y dejando listo el sistema para un repintado.
     */
    public void actualizar() {
        if(x>maxX)
            hacia_abajo=false;

        if(x==0)
            hacia_abajo=true;


        if(hacia_abajo) {
            x = x + 1;
            y = y + 1;
        }
        else{
            x = x - 1;
            y = y - 1;
        }
        contadorFrames++;
    }

    /**
     * Este m??todo dibuja el siguiente paso de la animaci??n correspondiente
     */
    public void renderizar(Canvas canvas) {
        if(canvas!=null) {
            Paint myPaint = new Paint();
            myPaint.setStyle(Paint.Style.STROKE);

            //Toda el canvas en rojo
            canvas.drawColor(Color.RED);

            //Dibujar mu??eco de android
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            canvas.drawBitmap(bmp, bmpInicialx + x, bmpInicialy + y, null);

            //Cambiar color y tama??o de brocha
            myPaint.setStrokeWidth(10);
            myPaint.setColor(Color.BLUE);

            //dibujar rect??ngulo de 300x300
            canvas.drawRect(rectInicialx+x, rectInicialy+y, 300, 300, myPaint);

            //dibujar ??valo y arco
            RectF rectF = new RectF(arcoInicialx+x, arcoInicialy+y, 200, 120);
            canvas.drawOval(rectF, myPaint);
            myPaint.setColor(Color.BLACK);
            canvas.drawArc(rectF, 90, 45, true, myPaint);

            //dibujar un texto
            myPaint.setStyle(Paint.Style.FILL);
            myPaint.setTextSize(40);
            canvas.drawText("Frames ejecutados:"+contadorFrames, textoInicialx, textoInicialy+y, myPaint);

        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "Juego destruido!");
        // cerrar el thread y esperar que acabe
        boolean retry = true;
        while (retry) {
            try {
               // bucle.fin();
                bucle.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }
    }





}
