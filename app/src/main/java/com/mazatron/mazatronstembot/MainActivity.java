package com.mazatron.mazatronstembot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    private GridLayout mGridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridView = findViewById(R.id.mainGrid);

        for (int i=0; i< mGridView.getChildCount(); i++){
            final CardView mCardView =  (CardView) mGridView.getChildAt(i);
            final int finalI = i;

            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (finalI){
                        case 0:
                            //Line Follower
                            openLineFollower();
                            break;
                        case 1:
                            //Obstacle Avoider
                            openObstacleAvoider();
                            break;
                        case 2:
                            //Wall Hugging
                            openWallHugging();
                            break;
                        case 3:
                            //Maze Solver
                            openMazeSolver();
                            break;
                        case 4:
                            //IR Bot
                            openIrBot();
                            break;
                        case 5:
                            //Bluetooth robot
                            openBleBot();
                            break;
                    }

                }
            });
        }
    }

    private void openLineFollower(){
        Intent lineFollwerIntent = new Intent(this, LineFollwerActivity.class);
        lineFollwerIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        lineFollwerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(lineFollwerIntent);
    }

    private void openObstacleAvoider(){
        Intent obstacleAvoiderIntent = new Intent(this, ObstacleAvoiderActivity.class);
        obstacleAvoiderIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        obstacleAvoiderIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(obstacleAvoiderIntent);
    }

    private void openWallHugging(){
        Intent wallHuggingIntent = new Intent(this, WallHuggingActivity.class);
        wallHuggingIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        wallHuggingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(wallHuggingIntent);

    }

    private void openMazeSolver(){
        Intent mazeSolverIntent = new Intent(this, MazeSolverActivity.class);
        mazeSolverIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        mazeSolverIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mazeSolverIntent);

    }

    private void openIrBot(){
        Intent irBotIntent = new Intent(this, IrBotActivity.class);
        irBotIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        irBotIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(irBotIntent);

    }

    private void openBleBot(){
        Intent bleBotIntent = new Intent(this, BleBotActivity.class);
        bleBotIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        bleBotIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(bleBotIntent);
    }
}
