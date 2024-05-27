package com.amar.photo.touch_listener;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import com.amar.photo.utils.TransformUtil;

import java.io.Serializable;


public class MultiTouchListener implements OnTouchListener {

    private static final int INVALID_POINTER_ID = -1;
    public boolean isRotateEnabled = true;
    public boolean isTranslateEnabled = true;
    public boolean isScaleEnabled = true;
    public float minimumScale = 0.2f;
    public float maximumScale = 10.0f;
    private int mActivePointerId = INVALID_POINTER_ID;
    private float mPrevX;
    private float mPrevY;
    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector gestureDetector;
    /*OnDoubleTapListener onDoubleTapListener;
    CollageViewButtonInfo collageViewButtonInfo;*/
    Context context;
    View view;

    public MultiTouchListener(Context context, View view) {
        this.context = context;
        mScaleGestureDetector = new ScaleGestureDetector(new ScaleGestureListener());
        gestureDetector = new GestureDetector(context, new GestureListener());
        /*this.onDoubleTapListener = onDoubleTapListener;
        if (context instanceof BlenderEditorLandscape) {
            collageViewButtonInfo = (CollageViewButtonInfo) context;
        }*/
        this.view = view;
        this.isRotateEnabled = true;
        this.isTranslateEnabled = true;
        this.isScaleEnabled = true;
        this.minimumScale = 0.1f;
        this.maximumScale = 10.0f;
        this.mActivePointerId = INVALID_POINTER_ID;
    }

    public MultiTouchListener() {
    }

    public void resetImageview() {
        mActivePointerId = INVALID_POINTER_ID;
        mScaleGestureDetector = new ScaleGestureDetector();
        mScaleGestureDetector.reset();
    }

    private static float adjustAngle(float degrees) {
        if (degrees > 180.0f) {
            degrees -= 360.0f;
        } else if (degrees < -180.0f) {
            degrees += 360.0f;
        }

        return degrees;
    }

    private static void adjustTranslation(View view, float deltaX, float deltaY) {
        float[] deltaVector = {deltaX, deltaY};
        view.getMatrix().mapVectors(deltaVector);
        view.setTranslationX(view.getTranslationX() + deltaVector[0]);
        view.setTranslationY(view.getTranslationY() + deltaVector[1]);
    }

    private static void move(View view, TransformInfo info) {
        computeRenderOffset(view, info.pivotX, info.pivotY);
        adjustTranslation(view, info.deltaX, info.deltaY);

        // Assume that scaling still maintains aspect ratio.
        float scale = view.getScaleX() * info.deltaScale;
        scale = Math.max(info.minimumScale, Math.min(info.maximumScale, scale));
        view.setScaleX(scale);
        view.setScaleY(scale);

        float rotation = adjustAngle(view.getRotation() + info.deltaAngle);
        view.setRotation(rotation);
    }

