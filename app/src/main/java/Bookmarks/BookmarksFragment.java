package Bookmarks;

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
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.travel_app.MainActivity;
import com.example.travel_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Home.AttractionsRV_Frag.AttractionsListFragment;
import Home.Country_Frag.Country;
import Home.Country_Frag.CountryAdapter;

//fragment for countries in bookmarks list
public class BookmarksFragment extends Fragment {

    MainActivity activity;

    DatabaseReference bookmarks_database, counries_database;
    ArrayList<Country> list;
    ValueEventListener eventListener, seceventListener;
    SearchView searchView;
    CountryAdapter adapter;
    RecyclerView rv;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    TextView LogInToViewBookmarks;

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
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.BookmarkProgressBar);
        LogInToViewBookmarks = view.findViewById(R.id.LogInToView);

        if (currentUser != null) {
            String userId = currentUser.getUid();
            bookmarks_database = FirebaseDatabase.getInstance("https://travelapp-5619b-default-rtdb.europe-west1.firebasedatabase.app").
                    getReference("user").child(userId).child("bookmarks");
            counries_database = FirebaseDatabase.getInstance("https://travelapp-5619b-default-rtdb.europe-west1.firebasedatabase.app").
                    getReference("Countries");

            rv = view.findViewById(R.id.rec_view_bookmarks);
            rv.setLayoutManager(new LinearLayoutManager(getContext()));

            searchView = view.findViewById(R.id.searchBookmarks);
            searchView.clearFocus();

            list = new ArrayList<Country>();

            adapter = new CountryAdapter(list);

            rv.setAdapter(adapter);
            rv.setHasFixedSize(true);

            eventListener = bookmarks_database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot CountriesSnapshot : snapshot.getChildren()) {


                        seceventListener = counries_database.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot attractionSnapshot : dataSnapshot.child(CountriesSnapshot.getKey()).getChildren()) {

                                    String imageUrl = attractionSnapshot.child("Image_URL").getValue(String.class);
                                    String key = dataSnapshot.getKey();
                                    Log.e("Bookmarks", CountriesSnapshot.getKey());
                                    Country country = new Country(CountriesSnapshot.getKey(), imageUrl);
                                    list.add(country);
                                    break;
                                }

                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                System.out.println("The read failed: " + error.getCode());
                            }
                        });


                    }

                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    System.out.println("The read failed: " + error.getCode());
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

            adapter.setItemClickListener((countryName) -> {
                BookmarkedAttractFragment attractionsListFragment = new BookmarkedAttractFragment();
                Bundle bundle = new Bundle();
                bundle.putString("countryName", countryName);
                attractionsListFragment.setArguments(bundle);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.relativelayout, attractionsListFragment, "BOOKMARKS_FRAGMENT");
                transaction.addToBackStack(null);
                transaction.commit();

            });

            DividerItemDecoration divider =
                    new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
            rv.addItemDecoration(divider);
        }
        else {
            progressBar.setVisibility(View.GONE);
            LogInToViewBookmarks.setVisibility(View.VISIBLE);
        }

    }

    public void searchList(String Text) {
        List<Country> searchData = new ArrayList<>();
        for(Country country1 : list) {

            if (country1.getCountry().toLowerCase().contains(Text.toLowerCase())) {
                searchData.add(country1);
            }
        }
        adapter.searchDataList(searchData);
    }


}