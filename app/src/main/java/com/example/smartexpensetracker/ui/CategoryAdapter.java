package com.example.smartexpensetracker.ui;

// Android classes used for creating and working with Views
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

// RecyclerView classes
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// Our project's resources and Category model
import com.example.smartexpensetracker.R;
import com.example.smartexpensetracker.model.Category;

// Java List used to store multiple Category objects
import java.util.List;


// Adapter connects Category data to the RecyclerView
public class CategoryAdapter
        extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    // Stores all Category objects
    private List<Category> categoryList;
    private OnCategoryClickListener listener;


    // Constructor receives the list of categories
    public CategoryAdapter(List<Category> categoryList) {

        // Store the received list inside this object's variable
        this.categoryList = categoryList;
    }
    public CategoryAdapter(
            List<Category> categoryList,
            OnCategoryClickListener listener) {

        this.categoryList = categoryList;
        this.listener = listener;
    }

    // Creates the View for one category item
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        // Converts item_category.xml into an actual Android View
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);

        // Create and return the ViewHolder
        return new CategoryViewHolder(view);
    }


    // Puts Category data into the item Views
    @Override
    public void onBindViewHolder(
            @NonNull CategoryViewHolder holder,
            int position) {

        // Get the Category object at the current position
        Category category = categoryList.get(position);

        // Put the category icon into the icon TextView
        holder.tvCategoryIcon.setText(category.getIcon());

        // Put the category name into the name TextView
        holder.tvCategoryName.setText(category.getName());

        holder.itemView.setOnClickListener(v -> {
            listener.onCategoryClick(category);
        });
    }


    // Tells RecyclerView how many categories are in the list
    @Override
    public int getItemCount() {

        // Return the number of Category objects
        return categoryList.size();
    }


    // ViewHolder stores references to Views inside one category item
    public static class CategoryViewHolder
            extends RecyclerView.ViewHolder {

        // References to the TextViews from item_category.xml
        TextView tvCategoryIcon;
        TextView tvCategoryName;


        // ViewHolder constructor
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            // Find the icon TextView from item_category.xml
            tvCategoryIcon =
                    itemView.findViewById(R.id.tvCategoryIcon);

            // Find the name TextView from item_category.xml
            tvCategoryName =
                    itemView.findViewById(R.id.tvCategoryName);
        }
    }
}