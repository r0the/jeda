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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import ch.jeda.platform.InputRequest;

class InputDialogFragment extends DialogFragment implements TextWatcher, DialogInterface.OnClickListener {

    private final InputRequest request;
    private EditText input;
    private Dialog dialog;

    InputDialogFragment(final InputRequest request) {
        this.request = request;
    }

    @Override
    public void afterTextChanged(final Editable editable) {
        validateInput();
    }

    @Override
    public void beforeTextChanged(final CharSequence cs, final int start, final int count, final int after) {
    }

    @Override
    public void onClick(final DialogInterface dialogInterface, final int which) {
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                request.setResult(request.getInputType().parse(input.getText().toString()));
                dialogInterface.dismiss();
                break;
            case Dialog.BUTTON_NEGATIVE:
                request.cancelRequest();
                dialogInterface.cancel();
                break;
        }
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(request.getTitle());
        builder.setMessage(request.getMessage());
        input = new EditText(getActivity());
        // This is required in order to show soft keyboard when input is focused.
        input.setFocusableInTouchMode(true);
        input.setImeOptions(EditorInfo.IME_ACTION_DONE);
        input.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        input.addTextChangedListener(this);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
        builder.setView(input);
        builder.setPositiveButton("OK", this);
        builder.setNegativeButton("Abbrechen", this);
        return builder.create();
    }

    @Override
    public void onTextChanged(final CharSequence cs, final int start, final int count, final int after) {
    }

    private void validateInput() {
//        acceptButton.setEnabled(request.getInputType().validate(input.getText().toString()));
    }
}
