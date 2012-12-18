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

import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import ch.jeda.Message;

class LogView extends DialogView {

    private final ScrollView scrollView;
    private final TextView textView;

    LogView(ViewManager manager) {
        super(manager);
        this.setTitle(Message.translate(Message.LOG_TITLE));

        this.textView = new TextView(this.getContext());
        this.textView.setLayoutParams(createFillLayout());

        this.scrollView = new ScrollView(this.getContext());
        this.scrollView.addView(this.textView);
        this.addContent(this.scrollView);

        Button button = this.addButton(Message.translate(Message.LOG_BUTTON));
        button.setOnClickListener(new OnClickListenerImp(this));
    }

    void log(String text) {
        this.textView.append(text);
        this.scrollView.fullScroll(View.FOCUS_DOWN);
    }

    private static class OnClickListenerImp implements OnClickListener {

        private final LogView view;

        public OnClickListenerImp(LogView view) {
            this.view = view;
        }

        @Override
        public void onClick(View view) {
            this.view.cancel();
        }
    }
}