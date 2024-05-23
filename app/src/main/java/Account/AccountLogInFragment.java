package Account;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.travel_app.MainActivity;
import com.example.travel_app.R;
import com.google.firebase.auth.FirebaseAuth;

//Fragment for logged in account
public class AccountLogInFragment extends Fragment {

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_account_log_in, container, false);

        Button Sign_out_butt = rootView.findViewById(R.id.buttonSignOut);

        mAuth = FirebaseAuth.getInstance();

        Sign_out_butt.setOnClickListener(v -> {
            mAuth.signOut();
            FragmentTransaction fragmentTransaction = getActivity()
                    .getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.relativelayout, new AccountNotLogInFragment(), "ACC_LOG_IN_FRAGMENT");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        });

        return rootView;
    }
}