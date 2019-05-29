package com.example.devyankshaw.floatingview;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class FloatingViewService extends Service implements View.OnClickListener, View.OnTouchListener {

    private View floatingWidget;
    private WindowManager windowManager;
    private View collapsedView;
    private View expandedView;
    private View rootView;
    private WindowManager.LayoutParams parameters;

    int startXPos ;
    int startYPos ;
    float startTouchX ;
    float startTouchY ;

    @Override
    public void onCreate() {
        super.onCreate();

        floatingWidget = LayoutInflater.from(FloatingViewService.this).
                inflate(R.layout.float_view_layout, null);

        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);

         parameters = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        parameters.gravity = Gravity.TOP | Gravity.LEFT;
        parameters.x = 200;
        parameters.y = 200;

        windowManager.addView(floatingWidget, parameters);

        collapsedView = floatingWidget.findViewById(R.id.collapsed_view);//By this we can access the collapsed view/state of this floating widget
        ImageView collapsedCloseButton = (ImageView)floatingWidget.findViewById(R.id.collapsed_close_button);

        collapsedCloseButton.setOnClickListener(FloatingViewService.this);

         expandedView = floatingWidget.findViewById(R.id.expanded_view);
        ImageView lionImage = floatingWidget.findViewById(R.id.lionImage);
        lionImage.setOnClickListener(FloatingViewService.this);

        ImageView previousButton = floatingWidget.findViewById(R.id.btnPrevious);
        previousButton.setOnClickListener(FloatingViewService.this);

        ImageView leopardImage = floatingWidget.findViewById(R.id.leopardImage);
        leopardImage.setOnClickListener(FloatingViewService.this);

        ImageView nextButton = floatingWidget.findViewById(R.id.btnNext);
        nextButton.setOnClickListener(FloatingViewService.this);

        ImageView expandedCloseButton = floatingWidget.findViewById(R.id.close_button_expanded);
        expandedCloseButton.setOnClickListener(FloatingViewService.this);

        ImageView openButton = floatingWidget.findViewById(R.id.open_button);
        openButton.setOnClickListener(FloatingViewService.this);

        rootView = floatingWidget.findViewById(R.id.root_view);
        rootView.setOnTouchListener(FloatingViewService.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.collapsed_close_button:
                stopSelf();//Stops the service completely and the view/widget will be removed from the screen
                Toast.makeText(FloatingViewService.this,
                        "The Service is stopped completely", Toast.LENGTH_SHORT).show();
                break;

            case R.id.lionImage:
                Toast.makeText(FloatingViewService.this, "Lion is Tapped", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnPrevious:
                Toast.makeText(FloatingViewService.this, "Previous Button is Tapped", Toast.LENGTH_SHORT).show();
                break;

            case R.id.leopardImage:
                Toast.makeText(FloatingViewService.this, "Leopard Image is Tapped", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnNext:
                Toast.makeText(FloatingViewService.this, "Next Button is Tapped", Toast.LENGTH_SHORT).show();
                break;

            case R.id.close_button_expanded:
                Toast.makeText(FloatingViewService.this, "Expanded Close Button is Tapped", Toast.LENGTH_SHORT).show();
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
                break;

            case R.id.open_button:
                Toast.makeText(FloatingViewService.this, "Open Button is Tapped", Toast.LENGTH_SHORT).show();
                Intent openAppIntent = new Intent(FloatingViewService.this, MainActivity.class);
                openAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(openAppIntent);
                stopSelf();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {



        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN://Action_Down means the user starts touching the widget in order to move it so return true

                startXPos = parameters.x;//Gives the x position of the floating widget
                startYPos = parameters.y;//Gives the y position of the floating widget

                startTouchX = event.getRawX();//When user the starts touching the widget this startTouchX holds the x pos of the touching position value
                startTouchY = event.getRawY();//When user the starts touching the widget this startTouchX holds the y pos of the touching position value

                return true;

            case MotionEvent.ACTION_UP://Action_Up means the user has stops dragging the widget moved his/her finger up from the widget

                int startToEndXDifference = (int) ((event.getRawX()) - startTouchX);
                int startToEndYDifference = (int) ((event.getRawY()) - startTouchY);

                if(startToEndXDifference < 5 && startToEndYDifference < 5){
                    if(isWidgetCollapsed()){

                        collapsedView.setVisibility(View.GONE);
                        expandedView.setVisibility(View.VISIBLE);
                    }
                }
                return true;

            case MotionEvent.ACTION_MOVE://Action_Move means that  when the user is dragging the widget then at that time Action_Move is called
                    //Here we actually updating the position of the widget on the screent
                parameters.x = startXPos + (int)(event.getRawX() - startTouchX);
                parameters.y = startYPos + (int)(event.getRawY() - startTouchY);
                windowManager.updateViewLayout(floatingWidget, parameters);

                return true;

        }

        return false;
    }

    private boolean isWidgetCollapsed(){

        return collapsedView.getVisibility() == View.VISIBLE;
    }

    //@androidx.annotation.Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {//Here onDestroy() is used so our may not consume any memory space after the floating widget is closed
        super.onDestroy();

        if(floatingWidget != null){//if floating widget consumes memory space then if blocks executes

            windowManager.removeView(floatingWidget);//removes the widget from the memory
        }
    }
}
