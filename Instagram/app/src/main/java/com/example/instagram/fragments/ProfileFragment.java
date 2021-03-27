package com.example.instagram.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.instagram.LoginActivity;
import com.example.instagram.MainActivity;
import com.example.instagram.Post;
import com.example.instagram.R;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnLogout;
    private static final String TAG = "Login";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etUsername = view.findViewById(R.id.username);
        etPassword = view.findViewById(R.id.password);
        btnLogin = view.findViewById(R.id.login);
        btnLogout = view.findViewById(R.id.btnLogout);
        if(ParseUser.getCurrentUser() != null){
            btnLogin.setVisibility(View.INVISIBLE);
            etUsername.setVisibility(View.INVISIBLE);
            etPassword.setVisibility(View.INVISIBLE);
        }
        btnLogin.setOnClickListener(view12 -> {
            Log.i(TAG, "onClick Login Button");
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            loginUser(username,password);
        });
        btnLogout.setOnClickListener(view1 -> logoutUser());
    }

    private void loginUser(String username, String password){
        Log.i(TAG, "Attempting to login user" + username);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null){
                    Log.e(TAG,"Issue with login", e);
                    return;
                }
                //navigate to the main activity if the user has signed in properly
                gotoMainActivity();
                Toast.makeText(getContext(), "Logged in successfully", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void logoutUser(){
        if(ParseUser.getCurrentUser() != null){
            Toast.makeText(getContext(),"Logged out successfully", Toast.LENGTH_SHORT).show();
            ParseUser.logOutInBackground();
            gotoLoginActivity();
        }
    }

    private void gotoMainActivity() {
        Intent i = new Intent(getContext(), MainActivity.class);
        startActivity(i);
        getActivity().finish();
    }

    private void gotoLoginActivity() {
        Intent i = new Intent(getContext(),LoginActivity.class);
        startActivity(i);
        getActivity().finish();
    }

}