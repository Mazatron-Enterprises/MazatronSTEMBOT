package com.mazatron.mazatronstembot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;

public class VideoActivity extends AppCompatActivity {

    private GridLayout mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        mGridView = findViewById(R.id.mainVGrid);

        for (int j=0; j< mGridView.getChildCount(); j++){

                final CardView mCardView =  (CardView) mGridView.getChildAt(j);
                final int finalI = j;

                mCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (finalI){
                            case 0:
                                //Line Follower
                                watchYoutubeVideo(getApplicationContext(),"BCVUpu9doLM");
                                break;
                            case 1:
                                //Obstacle Avoider
                                watchYoutubeVideo(getApplicationContext(),"BCVUpu9doLM");
                                break;
                            case 2:
                                //Wall Hugging
                                watchYoutubeVideo(getApplicationContext(),"BCVUpu9doLM");
                                break;
                            case 3:
                                //Maze Solver
                                watchYoutubeVideo(getApplicationContext(),"BCVUpu9doLM");
                                break;
                            case 4:
                                //IR Bot
                                watchYoutubeVideo(getApplicationContext(),"BCVUpu9doLM");
                                break;
                            case 5:
                                //Bluetooth robot
                                watchYoutubeVideo(getApplicationContext(),"BCVUpu9doLM");
                                break;
                        }

                    }
                });
            }

        }

    public static void watchYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
        webIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        webIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

}
