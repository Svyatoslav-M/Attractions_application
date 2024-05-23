package Account;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.travel_app.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

//Reset password fragment
public class ResetPasswordFragment extends Fragment {

    private FirebaseAuth mAuth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_reset_password, container, false);

        Button reset_pass = root.findViewById(R.id.res_pass);
        EditText email_edt = root.findViewById(R.id.Res_Email_EditText);

        reset_pass.setOnClickListener(v -> {

            mAuth = FirebaseAuth.getInstance();

            String email = String.valueOf(email_edt.getText());

            if (!TextUtils.isEmpty(email)) {
                ResetPassword(email);
                reset_pass.setVisibility(View.GONE);
            } else {
                email_edt.setError("Email field can't be empty");
            }

        });
        return root;
    }
    private void ResetPassword(String strEmail) {

        mAuth.sendPasswordResetEmail(strEmail)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getActivity(), "Reset Password link has been sent to your registered Email", Toast.LENGTH_SHORT).show();

                    }
                });
    }
}