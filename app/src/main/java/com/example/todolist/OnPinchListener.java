package com.example.todolist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.*;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

// Classe à base de choses trouvées sur stackOverflow
// Implémente un Listener pour détecter quand un mouvement de zoom (pincé) est fait sur l'objet

/* This listener is used to listen pinch zoom gesture. ß*/
public class OnPinchListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

    Button button;
    Task task;
    float factor;

    private DAOBase dataBase; // Base de données sur laquelle agir
    private GroupsList groupsList; // Ensemble des groupes

    public OnPinchListener(MainActivity mainActivity, Button b, Task task) {
        super();
        // Ouvrir la base de données
        dataBase = new DAOBase(mainActivity);
        dataBase.open();
        this.groupsList = dataBase.getGroupsList();

        button = b;
        this.task = task;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        factor = 1.0f;
        return true;
        //return super.onScaleBegin(detector);
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {

        // Mise à jour de la taille ici
        float scaleFactor = detector.getScaleFactor() - 1;
        factor += scaleFactor;
        button.setScaleX(factor);
        button.setScaleY(factor);

        // Mise à jour de la priorité de la tâche lorsqu'on change la taille
        int newPriority;
        if ((factor*button.getWidth()-150)/5 >= 100)     newPriority = 100;
        else if ((factor*button.getWidth()-150)/5 <= 0)  newPriority = 0;
        else                                             newPriority = ((int)(factor*button.getWidth())-150)/5;

        Log.d("debugging", "width, factor, newPriority : " + String.valueOf(button.getWidth()) + " " +
                String.valueOf(factor) + " " + String.valueOf(newPriority));

        dataBase.removeTask(task);
        dataBase.addTask(new Task(task.getName(), newPriority, task.getDeadline(), task.getGroup(), task.getPerson(), task.getCoordX(), task.getCoordY()));

        return true;
        //return super.onScale(detector);
    }

}