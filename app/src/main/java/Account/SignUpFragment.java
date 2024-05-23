package Account;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.travel_app.MainActivity;
import com.example.travel_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Account.AccountLogInFragment;

//Sig up fragment
public class SignUpFragment extends Fragment {

    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";
    private EditText user_edtext, email_edtext, password_edtext;
    private String user_name, email, password;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private static final String USERS = "user";
    private User user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        user_edtext = rootView.findViewById(R.id.UserNameEditText);
        email_edtext = rootView.findViewById(R.id.Email_EditText);
        password_edtext = rootView.findViewById(R.id.Password_EditText);

        database = FirebaseDatabase.getInstance("https://travelapp-5619b-default-rtdb.europe-west1.firebasedatabase.app");
        mDatabase = database.getReference(USERS);
        mAuth = FirebaseAuth.getInstance();

        Button button_sign_up = rootView.findViewById(R.id.sign_up_butt);

        button_sign_up.setOnClickListener( v -> {

            if(email_edtext.getText().toString() != null && password_edtext.getText().toString() != null
                    && user_edtext.getText().toString() != null) {

                user_name = String.valueOf(user_edtext.getText());
                email = String.valueOf(email_edtext.getText());
                password = String.valueOf(password_edtext.getText());

                user = new User(user_name, email);

                createUser();
            }

        });

        return rootView;
    }


    public void createUser() {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in users information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            FragmentTransaction fragmentTransaction = getActivity()
                                    .getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.relativelayout, new AccountLogInFragment(), "SIGN_UP_FRAGMENT");
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            String uid = currentUser.getUid();
            mDatabase.child(uid).setValue(user);
        }
    }
}