package com.flyhigh.tmillner.jigsawpicture;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainScreen extends AppCompatActivity {

    private static final String JIGSAW_TAG = "jigsaw_tag1";
    private static final String TILE_TAG = "tile_tag1";
    public static final String TAG = "MAINSCREEN";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        View.OnLongClickListener mLongClickListener = new View.OnLongClickListener() {
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

        View.OnDragListener mDragListener = new View.OnDragListener() {
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

        final ImageView jigsaw = (ImageView) findViewById(R.id.jigsaw_piece1);
        jigsaw.setTag("1");
        jigsaw.setOnLongClickListener(mLongClickListener);

        final ImageView drop_tile = (ImageView) findViewById(R.id.tile1);
        drop_tile.setTag("1");
        drop_tile.setOnDragListener(mDragListener);
    }

    private static class MyDragShadowBuilder extends View.DragShadowBuilder {

        private static Drawable shadow;

        public MyDragShadowBuilder(View v) {

            // Stores the View parameter passed to myDragShadowBuilder.
            super(v);
            // Creates a draggable image that will fill the Canvas provided by the system.
            shadow = new ColorDrawable(Color.LTGRAY);
        }

        // Defines a callback that sends the drag shadow dimensions and touch point back to the
        // system.
        @Override
        public void onProvideShadowMetrics (Point size, Point touch) {
            // Defines local variables
            int width, height;

            width = getView().getWidth() / 2;
            height = getView().getHeight() / 2;

            // The drag shadow is a ColorDrawable. This sets its dimensions to be the same as the
            // Canvas that the system will provide. As a result, the drag shadow will fill the
            // Canvas.
            shadow.setBounds(0, 0, width, height);

            // Sets the size parameter's width and height values. These get back to the system
            // through the size parameter.
            size.set(width, height);

            // Sets the touch point's position to be in the middle of the drag shadow
            touch.set(width / 2, height / 2);
        }

        // Defines a callback that draws the drag shadow in a Canvas that the system constructs
        // from the dimensions passed in onProvideShadowMetrics().
        @Override
        public void onDrawShadow(Canvas canvas) {

            // Draws the ColorDrawable in the Canvas passed in from the system.
            shadow.draw(canvas);
        }
    }
}


