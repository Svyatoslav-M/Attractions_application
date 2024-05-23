package Home.Country_Frag;

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

import com.example.travel_app.MainActivity;
import com.example.travel_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Home.AttractionsRV_Frag.AttractionsListFragment;


public class HomeFragment extends Fragment {

    MainActivity activity;
    DatabaseReference database;
    ArrayList<Country> list;
    ValueEventListener eventListener;
    SearchView searchView;
    CountryAdapter adapter;
    RecyclerView rv;
    ProgressBar progressBar;

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
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database = FirebaseDatabase.getInstance("https://travelapp-5619b-default-rtdb.europe-west1.firebasedatabase.app").getReference("Countries");

        progressBar = view.findViewById(R.id.progressBar);

        rv = view.findViewById(R.id.rv_countrys);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        searchView = view.findViewById(R.id.searchView2);
        searchView.clearFocus();

        list = new ArrayList<Country>();

        adapter = new CountryAdapter(list);

        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);

        eventListener = database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    for (DataSnapshot attractionSnapshot : dataSnapshot.getChildren()) {
                        String imageUrl = attractionSnapshot.child("Image_URL").getValue(String.class);
                        String key = dataSnapshot.getKey();
                        Country country = new Country(key, imageUrl);
                        list.add(country);
                        break; // выходим из цикла после получения первой достопримечательности
                    }

                }
                adapter.notifyDataSetChanged();

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
            AttractionsListFragment attractionsListFragment = new AttractionsListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("countryName", countryName);
            attractionsListFragment.setArguments(bundle);

            // Заменяем текущий фрагмент на AttractionsListFragment
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.relativelayout, attractionsListFragment, "HOME_FRAGMENT");
            transaction.addToBackStack(null);
            transaction.commit();

        });

        DividerItemDecoration divider =
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(divider);

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

    @Override
    public void onResume() {
        super.onResume();
        // Вызываем метод инициализации RecyclerView и адаптера при возврате к фрагменту
        initializeRecyclerView();
    }

    private void initializeRecyclerView() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot attractionSnapshot : dataSnapshot.getChildren()) {
                        String imageUrl = attractionSnapshot.child("Image_URL").getValue(String.class);
                        String key = dataSnapshot.getKey();
                        Country country = new Country(key, imageUrl);
                        list.add(country);
                        break;
                    }
                }
                adapter = new CountryAdapter(list);

                progressBar.setVisibility(View.GONE);

                // Установка слушателя нажатий для адаптера
                adapter.setItemClickListener((countryName) -> {
                    AttractionsListFragment attractionsListFragment = new AttractionsListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("countryName", countryName);
                    attractionsListFragment.setArguments(bundle);

                    // Заменяем текущий фрагмент на AttractionsListFragment
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.relativelayout, attractionsListFragment, "HOME_FRAGMENT");
                    transaction.addToBackStack(null);
                    transaction.commit();

                });

                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("HomeFragment", "The read failed: " + error.getCode());
            }
        });
    }

}