package com.biblepaywallet.presenter.customviews;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.biblepaywallet.R;
import com.biblepaywallet.presenter.activities.TitheActivity;
import com.biblepaywallet.presenter.activities.WalletActivity;
import com.biblepaywallet.tools.manager.TitheManager;
import com.biblepaywallet.tools.threads.executor.BRExecutor;

/**
 * BreadWallet
 * <p/>
 * Created by Mihail Gutan on <mihail@breadwallet.com> 5/8/17.
 * Copyright (c) 2017 breadwallet LLC
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
public class BRTitheSearchBar extends android.support.v7.widget.Toolbar {

    private static final String TAG = BRTitheSearchBar.class.getName();

    private EditText searchEdit;
    //    private LinearLayout filterButtonsLayout;
    private BRButton cancelButton;

    private TitheActivity breadActivity;

    public boolean[] filterSwitches = new boolean[4];

    public BRTitheSearchBar(Context context) {
        super(context);
        init();
    }

    public BRTitheSearchBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BRTitheSearchBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        inflate(getContext(), R.layout.searchtithe_bar, this);
        breadActivity = (TitheActivity) getContext();
        searchEdit = (EditText) findViewById(R.id.search_edit);
        cancelButton = (BRButton) findViewById(R.id.cancel_button);

        clearSwitches();
        setListeners();


        searchEdit.requestFocus();
        searchEdit.postDelayed(new Runnable() {

            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager)
                        getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(searchEdit, 0);
            }
        }, 200); //use 300 to make it run when coming back from lock screen

        BRExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                TitheManager.getInstance().updateTitheList(breadActivity);
            }
        });

    }

    private void updateFilterButtonsUI(boolean[] switches) {
        if (TitheManager.getInstance().adapter != null)
            TitheManager.getInstance().adapter.filterBy(searchEdit.getText().toString(), filterSwitches);
    }

    private void setListeners() {
        searchEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (breadActivity.barFlipper != null) {
                        breadActivity.barFlipper.setDisplayedChild(0);
                        clearSwitches();
                    }
                }
            }
        });

        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEdit.setText("");
                breadActivity.resetFlipper();
                clearSwitches();
                onShow(false);
            }
        });

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TitheManager.getInstance().adapter != null)
                    TitheManager.getInstance().adapter.filterBy(s.toString(), filterSwitches);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchEdit.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    onShow(false);
                    return true;
                }
                return false;
            }
        });

    }

    public void clearSwitches() {
        filterSwitches[0] = false;
        filterSwitches[1] = false;
        filterSwitches[2] = false;
        filterSwitches[3] = false;
    }

    public void onShow(boolean b) {

        final InputMethodManager keyboard = (InputMethodManager)
                getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (b) {
            clearSwitches();
            updateFilterButtonsUI(filterSwitches);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    searchEdit.requestFocus();
                    keyboard.showSoftInput(searchEdit, 0);
                }
            }, 400);
            if (TitheManager.getInstance().adapter != null)
                TitheManager.getInstance().adapter.updateData();

        } else {
            keyboard.hideSoftInputFromWindow(searchEdit.getWindowToken(), 0);
            clearSwitches();
            updateFilterButtonsUI(filterSwitches);
            if (TitheManager.getInstance().adapter != null) {
                TitheManager.getInstance().adapter.resetFilter();
            }
        }
    }


}