    private static void computeRenderOffset(View view, float pivotX, float pivotY) {
        if (view.getPivotX() == pivotX && view.getPivotY() == pivotY) {
            return;
        }

        float[] prevPoint = {0.0f, 0.0f};
        view.getMatrix().mapPoints(prevPoint);

        view.setPivotX(pivotX);
        view.setPivotY(pivotY);

        float[] currPoint = {0.0f, 0.0f};
        view.getMatrix().mapPoints(currPoint);

        float offsetX = currPoint[0] - prevPoint[0];
        float offsetY = currPoint[1] - prevPoint[1];

        view.setTranslationX(view.getTranslationX() - offsetX);
        view.setTranslationY(view.getTranslationY() - offsetY);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(view, event);
        gestureDetector.onTouchEvent(event);
        if (!isTranslateEnabled) {
            return true;
        }
     /*   if (context instanceof BlenderEditorLandscape) {
            collageViewButtonInfo.getViewOfColalgeBtn(view);
        }
        if (view.getId() == R.id.gallery_image_left) {
            DoubleFramesEditActivity.isClickedcollageViewOne = true;
            DoubleFramesEditActivity.isClickedcollageViewTwo = false;
            DoubleFramesEditActivity.isImageOneSelected = true;
            DoubleFramesEditActivity.isImageTwoSelected = false;
        } else if (view.getId() == R.id.gallery_image_right) {
            DoubleFramesEditActivity.isClickedcollageViewTwo = true;
            DoubleFramesEditActivity.isClickedcollageViewOne = false;
            DoubleFramesEditActivity.isImageTwoSelected = true;
            DoubleFramesEditActivity.isImageOneSelected = false;
        }*/

        int action = event.getAction();
        switch (action & event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                mPrevX = event.getX();
                mPrevY = event.getY();

                // Save the ID of this pointer.
                mActivePointerId = event.getPointerId(0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position.
                int pointerIndex = event.findPointerIndex(mActivePointerId);
                if (pointerIndex != -1) {
                    float currX = event.getX(pointerIndex);
                    float currY = event.getY(pointerIndex);

                    // Only move if the ScaleGestureDetector isn't processing a
                    // gesture.
                    if (!mScaleGestureDetector.isInProgress()) {
                        adjustTranslation(view, currX - mPrevX, currY - mPrevY);
                    }
                }

                break;
            }

            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = INVALID_POINTER_ID;

                break;

            case MotionEvent.ACTION_UP:
                mActivePointerId = INVALID_POINTER_ID;
                updateTransformUtil(view);
                Log.d("OnTouch", "onTouch: OnTouchHandled");
                break;

            case MotionEvent.ACTION_POINTER_UP: {
                // Extract the index of the pointer that left the touch sensor.
                int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mPrevX = event.getX(newPointerIndex);
                    mPrevY = event.getY(newPointerIndex);
                    mActivePointerId = event.getPointerId(newPointerIndex);
                }

                break;
            }
        }

        return true;
    }

    private class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        private float mPivotX;
        private float mPivotY;
        private Vector2D mPrevSpanVector = new Vector2D();

        @Override
        public boolean onScaleBegin(View view, ScaleGestureDetector detector) {
            mPivotX = detector.getFocusX();
            mPivotY = detector.getFocusY();
            mPrevSpanVector.set(detector.getCurrentSpanVector());
            return true;
        }

        @Override
        public boolean onScale(View view, ScaleGestureDetector detector) {
            TransformInfo info = new TransformInfo();
            info.deltaScale = isScaleEnabled ? detector.getScaleFactor() : 1.0f;
            info.deltaAngle = isRotateEnabled ? Vector2D.getAngle(mPrevSpanVector, detector.getCurrentSpanVector()) : 0.0f;
            info.deltaX = isTranslateEnabled ? detector.getFocusX() - mPivotX : 0.0f;
            info.deltaY = isTranslateEnabled ? detector.getFocusY() - mPivotY : 0.0f;
            info.pivotX = mPivotX;
            info.pivotY = mPivotY;
            info.minimumScale = minimumScale;
            info.maximumScale = maximumScale;

            move(view, info);
            return false;
        }
    }

    public class TransformInfo implements Serializable {

        public float deltaX;
        public float deltaY;
        public float deltaScale;
        public float deltaAngle;
        public float pivotX;
        public float pivotY;
        public float minimumScale;
        public float maximumScale;

    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            /*if (onDoubleTapListener != null) {
                onDoubleTapListener.onDoubleTapListner(view);
            }*/
            return super.onDoubleTap(e);
        }
    }

    // Add method to update TransformUtil
    private void updateTransformUtil(View view) {
        TransformInfo info = saveTransformInfo(view);
        TransformUtil.INSTANCE.saveTransformInfo(info);
    }

    private TransformInfo saveTransformInfo(View view) {
        TransformInfo info = new TransformInfo();
        info.deltaX = view.getTranslationX();
        info.deltaY = view.getTranslationY();
        info.deltaScale = view.getScaleX();
        info.deltaAngle = view.getRotation();
        info.pivotX = view.getPivotX();
        info.pivotY = view.getPivotY();
        info.minimumScale = minimumScale;
        info.maximumScale = maximumScale;
        return info;
    }

    public void setTransformInfo(View view, TransformInfo info) {
        view.setTranslationX(info.deltaX);
        view.setTranslationY(info.deltaY);
        view.setScaleX(info.deltaScale);
        view.setScaleY(info.deltaScale);
        view.setRotation(info.deltaAngle);
        view.setPivotX(info.pivotX);
        view.setPivotY(info.pivotY);
    }



}