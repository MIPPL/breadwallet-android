package com.swyftwallet.presenter.activities.intro;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.swyftwallet.R;
import com.swyftwallet.ui.recovery.RecoveryKeyActivity;
import com.swyftwallet.presenter.activities.util.BRActivity;
import com.swyftwallet.tools.animation.UiUtils;

public class RecoverActivity extends BRActivity {
    private Button nextButton;
    public static boolean appVisible = false;
    private static RecoverActivity app;

    public static RecoverActivity getApp() {
        return app;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_recover);

        nextButton = findViewById(R.id.send_button);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UiUtils.isClickAllowed()) return;
                Intent intent = new Intent(RecoverActivity.this, RecoveryKeyActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        appVisible = true;
        app = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        appVisible = false;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return checkOverlayAndDispatchTouchEvent(event);
    }
}
