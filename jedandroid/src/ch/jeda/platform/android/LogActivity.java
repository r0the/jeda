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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class LogActivity extends Activity {

    public static final String PARAM_BUTTON_TEXT = "Button";
    public static final String PARAM_LOG_CONTENT = "Log";
    public static final String PARAM_TITLE = "Title";
    private static final String LOG_TEXT_STATE = "ch.jeda.log.text";
    private static final String LOG_POS_STATE = "ch.jeda.log.pos";
    private Button button;
    private ScrollView scrollView;
    private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.textView = new TextView(this);
        setContentView(R.layout.log);

        this.button = (Button) this.findViewById(R.id.Button);
        this.button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        this.scrollView = (ScrollView) this.findViewById(R.id.ScrollView);
        this.textView = (TextView) this.findViewById(R.id.TextView);
        Intent intent = this.getIntent();
        if (intent != null) {
            this.setTitle(intent.getStringExtra(PARAM_TITLE));
            this.button.setText(intent.getStringExtra(PARAM_BUTTON_TEXT));
            this.textView.setText(intent.getStringExtra(PARAM_LOG_CONTENT));
            this.scrollView.fullScroll(View.FOCUS_DOWN);
        }
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
}
