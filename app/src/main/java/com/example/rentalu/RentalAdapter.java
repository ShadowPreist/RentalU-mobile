package com.example.rentalu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RentalAdapter extends RecyclerView.Adapter<RentalAdapter.ViewHolder> {

    private Context context;
    private List<RentalPost> rentalList;
    private int user_id;  // Add this field to store user_id
    private OnItemClickListener onItemClickListener;
    private DatabaseChangeListener databaseChangeListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface DatabaseChangeListener {
        void onDataChanged(List<RentalPost> updatedData);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setDatabaseChangeListener(DatabaseChangeListener listener) {
        this.databaseChangeListener = listener;
    }

    public RentalAdapter(Context context, List<RentalPost> rentalList, int user_id) {
        this.context = context;
        this.rentalList = rentalList;
        this.user_id = user_id;
    }

    public void updateData(List<RentalPost> updatedList) {
        rentalList = updatedList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rental_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RentalPost rentalPost = rentalList.get(position);

        // Bind your data to the ViewHolder views
        holder.propertyTypeTextView.setText("Property Type: " + rentalPost.getPropertyType());
        holder.bedroomTextView.setText("Bedrooms: " + rentalPost.getBedroom());
        holder.dateAddedTextView.setText("Date Added: " + rentalPost.getDateAdded());
        holder.locationTextView.setText("Location: " + rentalPost.getLocation());
        holder.priceTextView.setText("Price: " + rentalPost.getPrice());
        holder.furniTypesTextView.setText("Furniture Types: " + rentalPost.getFurniTypes());
        holder.phoneNoTextView.setText("Contact: " + rentalPost.getPhoneNo());
        holder.remarksTextView.setText("Remarks: " + rentalPost.getRemarks());
        holder.reporterTextView.setText("Reporter: " + rentalPost.getReporter());

        // Set click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Use holder.getAdapterPosition() to get the current position
                int currentPosition = holder.getAbsoluteAdapterPosition();

                // Handle item click, e.g., start EditPostActivity with user_id and rentalPost data
                if (currentPosition != RecyclerView.NO_POSITION) {
                    if (rentalList.get(currentPosition).getUserId() == user_id) {
                        Intent editIntent = new Intent(context, update_rental.class);
                        editIntent.putExtra("user_id", user_id);
                        editIntent.putExtra("ref_id", rentalList.get(currentPosition).getRefId());
                        editIntent.putExtra("property_type", rentalList.get(currentPosition).getPropertyType());
                        editIntent.putExtra("bedroom", rentalList.get(currentPosition).getBedroom());
                        editIntent.putExtra("location", rentalList.get(currentPosition).getLocation());
                        editIntent.putExtra("price", rentalList.get(currentPosition).getPrice());
                        editIntent.putExtra("furniture_type", rentalList.get(currentPosition).getFurniTypes());
                        editIntent.putExtra("contact", rentalList.get(currentPosition).getPhoneNo());
                        editIntent.putExtra("remarks", rentalList.get(currentPosition).getRemarks());
                        editIntent.putExtra("reporter", rentalList.get(currentPosition).getReporter());
                        context.startActivity(editIntent);
                    } else {
                        Toast.makeText(context, "You can only edit your own posts", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return rentalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView propertyTypeTextView;
        TextView bedroomTextView;
        TextView dateAddedTextView, locationTextView, priceTextView, furniTypesTextView, phoneNoTextView, remarksTextView, reporterTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            propertyTypeTextView = itemView.findViewById(R.id.propertyTypeTextView);
            bedroomTextView = itemView.findViewById(R.id.bedroomTextView);
            dateAddedTextView = itemView.findViewById(R.id.dateAddedTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            furniTypesTextView = itemView.findViewById(R.id.furniTypesTextView);
            phoneNoTextView = itemView.findViewById(R.id.phoneNoTextView);
            remarksTextView = itemView.findViewById(R.id.remarksTextView);
            reporterTextView = itemView.findViewById(R.id.reporterTextView);
        }
    }
}



