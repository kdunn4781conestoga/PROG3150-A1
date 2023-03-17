package ca.kdunn4781.assignment1.trip;

import android.view.View;

public interface OnTripPointClickListener {
    public void onAddPointClickListener(View view, int position);

    void onRemovePointClickListener(View view, int position);

    void onDetailPointClickListener(View view, int position);
}
