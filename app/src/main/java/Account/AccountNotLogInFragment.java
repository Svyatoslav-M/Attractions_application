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

//Fragment for not logged in account
public class AccountNotLogInFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account_not_log_in, container, false);

        Button btnSignIn = rootView.findViewById(R.id.buttonSignOut);

        btnSignIn.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = getActivity()
                    .getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.relativelayout, new LogInFragment(), "ACC_NOT_LOGIN_FRAGMENT");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();


        });


        return rootView;
    }
}