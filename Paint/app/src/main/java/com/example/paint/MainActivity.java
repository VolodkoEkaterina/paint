
package com.example.paint;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton currpaint, drawbtn, baru, erase, save;
    private DrawingView drawView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawView = (DrawingView)findViewById(R.id.drawing);
        drawbtn = (ImageButton)findViewById(R.id.draw_btn);
        baru = (ImageButton)findViewById(R.id.new_btn);
        erase = (ImageButton)findViewById(R.id.erase_btn);
        save = (ImageButton)findViewById(R.id.save_btn);
        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
        currpaint = (ImageButton)paintLayout.getChildAt(0);

        currpaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
        drawbtn.setOnClickListener(this);
        baru.setOnClickListener(this);
        erase.setOnClickListener(this);
        save.setOnClickListener(this);
    }
    public void paintClicked(View view){
        if(view !=currpaint){
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            drawView.setColor(color);
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currpaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currpaint = (ImageButton)view;
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.draw_btn){
            drawView.setupDrawing();
        }
        if(v.getId()==R.id.erase_btn){
            drawView.setErase(true);
            drawView.setBrushSize(drawView.getBrushSize());
        }
        if(v.getId()==R.id.new_btn){
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("New Drawing");
            newDialog.setMessage("Start new drawing");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    drawView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            newDialog.show();
        }
        if (v.getId()==R.id.save_btn){
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save Drawing");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    drawView.setDrawingCacheEnabled(true);
                    String imgSaved = MediaStore.Images.Media.insertImage(getContentResolver(),  drawView.getDrawingCache(), UUID.randomUUID().toString()+".png", "drawing");
                    if(imgSaved!=null){
                        Toast savedtoast = Toast.makeText(getApplicationContext(), "drawing save to Gallery!", Toast.LENGTH_SHORT);
                        savedtoast.show();
                    }
                    else{
                        Toast unsaved = Toast.makeText(getApplicationContext(), "Image could not saved.", Toast.LENGTH_SHORT);
                        unsaved.show();
                    }
                    drawView.destroyDrawingCache();
                }
            });
            saveDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
         saveDialog.show();
        }
    }
}