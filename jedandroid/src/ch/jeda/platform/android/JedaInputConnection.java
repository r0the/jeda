/*
 * Copyright (C) 2014 - 2015 by Stefan Rothe
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
import android.util.Log;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.CompletionInfo;

class JedaInputConnection extends BaseInputConnection {

    private final SpannableStringBuilder text;
    private SurfaceFragment topView;

    JedaInputConnection(final View targetView, final boolean fullEditor) {
        super(targetView, fullEditor);
        text = new SpannableStringBuilder();
    }

    @Override
    public Editable getEditable() {
        return text;
    }

    @Override
    public boolean commitCompletion(final CompletionInfo text) {
        Log.i("Jeda", "Jeda IK: commitCompletion");
        convertToKeystrokes(text.getText());
        return super.commitCompletion(text);
    }

    @Override
    public boolean deleteSurroundingText(int leftLength, int rightLength) {
        Log.i("Jeda", "Jeda IK: deleteSurroundingText");
        return super.deleteSurroundingText(leftLength, rightLength);
    }

    @Override
    public boolean sendKeyEvent(android.view.KeyEvent event) {
        Log.i("Jeda", "Sending key event:" + event.getKeyCode() + ", action=" + event.getAction());

        topView.sendKeyEvent(event.getKeyCode());
        return super.sendKeyEvent(event);
    }

    @Override
    public boolean commitText(final CharSequence text, final int newCursorPosition) {
        convertToKeystrokes(text);
        return super.commitText(text, newCursorPosition);
    }

    public void setTopView(final SurfaceFragment topView) {
        this.topView = topView;
        Log.i("Jeda", "Setting top view");
    }

    private void convertToKeystrokes(final CharSequence text) {
        if (topView == null) {
            return;
        }

        for (int i = 0; i < text.length(); ++i) {
            Log.i("Jeda", "Posting event");
            topView.sendKeyTypedEvent(text.charAt(i));
        }
    }
}
