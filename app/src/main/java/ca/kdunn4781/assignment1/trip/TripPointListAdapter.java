package ca.kdunn4781.assignment1.trip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import ca.kdunn4781.assignment1.R;
import ca.kdunn4781.assignment1.databinding.LocationItemBinding;

/**
 * This class handles creating and binding items for a list containing the Location class
 */
public class TripPointListAdapter extends BaseAdapter {
    /**
     * This interface is for multiple click listeners for the inflated view
     */
    public interface OnTripPointClickListener {
        public void onAddPointClickListener(View view, int position);

        void onRemovePointClickListener(View view, int position);

        void onDetailPointClickListener(View view, int position);
    }

    private Context context;
    private LayoutInflater inflater;
    private List<TripPoint> list = new ArrayList<>();
    private OnTripPointClickListener pointClickListener;

    /**
     * This constructor creates an empty instance of LocationListAdapter
     * @param context the applications context
     * @param inflater inflater for creating the items
     */
    public TripPointListAdapter(
            Context context,
            LayoutInflater inflater,
            OnTripPointClickListener travelPointClickListener
    ) {
        this(context, inflater, travelPointClickListener, null);
    }

    /**
     * This constructor creates an instance of LocationListAdapter
     * populated with the locations provided
     * @param context the applications context
     * @param inflater inflater for creating the items
     * @param list populate list with provided locations
     */
    public TripPointListAdapter(Context context, LayoutInflater inflater, OnTripPointClickListener travelPointClickListener, List<TripPoint> list)
    {
        this.context = context;
        this.inflater = inflater;
        this.pointClickListener = travelPointClickListener;
        this.list = new ArrayList<>();

        if (list != null) {
            this.list.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void setList(List<TripPoint> list)
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
    public TripPoint getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // creates the view if it doesn't exist
        if (convertView == null) {
            convertView =  inflater.inflate(R.layout.location_item, parent, false);
        }

        // gets the current location from position
        TripPoint currentPoint = getItem(position);

        // create binding to views in item
        LocationItemBinding binding = DataBindingUtil.bind(convertView);

        if (binding != null) {
            // changes the card color
            binding.mainCard.setSelected(position == 0 || position == getCount() - 1);

            // updates the text
            binding.locationText.setText(currentPoint.getLocation().getName());
            binding.dateText.setText(currentPoint.getArrivalDate().format(DateTimeFormatter.ofPattern("MMMM dd @ HH:mm")));

            // sets the click listener for adding a new location
            binding.addLocationButton.setOnClickListener(v -> pointClickListener.onAddPointClickListener(v, position));

            // sets the click listener for removing a location
            binding.removeLocationButton.setOnClickListener(v -> pointClickListener.onRemovePointClickListener(v, position));

            // gets the next location
            if (currentPoint.getNextPointIndex() != null) {
                // shows the next layout and updates the text
                binding.nextLayout.setVisibility(View.VISIBLE);
                binding.travelDistanceText.setText(context.getString(R.string.kilometers, currentPoint.getDistanceToTravel()));
                binding.travelTimeText.setText(context.getString(R.string.minutes, currentPoint.getTimeToTravel().toMinutes()));
                binding.estimatedCostText.setText(context.getString(R.string.cost, currentPoint.getCostToLocation()));
            } else {
                // hides the next layout (used for when updating existing items)
                binding.nextLayout.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    //////////////////// GETTERS AND SETTERS ///////////////////////////


    public List<TripPoint> getList() {
        return list;
    }
}
