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
class DialogView extends BaseView {

    private final LinearLayout buttonPanel;

    DialogView(ViewManager manager) {
        super(manager);
        this.buttonPanel = new LinearLayout(this.getContext());
        this.buttonPanel.setId(1);
        this.buttonPanel.setOrientation(LinearLayout.HORIZONTAL);
        this.buttonPanel.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams buttonPanelParams =
                new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        buttonPanelParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        this.buttonPanel.setLayoutParams(buttonPanelParams);
        this.addView(this.buttonPanel);
    }

    protected Button addButton(String text) {
        Button result = new Button(this.buttonPanel.getContext());
        result.setText(text);
        result.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        this.buttonPanel.addView(result);
        return result;
    }

    protected void addContent(View content) {
        RelativeLayout.LayoutParams contentParams =
                new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.FILL_PARENT);
        contentParams.addRule(RelativeLayout.ABOVE, this.buttonPanel.getId());
        content.setLayoutParams(contentParams);
        this.addView(content);
    }
}