/*
 * Copyright (C) 2014 by Stefan Rothe
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

import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import ch.jeda.event.EventType;
import ch.jeda.event.KeyEvent;

class JedaInputConnection extends BaseInputConnection {

    private final Main main;
    private final SpannableStringBuilder text;

    JedaInputConnection(final View targetView, final boolean fullEditor) {
        super(targetView, fullEditor);
        this.main = (Main) targetView.getContext();
        this.text = new SpannableStringBuilder();
    }

    @Override
    public Editable getEditable() {
        return this.text;
    }

    @Override
    public boolean commitText(final CharSequence text, final int newCursorPosition) {
        for (int i = 0; i < text.length(); ++i) {
            main.addEvent(new KeyEvent(this, EventType.KEY_TYPED, text.charAt(i)));
        }

        return super.commitText(text, newCursorPosition);
    }
}
