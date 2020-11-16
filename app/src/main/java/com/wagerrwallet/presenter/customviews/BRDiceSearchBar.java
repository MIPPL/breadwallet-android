package com.wagerrwallet.presenter.customviews;

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

import com.wagerrwallet.R;
import com.wagerrwallet.presenter.activities.DiceActivity;
import com.wagerrwallet.tools.manager.DiceManager;
import com.wagerrwallet.tools.manager.DiceManager;
import com.wagerrwallet.tools.threads.executor.BRExecutor;

/**
 * BreadWallet
 * <p/>
 * Created by MIP on 11/14/20.
 * Copyright (c) 2020 Wagerr LTD
 * <p>
 *
 * (c) Wagerr Betting platform 2020
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
public class BRDiceSearchBar extends android.support.v7.widget.Toolbar {

    private static final String TAG = BRDiceSearchBar.class.getName();

    private EditText searchEdit;
    private BRButton cancelButton;
    //    private LinearLayout filterButtonsLayout;
    private BRButton equalFilter;
    private BRButton notEqualFilter;
    private BRButton overFilter;
    private BRButton underButton;
    private BRButton evenFilter;
    private BRButton oddButton;

    private DiceActivity breadActivity;

    public boolean[] filterSwitches = new boolean[6];

    public BRDiceSearchBar(Context context) {
        super(context);
        init();
    }

    public BRDiceSearchBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BRDiceSearchBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.dicesearch_bar, this);
        breadActivity = (DiceActivity) getContext();
        searchEdit = (EditText) findViewById(R.id.search_edit);
        cancelButton = (BRButton) findViewById(R.id.cancel_button);
        equalFilter = (BRButton) findViewById(R.id.equal_filter);
        notEqualFilter = (BRButton) findViewById(R.id.notequal_filter);
        overFilter = (BRButton) findViewById(R.id.over_filter);
        underButton = (BRButton) findViewById(R.id.under_filter);
        evenFilter = (BRButton) findViewById(R.id.even_filter);
        oddButton = (BRButton) findViewById(R.id.odds_filter);

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
                DiceManager.getInstance().updateDiceList(breadActivity);
            }
        });

    }

    private void updateFilterButtonsUI(boolean[] switches) {
        equalFilter.setType(switches[0] ? 3 : 2);
        notEqualFilter.setType(switches[1] ? 3 : 2);
        overFilter.setType(switches[2] ? 3 : 2);
        underButton.setType(switches[3] ? 3 : 2);
        evenFilter.setType(switches[4] ? 3 : 2);
        oddButton.setType(switches[5] ? 3 : 2);

        if (DiceManager.getInstance().adapter != null)
            DiceManager.getInstance().adapter.filterBy(searchEdit.getText().toString(), filterSwitches);
    }

    private void setListeners() {
        searchEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                /*if (!hasFocus) {
                    if (breadActivity.barFlipper != null) {
                        breadActivity.barFlipper.setDisplayedChild(0);
                        clearSwitches();
                    }
                }*/
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
                if (DiceManager.getInstance().adapter != null)
                    DiceManager.getInstance().adapter.filterBy(s.toString(), filterSwitches);
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

        equalFilter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                filterSwitches[0] = !filterSwitches[0];
                updateFilterButtonsUI(filterSwitches);

            }
        });

        notEqualFilter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                filterSwitches[1] = !filterSwitches[1];
                updateFilterButtonsUI(filterSwitches);
            }
        });

        overFilter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                filterSwitches[2] = !filterSwitches[2];
                updateFilterButtonsUI(filterSwitches);
            }
        });

        underButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                filterSwitches[3] = !filterSwitches[3];
                updateFilterButtonsUI(filterSwitches);

            }
        });

        evenFilter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                filterSwitches[4] = !filterSwitches[4];
                updateFilterButtonsUI(filterSwitches);
            }
        });

        oddButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                filterSwitches[5] = !filterSwitches[5];
                updateFilterButtonsUI(filterSwitches);
            }
        });
    }

    public void clearSwitches() {
        filterSwitches[0] = false;
        filterSwitches[1] = false;
        filterSwitches[2] = false;
        filterSwitches[3] = false;
        filterSwitches[4] = false;
        filterSwitches[5] = false;
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
            if (DiceManager.getInstance().adapter != null)
                DiceManager.getInstance().adapter.updateData();

        } else {
            keyboard.hideSoftInputFromWindow(searchEdit.getWindowToken(), 0);
            clearSwitches();
            updateFilterButtonsUI(filterSwitches);
            if (DiceManager.getInstance().adapter != null) {
                DiceManager.getInstance().adapter.resetFilter();
            }
        }
        breadActivity.isSearchBarVisible = b;
    }


}