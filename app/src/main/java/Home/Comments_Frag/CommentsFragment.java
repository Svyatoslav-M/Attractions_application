package Home.Comments_Frag;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

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

import Home.AttractionsRV_Frag.Attraction;
import Home.AttractionsRV_Frag.AttractionsAdapter;

//comments fragment
public class CommentsFragment extends Fragment {

    MainActivity activity;
    private DatabaseReference database, userDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    String countryName, AttrID;
    RecyclerView rv;
    Button PostButton;

    EditText CommentEditText;

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

        View root = inflater.inflate(R.layout.fragment_comments, container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            countryName = args.getString("Country");
            AttrID = args.getString("AttrId");
        }

        database = FirebaseDatabase.getInstance("https://travelapp-5619b-default-rtdb.europe-west1.firebasedatabase.app").
                getReference("Countries").child(countryName).child(AttrID).child("comments");

        PostButton = view.findViewById(R.id.PostButton);
        CommentEditText = view.findViewById(R.id.editTextText);


        rv = view.findViewById(R.id.rv_comments);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Comments> comments = new ArrayList<>();
        CommentsAdapter adapter = new CommentsAdapter(comments);

        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);

        rv.setAdapter(adapter);


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                comments.clear();
                for (DataSnapshot commentSnapshot : dataSnapshot.getChildren()) {
                    Comments comment = commentSnapshot.getValue(Comments.class);
                    comments.add(comment);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        PostButton.setOnClickListener(v -> {
            String Comment = CommentEditText.getText().toString();

            mAuth = FirebaseAuth.getInstance();
            currentUser = mAuth.getCurrentUser();
            if(currentUser != null) {
                String userId = currentUser.getUid();
                userDatabase = FirebaseDatabase.getInstance("https://travelapp-5619b-default-rtdb.europe-west1.firebasedatabase.app").
                        getReference("user").child(userId);

                userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // get user name
                            String userName = dataSnapshot.child("user_name").getValue(String.class);
                            addComment(userId, userName, Comment);
                        } else {
                            Log.e("UserName", "User not found.");
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("UserName", "Error fetching user data", databaseError.toException());
                    }
                });
            }

        });


    }

    public void addComment(String userId, String userName, String commentText) {

        String commentId = database.push().getKey();
        long timestamp = System.currentTimeMillis();

        Comments comment = new Comments(userId, userName, commentText, timestamp);
        database.child(commentId).setValue(comment);
    }
}