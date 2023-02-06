package ca.kdunn4781.assignment1.location;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import ca.kdunn4781.assignment1.R;
import ca.kdunn4781.assignment1.databinding.LocationItemBinding;

/**
 * This class handles creating and binding items for a list containing the Location class
 */
public class LocationListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Location> locations = new ArrayList<>();

    /**
     * This constructor creates an empty instance of LocationListAdapter
     * @param context the applications context
     * @param inflater inflater for creating the items
     */
    public LocationListAdapter(Context context, LayoutInflater inflater) {
        this(context, inflater, null);
    }

    /**
     * This constructor creates an instance of LocationListAdapter
     * populated with the locations provided
     * @param context the applications context
     * @param inflater inflater for creating the items
     * @param locations populate list with provided locations
     */
    public LocationListAdapter(Context context, LayoutInflater inflater, List<Location> locations)
    {
        this.context = context;
        this.inflater = inflater;

        if (locations != null) {
            this.locations.addAll(locations);
            calculateDistances();
            notifyDataSetChanged();
        }
    }

    /**
     * This functions calculates the distances from one location to another.
     *
     * *Note:* these are fake distances and aren't reliable in any way
     */
    private void calculateDistances() {
        Location prev = null;

        for (int i = 0; i < locations.size(); i++) {
            Location location = locations.get(i);
            location.setNextLocation(null);
            location.setTimeToTravel(null);

            if (prev != null) {
                prev.setNextLocation(location);

                Duration duration = Duration.ofMinutes((long)(50 * 0.9));
                prev.setTimeToTravel(duration);

                location.setDate(prev.getDate().plusMinutes(duration.toMinutes()));
            }

            prev = location;
        }
    }

    /**
     * This function adds a location to the list
     * @param index the index to insert the location at
     * @param location the location to add
     */
    public void addLocation(int index, Location location)
    {
        locations.add(index, location);
        calculateDistances();
        notifyDataSetChanged();
    }

    /**
     * This function removes a location from the list
     * @param location the location to remove
     */
    public void removeLocation(Location location)
    {
        locations.remove(location);
        calculateDistances();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return locations.size();
    }

    @Override
    public Location getItem(int position) {
        return locations.get(position);
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
        Location currentLocation = getItem(position);

        // create binding to views in item
        LocationItemBinding binding = DataBindingUtil.bind(convertView);

        if (binding != null) {
            // changes the card color
            binding.mainCard.setSelected(position == 0 || position == getCount() - 1);

            // updates the text
            binding.locationText.setText(currentLocation.getLocation());
            binding.dateText.setText(currentLocation.getDate().format(DateTimeFormatter.ofPattern("MMMM dd @ HH:mm")));

            // sets the click listener for adding a new location
            binding.addLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // shows a dialog for selecting a list of locations
                    AlertDialog.Builder b = new AlertDialog.Builder(context);
                    String[] locations = context.getResources().getStringArray(R.array.locations);
                    b.setTitle("Add Location");
                    b.setItems(locations, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            addLocation(position + 1, new Location(locations[which]));
                        }
                    });
                    b.show();
                }
            });

            // sets the click listener for removing a location
            binding.removeLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeLocation(currentLocation);
                }
            });

            // gets the next location
            Location nextLocation = currentLocation.getNextLocation();

            if (nextLocation != null) {
                // shows the next layout and updates the text
                binding.nextLayout.setVisibility(View.VISIBLE);
                binding.travelTimeText.setText(context.getString(R.string.minutes, currentLocation.getTimeToTravel().toMinutes()));
            } else {
                // hides the next layout (used for when updating existing items)
                binding.nextLayout.setVisibility(View.GONE);
            }
        }

        return convertView;
    }
}
