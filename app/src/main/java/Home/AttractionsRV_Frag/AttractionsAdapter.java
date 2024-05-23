package Home.AttractionsRV_Frag;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.R;
import com.squareup.picasso.Picasso;

import java.util.List;

//adapter for attracctions rec view fragment
public class AttractionsAdapter extends RecyclerView.Adapter<AttractionsAdapter.MyViewHolder> {

    ItemClickListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {

        View row;
        TextView Attraction, Category;
        ImageView AttractionImage;

        public MyViewHolder(View row){
            super(row);
            this.row = row;
            Attraction = row.findViewById(R.id.AttractionText);
            AttractionImage = row.findViewById(R.id.AttractionImageView);
            Category = row.findViewById(R.id.TypeAttractionText);
        }
    }
    List<Attraction> data;
    Attraction get_data_pos;

    public AttractionsAdapter(List<Attraction> data){
        this.data = data;
    }

    @NonNull
    @Override
    public AttractionsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =
                LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.rec_view_attraction, parent, false);
        return new AttractionsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttractionsAdapter.MyViewHolder holder, int position) {

        get_data_pos = data.get(position);
        holder.Attraction.setText(get_data_pos.getTitle());
        holder.Category.setText(get_data_pos.getCategory());

        Picasso.get().load(get_data_pos.getImage_URL()).into(holder.AttractionImage);

        holder.row.setOnClickListener(view -> {
            if(listener != null)
                listener.onItemClick(data.get(position).getId(),
                        data.get(position).getTitle(),
                        data.get(position).getAddress(),
                        data.get(position).getCategory(),
                        data.get(position).getRating(),
                        data.get(position).getReviews(),
                        data.get(position).getImage_URL(),
                        data.get(position).getPage_URL());
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void searchDataList(List<Attraction> searchData) {
        data = searchData;
        notifyDataSetChanged();
    }

    public void setItemClickListener(AttractionsAdapter.ItemClickListener listener) {
        this.listener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(String Id, String Title, String Address, String Category, String Rating, String Reviews, String Image, String PageURL);
    }
}
