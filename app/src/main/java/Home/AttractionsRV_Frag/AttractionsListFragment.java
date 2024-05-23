package Home.AttractionsRV_Frag;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.travel_app.MainActivity;
import com.example.travel_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Home.AttractionsInf_Frag.InfAttractionFragment;

//attractions rec view fragment
public class AttractionsListFragment extends Fragment {


    MainActivity activity;
    DatabaseReference database;
    ArrayList<Attraction> list;
    SearchView searchView;
    AttractionsAdapter adapter;
    String countryName;
    RecyclerView rv;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            activity = (MainActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_attractions_list, container, false);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            countryName = args.getString("countryName");
            Log.e("Country", countryName);
        }

        database = FirebaseDatabase.getInstance("https://travelapp-5619b-default-rtdb.europe-west1.firebasedatabase.app").getReference("Countries").child(countryName);

        rv = view.findViewById(R.id.rv_attractions);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        searchView = view.findViewById(R.id.AttractionsSearch);
        searchView.clearFocus();

        list = new ArrayList<Attraction>();

        adapter = new AttractionsAdapter(list);

        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot attractionSnapshot : snapshot.getChildren()) {

                    String imageUrl = attractionSnapshot.child("Image_URL").getValue(String.class);
                    String title = attractionSnapshot.child("Title").getValue(String.class);
                    String Address = attractionSnapshot.child("Address").getValue(String.class);
                    String Category = attractionSnapshot.child("Category").getValue(String.class);
                    String Rating = attractionSnapshot.child("Rating").getValue(String.class);
                    String Reviews = attractionSnapshot.child("Reviews").getValue(String.class);
                    String Page_URL = attractionSnapshot.child("Page_URL").getValue(String.class);

                    String id = attractionSnapshot.getKey();
                    Attraction attraction = new Attraction(id, title, imageUrl);

                    attraction.setAddress(Address);
                    attraction.setCategory(Category);
                    attraction.setRating(Rating);
                    attraction.setReviews(Reviews);
                    attraction.setPage_URL(Page_URL);

                    list.add(attraction);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AttractionsListFragment", "The read failed: " + error.getCode());
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        adapter.setItemClickListener((Id, Title, Address, Category, Rating, Reviews, imageUrl, pageUrl) -> {
            InfAttractionFragment infAttractionFragment = new InfAttractionFragment();
            Bundle bundle = new Bundle();

            bundle.putString("Id", Id);
            bundle.putString("Title", Title);
            bundle.putString("Address", Address);
            bundle.putString("Category", Category);
            bundle.putString("Rating", Rating);
            bundle.putString("Reviews", Reviews);
            bundle.putString("Image", imageUrl);
            bundle.putString("PageURL", pageUrl);


            infAttractionFragment.setArguments(bundle);

            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.relativelayout, infAttractionFragment, "HOME_FRAGMENT");
            transaction.addToBackStack(null);
            transaction.commit();
        });

        DividerItemDecoration divider =
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(divider);
    }
    public void searchList(String Text) {
        List<Attraction> searchData = new ArrayList<>();
        for(Attraction attraction1 : list) {
            if (attraction1.getTitle().toLowerCase().contains(Text.toLowerCase())) {
                searchData.add(attraction1);
            }
        }
        adapter.searchDataList(searchData);
    }

    //methods for back button
    @Override
    public void onResume() {
        super.onResume();
        initializeRecyclerView();
    }

    private void initializeRecyclerView() {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot attractionSnapshot : snapshot.getChildren()) {
                    String imageUrl = attractionSnapshot.child("Image_URL").getValue(String.class);
                    String title = attractionSnapshot.child("Title").getValue(String.class);
                    String Address = attractionSnapshot.child("Address").getValue(String.class);
                    String Category = attractionSnapshot.child("Category").getValue(String.class);
                    String Rating = attractionSnapshot.child("Rating").getValue(String.class);
                    String Reviews = attractionSnapshot.child("Reviews").getValue(String.class);
                    String id = attractionSnapshot.getKey();
                    String Page_URL = attractionSnapshot.child("Page_URL").getValue(String.class);

                    Attraction attraction = new Attraction(id, title, imageUrl);
                    attraction.setAddress(Address);
                    attraction.setCategory(Category);
                    attraction.setRating(Rating);
                    attraction.setReviews(Reviews);
                    attraction.setPage_URL(Page_URL);

                    list.add(attraction);
                }

                adapter = new AttractionsAdapter(list);

                adapter.setItemClickListener((Id, Title, Address, Category, Rating, Reviews, imageUrl, PageURL) -> {
                    InfAttractionFragment infAttractionFragment = new InfAttractionFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Id", Id);
                    bundle.putString("Title", Title);
                    bundle.putString("Address", Address);
                    bundle.putString("Category", Category);
                    bundle.putString("Rating", Rating);
                    bundle.putString("Reviews", Reviews);
                    bundle.putString("Image", imageUrl);
                    bundle.putString("Country", countryName);
                    bundle.putString("PageURL", PageURL);
                    infAttractionFragment.setArguments(bundle);

                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.relativelayout, infAttractionFragment, "HOME_FRAGMENT");
                    transaction.addToBackStack(null);
                    transaction.commit();
                });

                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AttractionsListFragment", "The read failed: " + error.getCode());
            }
        });
    }

}