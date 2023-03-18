package ca.kdunn4781.assignment1.trip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.kdunn4781.assignment1.R;

/**
 * This class is a list adapter for the saved trips fragment
 */
public class SavedTripsListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Trip> list = new ArrayList<>();
    private AdapterView.OnItemClickListener onItemClickListener;

    /**
     * This constructor creates an empty instance of SavedTripsListAdapter
     * @param context the applications context
     * @param inflater inflater for creating the items
     */
    public SavedTripsListAdapter(
            Context context,
            LayoutInflater inflater
    ) {
        this(context, inflater, null);
    }

    /**
     * This constructor creates an instance of SavedTripsListAdapter
     * populated with the locations provided
     * @param context the applications context
     * @param inflater inflater for creating the items
     * @param list populate list with provided locations
     */
    public SavedTripsListAdapter(Context context, LayoutInflater inflater, List<Trip> list)
    {
        this.context = context;
        this.inflater = inflater;
        this.list = new ArrayList<>();

        if (list != null) {
            this.list.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setList(List<Trip> list)
    {
        this.list.clear();
        this.list.addAll(list);

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Trip getItem(int i) {
        if (i < 0 || i >= list.size())
            return null;

        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        Trip trip = getItem(i);

        if (trip != null) {
            return trip.getId();
        }

        return -1;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // creates the view if it doesn't exist
        if (view == null) {
            view =  inflater.inflate(android.R.layout.simple_list_item_2, viewGroup, false);
        }

        view.setOnClickListener((v) -> {
            onItemClickListener.onItemClick(null, v, i, getItemId(i));
        });

        Trip trip = getItem(i);

        if (trip != null) {
            TextView text1 = view.findViewById(android.R.id.text1);
            TextView text2 = view.findViewById(android.R.id.text2);

            text1.setText(context.getResources().getString(R.string.tripListItem1, trip.getId(), trip.getName()));

            if (trip.getDescription() != null) {
                text2.setText(trip.getDescription());
            } else {
                text2.setText("No description");
            }
        }

        return view;
    }
}
