package com.flyhigh.tmillner.jigsawpicture;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainScreen extends AppCompatActivity {

    public static final String TAG = "MAINSCREEN";
    private static Integer columns = 3;
    private static Integer rows = 3;
    private static final String CHUNK_PREFIX = "img";

    private View.OnLongClickListener mLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            Log.i(TAG, "INSIDE");
            ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
            ClipData dragData = new ClipData((CharSequence) v.getTag(),
                    new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
            View.DragShadowBuilder shadow = new MyDragShadowBuilder(v);
            v.startDrag(dragData,  // the data to be dragged
                    shadow,  // the drag shadow builder
                    v,      // no need to use local data
                    0          // flags (not currently used, set to 0)
            );
            return true;
        }
    };

    private View.OnDragListener mDragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DROP:
                    Log.i(TAG, "IN DROP");

                    ImageView dragged = (ImageView) event.getLocalState();

                    Log.i(TAG, " v is " + v.toString() + v.getTag()); // tile tag
                    Log.i(TAG, " view is " + v.toString() + dragged.getTag()); // jigsaw tag

                    if (v.getTag() == dragged.getTag()) {
                        ((LinearLayout)dragged.getParent()).removeView(dragged);
                        ((ImageView)v).setImageDrawable(dragged.getDrawable());
                    }
                    break;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int[] drawables = new int[rows*columns];

        // Run the function to break up a picture save the rows/columns
        int chunk =0;
        for (int col =0; col < columns; col++){
            for (int row=0; row < rows; row++) {
                drawables[chunk] = chunk;
                GridLayout grid = (GridLayout) findViewById(R.id.gridLayout);

                // Set tile
                ImageView tile = new ImageView(getApplicationContext());
                tile.setImageResource(R.drawable.tile);
                //tile.setImageResource(getResources().getIdentifier(
                //        CHUNK_PREFIX + chunk, "drawable", getPackageName()));
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(150, 150);
                tile.setLayoutParams(lp);
                Log.i(TAG, "chunk is " + chunk);
                tile.setTag(Integer.toString(chunk));
                //tile.getLayoutParams().height = 500;
                //tile.getLayoutParams().width = 500;
                tile.setOnDragListener(mDragListener);
                grid.addView(tile);
                // Set jigsaw

                LinearLayout drawer = (LinearLayout) findViewById(R.id.drawer);
                Log.i(TAG, drawer.toString());
                Log.i(TAG, drawer.getLayoutParams().toString());
                ImageView jigsaw = new ImageView(getApplicationContext());
                LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(150, 150);
                jigsaw.setImageResource(getResources().getIdentifier(
                        CHUNK_PREFIX + chunk, "drawable", getPackageName()));

                jigsaw.setLayoutParams(lp2);
                jigsaw.setTag(Integer.toString(chunk));
                jigsaw.setOnLongClickListener(mLongClickListener);

                drawer.addView(jigsaw);

                chunk++;
            }
        }

        /*
        for(int i=0; i<drawables.length; i++) {

            Drawable jigsaw = ResourcesCompat.getDrawable(
                    getResources(),
                    getResources().getIdentifier(
                            "img" + i + ".jpg", "drawable", getPackageName()),
                    null);
        }

        final ImageView jigsaw = (ImageView) findViewById(R.id.jigsaw_piece1);
        jigsaw.setTag("0");
        jigsaw.setOnLongClickListener(mLongClickListener);

        final ImageView jigsaw2 = (ImageView) findViewById(R.id.jigsaw_piece2);
        jigsaw2.setTag("1");
        jigsaw2.setOnLongClickListener(mLongClickListener);

        */

        /*
        final ImageView drop_tile = (ImageView) findViewById(R.id.tile1);
        drop_tile.setTag("1");
        drop_tile.setOnDragListener(mDragListener);

        final ImageView drop_tile2 = (ImageView) findViewById(R.id.tile2);
        drop_tile.setTag("2");
        drop_tile2.setOnDragListener(mDragListener);
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mainscreen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            //case (R.id.one):
                //startActivity(new Intent(this, SettingsActivity.class));
                //return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private static class MyDragShadowBuilder extends View.DragShadowBuilder {

        private static Drawable shadow;
        public MyDragShadowBuilder(View v) {
            super(v);
            shadow = new ColorDrawable(Color.LTGRAY);
        }

        @Override
        public void onProvideShadowMetrics (Point size, Point touch) {
            int width, height;

            width = getView().getWidth() / 2;
            height = getView().getHeight() / 2;

            shadow.setBounds(0, 0, width, height);

            size.set(width, height);
            touch.set(width / 2, height / 2);
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            shadow.draw(canvas);
        }
    }
}


