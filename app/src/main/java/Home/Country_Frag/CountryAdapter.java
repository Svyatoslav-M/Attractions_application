package Home.Country_Frag;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.R;

import java.util.List;
import com.squareup.picasso.Picasso;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MyViewHolder>{
    ItemClickListener listener;
    class MyViewHolder extends RecyclerView.ViewHolder {


        View row;
        TextView Country;
        ImageView CountryImage;

        public MyViewHolder(View row){
            super(row);
            this.row = row;
            Country = row.findViewById(R.id.CountryText);
            CountryImage = row.findViewById(R.id.CountryimageView);
        }
    }
    List<Country> data;
    Country get_data_pos;

    public CountryAdapter(List<Country> data){
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =
                LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_activity, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        get_data_pos = data.get(position);
        holder.Country.setText(get_data_pos.getCountry());

        Picasso.get().load(get_data_pos.getCountryImageURL()).into(holder.CountryImage);


        holder.row.setOnClickListener(view -> {
            if(listener != null)
                listener.onItemClick(data.get(position).getCountry());
        });

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public void searchDataList(List<Country> searchData) {
        data = searchData;
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(String countryName);
    }


}
