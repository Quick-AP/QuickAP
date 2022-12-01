package com.quickap.quickap.design;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.quickap.quickap.R;
import com.quickap.quickap.activities.MenuActivity;
import com.quickap.quickap.controller.MenuController;
import com.quickap.quickap.model.FoodModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MenuGridViewAdapter extends ArrayAdapter<FoodModel> implements Filterable {

    private final MenuController menuController;
    private final MenuActivity menuActivity;
    private List<FoodModel> foodModels;
    private final List<FoodModel> foodModelsFilterList;
    private ValueFilter valueFilter;

    public MenuGridViewAdapter(@NonNull MenuActivity menuActivity, ArrayList<FoodModel> foodModels, MenuController menuController) {
        super(menuActivity, 0, foodModels);
        this.menuController = menuController;
        this.menuActivity = menuActivity;
        this.foodModels = foodModels;
        this.foodModelsFilterList = foodModels;
    }

    @Override
    public int getCount() {
        return foodModels.size();
    }

    @Override
    public FoodModel getItem(int position) {
        return foodModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * Render a custom view via menu_grid_view_item_list and the given Food List.
     * @param position position of food menu item.
     * @param convertView view to render food card onto.
     * @param parent the parent view group.
     * @return the populated convert view.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        HolderValue holderValue;
        FoodModel food = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.menu_grid_view_item_list, parent, false);
            holderValue = new HolderValue(convertView);
            convertView.setTag(holderValue);
        }
        else {
            holderValue = (HolderValue) convertView.getTag();
        }

        int initialAmount = this.menuController.getAmountOfFood(food.getFoodID());
        Log.d("DEBUG: initial amount", String.format(Locale.ENGLISH, "%s:%d",
                food.getFoodID(), initialAmount));
        holderValue.updateCalculatedPrice(initialAmount, food.getPrice());

        holderValue.nameTextView.setText(food.getName());
        holderValue.priceTextView.setText(String.format(
                Locale.ENGLISH,
                "%.2f RMB",
                food.getPrice()));

        holderValue.stepperMinus.setOnClickListener(v -> {
            this.menuController.removeFood(food);
            notifyDataSetChanged();
        });

        holderValue.stepperPlus.setOnClickListener(v -> {
            this.menuController.addFood(food);
            notifyDataSetChanged();
        });

        // TODO: Load Drawable from Json URLS and display
        if (food.isOnline()) {
            final Bitmap[] foodImage = new Bitmap[1];
            // Force download to run separate from the main thread
            Thread foodImageDownloader = new Thread(){
                @Override
                public void run()
                {
                    foodImage[0] = getImageBitmap(food.getFoodURL());
                }
            };

            foodImageDownloader.start();
            try {
                foodImageDownloader.join();
                holderValue.menuImage.setImageBitmap(foodImage[0]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else {
            holderValue.menuImage.setImageResource(food.getFoodImageID());
        }

        return convertView;
    }

    /**
     * Downloads a image from URL and returns a corresponding Drawable.
     * @param urlString URL of the Image.
     * @param srcName Source name of the Drawable.
     * @return Drawable containing the downloaded image, or null when failed to download.
     */
    public static Drawable getDrawableImageFromUrl(String urlString, String srcName) {
        try {
            InputStream is = (InputStream) new URL(urlString).getContent();
            return Drawable.createFromStream(is, srcName);
        } catch (Exception e) {
            Log.e("FoodArrayListModel",
                    "Failed to load image from "+urlString+" : "+e.getMessage());

            return null;
        }
    }

    public Bitmap getImageBitmap(String url) {
        URL imgUrl;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imgUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bitmap;
    }

    private static class HolderValue {
        public ImageView menuImage;
        public TextView nameTextView;
        public TextView priceTextView;
        public Button stepperMinus;
        public Button stepperPlus;
        public Button calculatedPrice;

        public HolderValue(View view) {
            menuImage = view.findViewById(R.id.menu_image_id);
            nameTextView = view.findViewById(R.id.menu_item_name);
            priceTextView = view.findViewById(R.id.menu_item_price);
            stepperMinus = view.findViewById(R.id.menu_item_stepper_minus);
            stepperPlus = view.findViewById(R.id.menu_item_stepper_plus);
            calculatedPrice = view.findViewById(R.id.menu_item_button_calculated_price);
        }

        void updateCalculatedPrice(int amount, double price) {
            this.calculatedPrice.setText(String.format(
                    Locale.ENGLISH,
                    "%.2f x %d = %.2f RMB",
                    price,
                    amount,
                    amount * price
            ));
        }

    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {

        protected boolean keepFood(FoodModel food, String queryString) {
            Log.d("QUERY STRING", queryString + "?" + food.getName());
            queryString = queryString.toLowerCase(Locale.ROOT);
            return food.getName().toLowerCase(Locale.ROOT).contains(queryString)
                    || food.getFoodType().toLowerCase(Locale.ROOT).contains(queryString);
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                String queryString = constraint.toString();
                ArrayList<FoodModel> filteredFoodModels = new ArrayList<>();

                for (FoodModel food : foodModelsFilterList) {
                    if (keepFood(food, queryString))
                        filteredFoodModels.add(food);
                }
                results.count = filteredFoodModels.size();
                results.values = filteredFoodModels;
                System.out.println(results.count);

            } else {
                results.count = foodModelsFilterList.size();
                results.values = foodModelsFilterList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            foodModels = (List<FoodModel>) results.values;
            notifyDataSetChanged();
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        this.menuActivity.updateTotalPrice(
                String.format(Locale.ENGLISH, "%.2f", this.menuController.getTotalPrice()));
    }
}
