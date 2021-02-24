package com.hammer67.mymall.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hammer67.mymall.activities.MainActivity;
import com.hammer67.mymall.R;

public class SingInFragment extends Fragment {

    public SingInFragment() {
        // Required empty public constructor
    }

    private TextView tvRegister,forgot_password;
    private FrameLayout parentFramelayout;
    private EditText email, password;
    private Button btnLogin;
    private ImageButton imgClose;
    private ProgressBar progressBar;


    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sing_in, container, false);
        tvRegister = view.findViewById(R.id.tvRegister);

        parentFramelayout = getActivity().findViewById(R.id.registerFrameLayout);
        email = view.findViewById(R.id.singIn_email);
        password = view.findViewById(R.id.singIn_password);
        btnLogin = view.findViewById(R.id.btnLogin);
        imgClose = view.findViewById(R.id.imgClose);
        progressBar = view.findViewById(R.id.progress_circular);
        forgot_password = view.findViewById(R.id.singIn_forgot_password);

        firebaseAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragment(new SingUpFragment());
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailAndPassword();
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onRessetPasswordFragment = true;
                getFragment(new RessetPasswordFragment());
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFramelayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    //Validacion de las entradas de email y contraseña
    private void checkInputs() {
        if (!TextUtils.isEmpty(email.getText())){
            if(!TextUtils.isEmpty(password.getText())){
                btnLogin.setEnabled(true);
                btnLogin.setTextColor(Color.GREEN);
            }
            else{
                btnLogin.setEnabled(false);
                btnLogin.setTextColor(Color.argb(50,255,255,255));
            }
        }
        else{
            btnLogin.setEnabled(false);
            btnLogin.setTextColor(Color.argb(50,255,255,255));
        }
    }

    private void checkEmailAndPassword(){
        if (email.getText().toString().matches(emailPattern)){
            if(password.length() >= 6){
                progressBar.setVisibility(View.VISIBLE);
                btnLogin.setEnabled(false);
                btnLogin.setTextColor(Color.argb(50,255,255,255));

                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    mainIntent();
                                }
                                else{
                                    progressBar.setVisibility(View.GONE);
                                    btnLogin.setEnabled(true);
                                    btnLogin.setTextColor(Color.rgb(255,255,255));
                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(getActivity(), "Ocurrio un error "+ errorMessage , Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }else{
                Toast.makeText(getActivity(), "El correo o la contraseña es incorrecta, verifique por favor", Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(getActivity(), "El correo o la contraseña es incorrecta, verifique por favor", Toast.LENGTH_SHORT).show();

        }
    }
    private void mainIntent(){
        Toast.makeText(getActivity(), "Sesion iniciado satisfactoriamente", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}