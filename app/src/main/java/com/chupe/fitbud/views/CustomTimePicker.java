package com.chupe.fitbud.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chupe.fitbud.R;

import java.util.ArrayList;

public class CustomTimePicker extends ListView {
    // views
    private ListView listView;
    private ArrayAdapter<Integer> adapter;
    // privates
    private Integer firstItem;
    private IntegerLooper looper;

    // Constructors
    public CustomTimePicker(Context context) {
        super(context);
        init(context, null);
    }
    public CustomTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    public CustomTimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    // Overridden methods
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    // Public methods
    public int getCurrent() {
        return adapter.getItem(getCenterItem(listView));
    }
    public void setRange(int min, int max) {
        looper = new IntegerLooper(min, max);
    }
    // Returns the distance between a and b
    public static int getDistance(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        int leastDistance = 0;
        if (a > b) {
            leastDistance = a-b;
        }
        if (b > a) {
            leastDistance = b-a;
        }
        return  leastDistance;
    }
    public void setCurrentByValue(int value) {
        // TODO complete function

    }
    public void setCurrentByPosition(int newPosition) {
        // TODO complete function ^ set current position by position
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        // Prepare the looper before we use it
        TypedArray a = context.getTheme().obtainStyledAttributes(
            attrs,
            R.styleable.CustomTimePicker,
            0, 0);
        try {
            int min = a.getInteger(R.styleable.CustomTimePicker_min, 0);
            int max = a.getInteger(R.styleable.CustomTimePicker_max, 0);
            looper = new IntegerLooper(min, max);
        } finally {
            a.recycle();
        }

        // Reference our list view
        listView = this;

        setDividerHeight(0);

        ArrayList<Integer> arrayList = new ArrayList<>();

        boolean REE = true;
        if (REE)
            looper.sub(6);

        for (int i = 0; i < 10; i++) {
            arrayList.add(looper.getValue());
            looper.add(1);
        }

        adapter = new ArrayAdapter<>(
            context,
            android.R.layout.simple_list_item_1,
            arrayList
        );

        setAdapter(adapter);
        setSelection(adapter.getCount()/2);

        firstItem = adapter.getItem(getFirstVisiblePosition());

        centerListOnChild(listView);
        centerListOnChild(listView);

        super.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case RecyclerView.SCROLL_STATE_IDLE: {
                        centerListOnChild(listView);
                    }
                    case RecyclerView.SCROLL_STATE_DRAGGING: {

                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem /* this is the position in the list */, int visibleItemCount, int totalItemCount) {
                updateListItems(listView, adapter, firstVisibleItem, visibleItemCount, totalItemCount, looper);
                listFadeEffect(listView);
            }
        });

        listView.scrollListBy(listView.getHeight());
    }
    // Center on closest child view
    private void centerListOnChild(@NonNull ListView list) {
        int listMiddle = list.getHeight() / 2;
        int scrollBy = list.getHeight();
        for (int i = 0; i < list.getChildCount(); ++i) {
            int childMiddle = list.getChildAt(i).getTop() + list.getChildAt(i).getHeight() / 2;
            int tmp = getDistance(childMiddle, listMiddle);
            if (scrollBy > tmp) {
                if (childMiddle > listMiddle)
                    scrollBy = -tmp;
                else
                    scrollBy = tmp;
            }
        }
        scrollBy = -scrollBy;
        list.smoothScrollBy(scrollBy, 500);
    }
    // Give it a list, and it will return the position of the item
    // closest to the center
    private int getCenterItem(@NonNull ListView list) {
        int listMiddle = list.getHeight() / 2;
        int scrollBy = list.getHeight();
        int ret = 0;
        int firstVisibleItemPosition = list.getFirstVisiblePosition();
        for (int i = 0; i < list.getChildCount(); ++i) {
            int childMiddle = list.getChildAt(i).getTop() + list.getChildAt(i).getHeight() / 2;
            int tmp = getDistance(childMiddle, listMiddle);
            if (scrollBy > tmp) {
                scrollBy = tmp;
                ret = i;
            }
        }
        return ret+firstVisibleItemPosition;
    }
    // Makes a fade effect on elements not centered
    private void listFadeEffect(ListView list) {
        for (int i = 0; i < list.getChildCount(); ++i) {
            int listMiddle = list.getHeight() / 2;
            int childMiddle = list.getChildAt(i).getTop() + list.getChildAt(i).getHeight() / 2;
            int distance = getDistance(listMiddle, childMiddle);
            float alpha = (float) distance / (list.getHeight() / 2);
            alpha = 1 - alpha;
            list.getChildAt(i).setAlpha(alpha);
        }
    }
    // TODO fix how adapter is passed, use c++ style templates if possible
    // This function does stuff, don't bother
    private void updateListItems(ListView list, ArrayAdapter<Integer> adapter, int firstVisibleItem, int visibleItemCount, int totalItemCount, IntegerLooper looper) {
        Integer tmp = adapter.getItem(firstVisibleItem);
        if (!firstItem.equals(tmp)) { // first visible item has changed
            firstItem = tmp;

            // Bottom check
            if (list.getLastVisiblePosition()+1 == adapter.getCount()) { // need to insert an element
                looper.setValue(adapter.getItem(list.getLastVisiblePosition()));
                looper.add(1);
                adapter.insert(looper.getValue(), adapter.getCount());
            }

            // Top check
            if (firstVisibleItem == 1 || firstVisibleItem == 0) {
                // save index and top position
                int index = list.getFirstVisiblePosition(); //This changed
                View ve = list.getChildAt(0);
                int top = (ve == null) ? 0 : ve.getTop(); //this changed

                // notify dataset changed or re-assign adapter here
                int currPos = list.getScrollY();
                int i = adapter.getItem(0);

                looper.setValue(i);
                looper.sub(1);
                adapter.insert(looper.getValue(), 0);

                // restore the position of listview
                index +=1;
                list.setSelectionFromTop(index, top);
            }
//                    list.setSelection(currPosition);
        }

        // check for hidden and remove

        int hidden = totalItemCount - visibleItemCount;

        int topLimit = 20;
        int botLimit = 20;
        if (firstVisibleItem > 20)
            for (int i = 0; i<topLimit; i++)
                adapter.remove(adapter.getItem(0));

        int last = list.getLastVisiblePosition();
        if (totalItemCount - last > 20)
            for (int i = last+1; i<totalItemCount; ++i)
                adapter.remove(adapter.getItem(adapter.getCount()-1));
    }
}
