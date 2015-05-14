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

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
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

class SelectionDialogFragment extends DialogFragment {

    private final SelectionRequest request;
    private ListAdapterImp listAdapter;
    private int selectedItemPosition;

    SelectionDialogFragment(final SelectionRequest request) {
        this.request = request;
        setShowsDialog(false);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        final ListView result = new ListView(getActivity());
        listAdapter = new ListAdapterImp(getActivity());
        listAdapter.setItems(request.getDisplayItems());
        result.setAdapter(listAdapter);
        result.setOnItemClickListener(new OnItemClickListenerImp(this));
        selectedItemPosition = -1;
        return result;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        request.setResult(selectedItemPosition);
    }

    private static class ListAdapterImp implements ListAdapter {

        private static final int TEXT_VIEW_ID = 1;
        private final Context context;
        private final List<String> items;
        private final Set<DataSetObserver> observers;

        ListAdapterImp(final Context context) {
            this.context = context;
            observers = new HashSet();
            items = new ArrayList();
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEnabled(final int position) {
            return true;
        }

        @Override
        public void registerDataSetObserver(final DataSetObserver observer) {
            observers.add(observer);
        }

        @Override
        public void unregisterDataSetObserver(final DataSetObserver observer) {
            observers.remove(observer);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(final int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(final int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            View result = convertView;
            if (result == null) {
                result = createRowView();
            }

            ((TextView) result.findViewById(TEXT_VIEW_ID)).setText(items.get(position));
            return result;
        }

        @Override
        public int getItemViewType(final int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return items.isEmpty();
        }

        void setItems(final Collection<String> items) {
            items.clear();
            items.addAll(items);
            for (final DataSetObserver observer : observers) {
                observer.onChanged();
            }
        }

        private View createRowView() {
            final LinearLayout result = new LinearLayout(context);
            result.setOrientation(LinearLayout.HORIZONTAL);
            final TextView textView = new TextView(context);
            textView.setId(TEXT_VIEW_ID);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setHeight(50);
            textView.setTextSize(20);
            result.addView(textView);
            return result;
        }
    }

    private static class OnItemClickListenerImp implements OnItemClickListener {

        private final SelectionDialogFragment selectionView;

        public OnItemClickListenerImp(final SelectionDialogFragment selectionView) {
            this.selectionView = selectionView;
        }

        @Override
        public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
            selectionView.selectedItemPosition = position;
            selectionView.dismiss();
        }
    }
}
