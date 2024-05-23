package Home.AttractionsInf_Frag;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import Home.AttractionsRV_Frag.AttractionsAdapter;

//Adapter for attractions information fragment
public class InfAttractionAdapter extends RecyclerView.Adapter<InfAttractionAdapter.MyViewHolder> {
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private Context context;

    ItemClickListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageButton bookmarks_butt;
        View row;
        TextView Title, Address, Category, Rating;
        ImageView AttractionImage;

        Button GoogleMapsButton, CommentsButton;



        public MyViewHolder(View row) {
            super(row);
            this.row = row;

            Title = row.findViewById(R.id.textTitle);
            Address = row.findViewById(R.id.textAddress);
            Category = row.findViewById(R.id.textCategory);
            Rating = row.findViewById(R.id.textRating);
            bookmarks_butt = row.findViewById(R.id.imageButton);
            GoogleMapsButton = row.findViewById(R.id.GoogleMapsButton);
            CommentsButton = row.findViewById(R.id.CommentsButton);

            AttractionImage = row.findViewById(R.id.AttrInfImage);
            context = row.getContext();

            database = FirebaseDatabase.getInstance("https://travelapp-5619b-default-rtdb.europe-west1.firebasedatabase.app");

            mAuth = FirebaseAuth.getInstance();


        }
    }

    String Title, Address, Category, Rating, Reviews, Image, Country, Attr_Id, PageURL;

    public InfAttractionAdapter(String Title, String Address, String Category, String Rating,
                                String Reviews, String Image, String Country, String Attr_id, String PageURL) {
        this.Title = Title;
        this.Address = Address;
        this.Category = Category;
        this.Rating = Rating;
        this.Reviews = Reviews;
        this.Image = Image;
        this.Country = Country;
        this.Attr_Id = Attr_id;
        this.PageURL = PageURL;
    }

    @NonNull
    @Override
    public InfAttractionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =
                LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.rec_view_attr_inf, parent, false);
        return new InfAttractionAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InfAttractionAdapter.MyViewHolder holder, int position) {

        holder.Title.setText(Title);
        holder.Category.setText("Category: " + Category);
        holder.Address.setText("Address: " + Address);
        holder.Rating.setText("Rating: " + Rating + " " +Reviews);

        Picasso.get().load(Image).into(holder.AttractionImage);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        holder.GoogleMapsButton.setOnClickListener(view -> {
            Uri uri = Uri.parse(PageURL); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        });

        holder.CommentsButton.setOnClickListener(view -> {
            if(listener != null) {
                listener.onItemClick("CommentsButt");
            }
        });

        if (currentUser != null) {
            // get identify of user
            String userId = currentUser.getUid();

            // path to bookmarks in user
            DatabaseReference userBookmarksRef = database.getReference("user").child(userId).child("bookmarks");

            userBookmarksRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // check if attraction in database
                    if (snapshot.child(Country).hasChild(Attr_Id)) {
                        // attraction added to bookmarks
                        holder.bookmarks_butt.setImageResource(R.drawable.baseline_bookmark_24);
                    } else {
                        // attraction didnt added to bookmarks
                        holder.bookmarks_butt.setImageResource(R.drawable.baseline_bookmark_border_24);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

        holder.bookmarks_butt.setOnClickListener(view -> {

            if (Attr_Id != null) {
                toggleBookmark(holder, Attr_Id);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    private void toggleBookmark(MyViewHolder holder, String attraction) {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userBookmarksRef = database.getReference("user").child(userId).child("bookmarks");

            String attractionKey = attraction;

            userBookmarksRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(Country)) {
                        if(snapshot.child(Country).hasChild(attractionKey)) {
                            // attraction in bookmarks already
                            userBookmarksRef.child(Country).child(attractionKey).removeValue();
                            holder.bookmarks_butt.setImageResource(R.drawable.baseline_bookmark_border_24);
                        }
                        else {
                            userBookmarksRef.child(Country).child(attractionKey).setValue(true);
                            holder.bookmarks_butt.setImageResource(R.drawable.baseline_bookmark_24);
                        }

                    } else {
                        // attraction not in bookmarks, adding it
                        userBookmarksRef.child(Country).child(attractionKey).setValue(true);
                        holder.bookmarks_butt.setImageResource(R.drawable.baseline_bookmark_24);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    public void setItemClickListener(InfAttractionAdapter.ItemClickListener listener) {
        this.listener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(String ButtonName);
    }

}
