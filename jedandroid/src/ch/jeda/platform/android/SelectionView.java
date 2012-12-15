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
import android.database.DataSetObserver;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import ch.jeda.platform.SelectionRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class SelectionView extends DialogView {

    static final String TITLE = "Title";
    static final String ITEMS = "Items";
    static final String SELECTED_INDEX = "SelectedIndex";
    private final ListAdapterImp listAdapter;
    private final ListView listView;
    private SelectionRequest request;
    private int selectedItemPosition;

    SelectionView(ViewManager manager) {
        super(manager);
        this.listView = new ListView(this.getContext());
        this.addContent(this.listView);
        this.listAdapter = new ListAdapterImp(this.getContext());
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new OnItemClickListenerImp(this));
    }

    void setSelectionRequest(SelectionRequest request) {
        this.request = request;
        this.setTitle(request.getTitle());
        this.listAdapter.setItems(request.getDisplayItems());
    }

    @Override
    protected void onAccept() {
        this.request.setSelectedIndex(selectedItemPosition);
    }

    private static class ListAdapterImp implements ListAdapter {

        private static final int TEXT_VIEW_ID = 1;
        private final Context context;
        private final List<String> items;
        private final Set<DataSetObserver> observers;

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEnabled(int position) {
            return true;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
            this.observers.add(observer);
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            this.observers.remove(observer);
        }

        @Override
        public int getCount() {
            return this.items.size();
        }

        @Override
        public Object getItem(int position) {
            return this.items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View result = convertView;
            if (result == null) {
                result = this.createRowView();
            }

            ((TextView) result.findViewById(TEXT_VIEW_ID)).setText(this.items.get(position));
            return result;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return this.items.isEmpty();
        }

        ListAdapterImp(Context context) {
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

    private static class OnItemClickListenerImp implements OnItemClickListener {

        private final SelectionView selectionView;

        public OnItemClickListenerImp(SelectionView selectionView) {
            this.selectionView = selectionView;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            this.selectionView.selectedItemPosition = position;
            this.selectionView.accept();
        }
    }
}
