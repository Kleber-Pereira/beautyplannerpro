package com.tcc.beautyplannerpro.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.tcc.beautyplannerpro.R;
import com.tcc.beautyplannerpro.activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;


public class FragmentHome extends Fragment {

    private CardView cardView_SignOut;
    private TextView textView_UserEmail;
    private FirebaseAuth user;
    public FragmentHome() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_home, container, false);

        cardView_SignOut = view.findViewById(R.id.cardView_SignOut);
        textView_UserEmail = view.findViewById(R.id.textView_UserEmail);
        user = FirebaseAuth.getInstance();
        if (user.getCurrentUser() != null) {
            String userEmail = user.getCurrentUser().getEmail();
            textView_UserEmail.setText(userEmail);
        }
        cardView_SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                // For example, you can trigger the sign-out logic here
                signOut();
            }
        });

        return view;
    }
    private void signOut() {
        // Implement the sign-out logic here.
        // For example, if you're using Firebase Authentication, you can sign out as follows:
        FirebaseAuth.getInstance().signOut();

        // Redirect to the login screen or perform any necessary actions after sign-out.
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }
}