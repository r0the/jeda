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

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectionActivity<T> extends android.app.ListActivity {

    static final String TITLE = "Title";
    static final String ITEMS = "Items";
    static final String SELECTED_INDEX = "SelectedIndex";
    private MyListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.listAdapter = new MyListAdapter(this);
        this.setListAdapter(this.listAdapter);
        Intent intent = this.getIntent();
        if (intent != null) {
            this.setTitle(intent.getStringExtra(TITLE));
            this.listAdapter.setItems(intent.getStringArrayListExtra(ITEMS));
        }
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        Intent data = new Intent();
        data.putExtra(SELECTED_INDEX, position);
        this.setResult(RESULT_OK, data);
        this.finish();
    }

    private static class MyListAdapter implements ListAdapter {

        private static final int TEXT_VIEW_ID = 1;
        private final Context context;
        private final List<String> items;
        private final Set<DataSetObserver> observers;

        MyListAdapter(Context context) {
            this.context = context;
            this.observers = new HashSet();
            this.items = new ArrayList();
        }

        void setItems(Collection<String> items) {
            this.items.clear();
            this.items.addAll(items);
            for (DataSetObserver observer : this.observers) {
                observer.onChanged();
            }
        }

        public boolean areAllItemsEnabled() {
            return true;
        }

        public boolean isEnabled(int position) {
            return true;
        }

        public void registerDataSetObserver(DataSetObserver observer) {
            this.observers.add(observer);
        }

        public void unregisterDataSetObserver(DataSetObserver observer) {
            this.observers.remove(observer);
        }

        public int getCount() {
            return this.items.size();
        }

        public Object getItem(int position) {
            return this.items.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public boolean hasStableIds() {
            return true;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View result = convertView;
            if (result == null) {
                result = this.createRowView();
            }

            ((TextView) result.findViewById(TEXT_VIEW_ID)).setText(this.items.get(position));
            return result;
        }

        public int getItemViewType(int position) {
            return 0;
        }

        public int getViewTypeCount() {
            return 1;
        }

        public boolean isEmpty() {
            return this.items.isEmpty();
        }

        private View createRowView() {
            LinearLayout result = new LinearLayout(this.context);
            result.setOrientation(LinearLayout.HORIZONTAL);
            TextView textView = new TextView(this.context);
            textView.setId(TEXT_VIEW_ID);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setHeight(50);
            textView.setTextSize(20);
            result.addView(textView);
            return result;
        }
    }
}
