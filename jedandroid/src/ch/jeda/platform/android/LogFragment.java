/*
 * Copyright (C) 2012 - 2015 by Stefan Rothe
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
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

class LogFragment extends Fragment {

    private final StringBuilder log;
    private ScrollView scrollView;
    private TextView textView;

    public LogFragment() {
        log = new StringBuilder();
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        activity.setTitle("Jeda Log");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        textView = new TextView(getActivity());
        textView.setLayoutParams(new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT));
        scrollView = new ScrollView(getActivity());
        scrollView.addView(textView);
        updateView();
        return scrollView;
    }

    void append(final String text) {
        log.append(text);
        if (isVisible()) {
            updateView();
        }
    }

    private void updateView() {
        textView.setText(log.toString());
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}
