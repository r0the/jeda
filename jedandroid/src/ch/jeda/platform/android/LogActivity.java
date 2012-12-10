/*
 * Copyright (C) 2012 by Stefan Rothe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY); without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.jeda.platform.android;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import ch.jeda.Message;

public class LogActivity extends Activity {

    static final String PARAM_BUTTON_TEXT = "Button";
    static final String PARAM_LOG_CONTENT = "Log";
    static final String PARAM_TITLE = "Title";
    private static final String LOG_TEXT_STATE = "ch.jeda.log.text";
    private static final String LOG_POS_STATE = "ch.jeda.log.pos";
    private Button button;
    private ScrollView scrollView;
    private TextView textView;
    private BroadcastReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setTitle(Message.translate(Message.LOG_TITLE));

        this.textView = new TextView(this);
        this.textView.setLayoutParams(createFillLayout());

        this.scrollView = new ScrollView(this);
        this.scrollView.addView(this.textView);

        DialogLayout layout = new DialogLayout(this, this.scrollView);
        this.button = layout.addButton(Message.translate(Message.LOG_BUTTON));
        this.button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        this.updateUI(this.getIntent());
        this.receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                updateUI(intent);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(this.receiver, new IntentFilter(this.getClass().getName()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(this.receiver);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.textView.setText(savedInstanceState.getCharSequence(LOG_TEXT_STATE));
        this.scrollView.scrollTo(savedInstanceState.getInt(LOG_POS_STATE), 0);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putCharSequence(LOG_TEXT_STATE, this.textView.getText());
        outState.putInt(LOG_POS_STATE, this.scrollView.getScrollY());
        super.onSaveInstanceState(outState);
    }

    private void updateUI(Intent intent) {
        if (intent != null) {
            this.textView.append(intent.getStringExtra(PARAM_LOG_CONTENT));
            this.scrollView.fullScroll(View.FOCUS_DOWN);
        }
    }

    private static ViewGroup.LayoutParams createFillLayout() {
        return new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
    }
}
