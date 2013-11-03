/*
 * Copyright (C) 2012 - 2013 by Stefan Rothe
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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import ch.jeda.LogLevel;

class LogFragment extends Fragment {

    private final StringBuilder log;
    private ScrollView scrollView;
    private TextView textView;

    public LogFragment() {
        this.log = new StringBuilder();
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        activity.setTitle("Jeda Log");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        this.textView = new TextView(this.getActivity());
        this.textView.setLayoutParams(new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.FILL_PARENT,
            ViewGroup.LayoutParams.FILL_PARENT));
        this.scrollView = new ScrollView(this.getActivity());
        this.scrollView.addView(this.textView);
        this.updateView();
        return this.scrollView;
    }

    void log(final LogLevel logLevel, final String message) {
        switch (logLevel) {
            case DEBUG:
                Log.d("Jeda", message);
                break;
            case ERROR:
                Log.e("Jeda", message);
                this.append(message);
                break;
            case INFO:
                System.out.println(message);
                this.append(message);
                break;
        }
    }

    void append(final String text) {
        this.log.append(text);
        if (this.isVisible()) {
            this.updateView();
        }
    }

    private void updateView() {
        this.textView.setText(this.log.toString());
        this.scrollView.fullScroll(View.FOCUS_DOWN);
    }
}
