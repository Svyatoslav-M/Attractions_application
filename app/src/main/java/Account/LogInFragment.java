package Account;


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
import android.widget.TextView;
import android.widget.Toast;

import com.example.travel_app.MainActivity;
import com.example.travel_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//Login fragment
public class LogInFragment extends Fragment {

    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_log_in, container, false);

        Button sign_up_butt = rootView.findViewById(R.id.sign_up_butt);
        Button sign_in_butt = rootView.findViewById(R.id.sign_in_butt);

        EditText email_edtext = rootView.findViewById(R.id.Email_EditText);
        EditText password_edtext = rootView.findViewById(R.id.Password_EditText);

        TextView reset_pass = rootView.findViewById(R.id.ForgotPass);

        mAuth = FirebaseAuth.getInstance();

        sign_in_butt.setOnClickListener(v -> {
            String email = String.valueOf(email_edtext.getText());
            String password = String.valueOf(password_edtext.getText());


            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in users information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                FragmentTransaction fragmentTransaction = getActivity()
                                        .getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.relativelayout, new AccountLogInFragment());
                                fragmentTransaction.commit();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(getActivity(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });

        //Navigation
        sign_up_butt.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = getActivity()
                    .getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.relativelayout, new SignUpFragment(), "LOG_IN_FRAGMENT");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        });

        reset_pass.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = getActivity()
                    .getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.relativelayout, new ResetPasswordFragment(), "LOG_IN_FRAGMENT");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        });

        return rootView;
    }

}