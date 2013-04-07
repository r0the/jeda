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

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * A view with a button panel at the bottom.
 */
abstract class DialogView extends BaseView {

    private final LinearLayout buttonPanel;
    private final OnClickListener onClickListener;

    DialogView(final ViewManager manager) {
        super(manager);
        this.buttonPanel = new LinearLayout(this.getContext());
        this.buttonPanel.setId(1);
        this.buttonPanel.setOrientation(LinearLayout.HORIZONTAL);
        this.buttonPanel.setGravity(Gravity.CENTER);
        final RelativeLayout.LayoutParams buttonPanelParams =
                new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        buttonPanelParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        this.buttonPanel.setLayoutParams(buttonPanelParams);
        this.addView(this.buttonPanel);
        this.onClickListener = new OnClickListenerImp(this);
    }

    protected Button addButton(final String text) {
        final Button result = new Button(this.buttonPanel.getContext());
        result.setText(text);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1f;
        result.setLayoutParams(layoutParams);
        this.buttonPanel.addView(result);
        result.setOnClickListener(this.onClickListener);
        return result;
    }

    protected void addContent(final View content) {
        final RelativeLayout.LayoutParams contentParams =
                new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.FILL_PARENT);
        contentParams.addRule(RelativeLayout.ABOVE, this.buttonPanel.getId());
        content.setLayoutParams(contentParams);
        this.addView(content);
    }

    protected abstract void onButtonClicked(Button button);

    private static class OnClickListenerImp implements OnClickListener {

        private final DialogView view;

        public OnClickListenerImp(final DialogView view) {
            this.view = view;
        }

        @Override
        public void onClick(final View view) {
            this.view.onButtonClicked((Button) view);
        }
    }
}
