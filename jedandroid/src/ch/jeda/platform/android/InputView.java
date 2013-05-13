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

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import ch.jeda.platform.InputRequest;

class InputView extends DialogView implements TextWatcher {

    private final Button acceptButton;
    private final Button cancelButton;
    private final EditText input;
    private final TextView label;
    private InputRequest request;

    public void beforeTextChanged(final CharSequence cs, final int start, final int count, final int after) {
    }

    public void onTextChanged(final CharSequence cs, final int start, final int count, final int after) {
    }

    public void afterTextChanged(final Editable editable) {
        this.validateInput();
    }

    InputView(final ViewManager manager) {
        super(manager);
        this.acceptButton = this.addButton("Ok");
        this.cancelButton = this.addButton("Cancel");
        final LinearLayout main = new LinearLayout(this.getContext());
        main.setOrientation(LinearLayout.VERTICAL);
        this.addContent(main);
        // This is required in order to show soft keyboard when input is focused.
        this.setFocusableInTouchMode(true);

        this.input = new EditText(this.getContext());
        this.input.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        this.label = new TextView(this.getContext());
        this.label.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        main.addView(this.label);
        main.addView(this.input);

        this.input.setImeOptions(EditorInfo.IME_ACTION_DONE);
        this.input.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        this.input.addTextChangedListener(this);

        final InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this.input, InputMethodManager.SHOW_IMPLICIT);
    }

    void setInputRequest(final InputRequest request) {
        this.request = request;
        this.setTitle(request.getTitle());
        this.label.setText(request.getMessage());
        this.input.setText("");
        this.input.requestFocus();
        this.validateInput();
    }

    @Override
    protected void onAccept() {
        this.request.setResult(this.request.getInputType().parse(this.input.getText().toString()));
    }

    @Override
    protected void onCancel() {
        this.request.cancelRequest();
    }

    @Override
    protected void onButtonClicked(final Button button) {
        if (button == this.acceptButton) {
            this.accept();
        }
        else if (button == this.cancelButton) {
            this.cancel();
        }
    }

    private void validateInput() {
        this.acceptButton.setEnabled(this.request.getInputType().validate(this.input.getText().toString()));
    }
}
