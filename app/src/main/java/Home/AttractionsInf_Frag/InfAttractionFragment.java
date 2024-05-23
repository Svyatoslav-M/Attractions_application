package Home.AttractionsInf_Frag;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.travel_app.MainActivity;
import com.example.travel_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import Home.AttractionsInf_Frag.InfAttractionAdapter;
import Home.AttractionsRV_Frag.Attraction;
import Home.Comments_Frag.CommentsFragment;

//attractions information fragment
public class InfAttractionFragment extends Fragment {

    MainActivity activity;

    InfAttractionAdapter adapter;

    String Id, Title, Address, Category, Rating, Reviews, Image, Country, PageURL;


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

        View root = inflater.inflate(R.layout.fragment_inf_attraction, container, false);


        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        Bundle args = getArguments();
        if (args != null) {
            Id = args.getString("Id");
            Title = args.getString("Title");
            Address = args.getString("Address");
            Category = args.getString("Category");
            Rating = args.getString("Rating");
            Reviews = args.getString("Reviews");
            Image = args.getString("Image");
            Country = args.getString("Country");
            PageURL = args.getString("PageURL");

        }

        RecyclerView rv = view.findViewById(R.id.rv_inf_attr);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new InfAttractionAdapter(Title, Address, Category, Rating, Reviews, Image, Country, Id, PageURL);

        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);

        adapter.setItemClickListener(ButtonName -> {
            if(ButtonName == "CommentsButt") {
                CommentsFragment CommentsFrag = new CommentsFragment();

                Bundle bundle = new Bundle();
                bundle.putString("AttrId", Id);
                bundle.putString("Country", Country);

                CommentsFrag.setArguments(bundle);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.relativelayout, CommentsFrag, "HOME_FRAGMENT");
                transaction.addToBackStack(null);
                transaction.commit();
            }


        });

    }

}