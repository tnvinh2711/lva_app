package com.lva.shop.ui.customview;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lva.shop.R;

public class ValueSelectorMini extends LinearLayout {

    private int minValue = Integer.MIN_VALUE;
    private int maxValue = Integer.MAX_VALUE;

    private boolean plusButtonIsPressed = false;
    private boolean minusButtonIsPressed = false;
    private final long REPEAT_INTERVAL_MS = 100l;

    View rootView;
    TextView valueTextView;
    ImageView minusButton;
    ImageView plusButton;
    SetOnValueListener setOnValueListener;
    Handler handler = new Handler();

    public ValueSelectorMini(Context context) {
        super(context);
        init(context);
    }

    public ValueSelectorMini(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ValueSelectorMini(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     * Get the current minimum value that is allowed
     *
     * @return
     */
    public int getMinValue() {
        return minValue;
    }

    /**
     * Set the minimum value that will be allowed
     *
     * @param minValue
     */
    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    /**
     * Get the current maximum value that is allowed
     *
     * @return
     */
    public int getMaxValue() {
        return maxValue;
    }

    /**
     * Set the maximum value that will be allowed
     *
     * @param maxValue
     */
    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setOnValueListener(SetOnValueListener onValueListener) {
        this.setOnValueListener = onValueListener;
    }

    /**
     * Get the current value
     *
     * @return the current value
     */
    public int getValue() {
        return Integer.valueOf(valueTextView.getText().toString());
    }

    /**
     * Set the current value.  If the passed in value exceeds the current min or max, the value
     * will be set to the respective min/max.
     *
     * @param newValue new value
     */
    public void setValue(int newValue) {
        int value = newValue;
        if (newValue <= minValue) {
            minusButton.setColorFilter(getContext().getResources().getColor(R.color.color_text_gray));
            plusButton.setColorFilter(getContext().getResources().getColor(R.color.color_text_root));
            value = minValue;
        } else if (newValue >= maxValue) {
            minusButton.setColorFilter(getContext().getResources().getColor(R.color.color_text_root));
            plusButton.setColorFilter(getContext().getResources().getColor(R.color.color_text_gray));
            value = maxValue;
        } else {
            minusButton.setColorFilter(getContext().getResources().getColor(R.color.color_text_root));
            plusButton.setColorFilter(getContext().getResources().getColor(R.color.color_text_root));
        }

        valueTextView.setText(String.format("%02d", value));
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.value_selector_mini, this);
        valueTextView = rootView.findViewById(R.id.valueTextView);

        minusButton = rootView.findViewById(R.id.minusButton);
        plusButton = rootView.findViewById(R.id.plusButton);

        minusButton.setOnClickListener(v -> decrementValue());
        minusButton.setOnLongClickListener(
                arg0 -> {
                    minusButtonIsPressed = true;
                    handler.post(new AutoDecrementer());
                    return false;
                }
        );
        minusButton.setOnTouchListener((v, event) -> {
            if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
                minusButtonIsPressed = false;
            }
            return false;
        });

        plusButton.setOnClickListener(v -> incrementValue());
        plusButton.setOnLongClickListener(
                arg0 -> {
                    plusButtonIsPressed = true;
                    handler.post(new AutoIncrementer());
                    return false;
                }
        );

        plusButton.setOnTouchListener((v, event) -> {
            if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
                plusButtonIsPressed = false;
            }
            return false;
        });
    }

    private void incrementValue() {
        int currentVal = Integer.valueOf(valueTextView.getText().toString());
        if (currentVal < maxValue) {
            if (setOnValueListener != null) setOnValueListener.onChange(currentVal + 1);
            valueTextView.setText(String.format("%02d", currentVal + 1));
            if (currentVal + 1 < maxValue) {
                plusButton.setColorFilter(getContext().getResources().getColor(R.color.color_text_root));
                minusButton.setColorFilter(getContext().getResources().getColor(R.color.color_text_root));
            } else {
                plusButton.setColorFilter(getContext().getResources().getColor(R.color.color_text_gray));
                minusButton.setColorFilter(getContext().getResources().getColor(R.color.color_text_root));
            }
        } else {
            plusButton.setColorFilter(getContext().getResources().getColor(R.color.color_text_gray));
            minusButton.setColorFilter(getContext().getResources().getColor(R.color.color_text_root));
        }
    }

    private void decrementValue() {
        int currentVal = Integer.valueOf(valueTextView.getText().toString());
        if (currentVal > minValue) {
            if (setOnValueListener != null) setOnValueListener.onChange(currentVal - 1);
            valueTextView.setText(String.format("%02d", currentVal - 1));
            if (currentVal - 1 > minValue) {
                plusButton.setColorFilter(getContext().getResources().getColor(R.color.color_text_root));
                minusButton.setColorFilter(getContext().getResources().getColor(R.color.color_text_root));
            } else {
                plusButton.setColorFilter(getContext().getResources().getColor(R.color.color_text_root));
                minusButton.setColorFilter(getContext().getResources().getColor(R.color.color_text_gray));
            }
        } else {
            plusButton.setColorFilter(getContext().getResources().getColor(R.color.color_text_root));
            minusButton.setColorFilter(getContext().getResources().getColor(R.color.color_text_gray));
        }
    }

    private class AutoIncrementer implements Runnable {
        @Override
        public void run() {
            if (plusButtonIsPressed) {
                incrementValue();
                handler.postDelayed(new AutoIncrementer(), REPEAT_INTERVAL_MS);
            }
        }
    }

    private class AutoDecrementer implements Runnable {
        @Override
        public void run() {
            if (minusButtonIsPressed) {
                decrementValue();
                handler.postDelayed(new AutoDecrementer(), REPEAT_INTERVAL_MS);
            }
        }
    }

    public interface SetOnValueListener {
        void onChange(int value);
    }
}