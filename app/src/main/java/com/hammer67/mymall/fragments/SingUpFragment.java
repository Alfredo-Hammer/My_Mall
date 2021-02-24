package com.hammer67.mymall.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hammer67.mymall.activities.MainActivity;
import com.hammer67.mymall.R;

import java.util.HashMap;
import java.util.Map;

public class SingUpFragment extends Fragment {

    public SingUpFragment() {

    }

    private TextView tvAlready;
    private FrameLayout parentFramelayout;
    private EditText fullName, email, password, confirmPassword;
    private ImageButton imgClose;
    private Button btnRegister;
    private ProgressBar progressBar;

    ///////////FIREBASE DATABASE
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sing_up, container, false);

        tvAlready = view.findViewById(R.id.tvAlready);
        parentFramelayout = getActivity().findViewById(R.id.registerFrameLayout);
        fullName = view.findViewById(R.id.singUp_fullName);
        email = view.findViewById(R.id.singUp_email);
        password = view.findViewById(R.id.singUp_password);
        confirmPassword = view.findViewById(R.id.singUp_ConfirmPassword);
        imgClose = view.findViewById(R.id.imgClose);
        btnRegister = view.findViewById(R.id.btnRegister);
        progressBar = view.findViewById(R.id.progress_circular);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvAlready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragment(new SingInFragment());
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
        fullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                checkInputs();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

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
        confirmPassword.addTextChangedListener(new TextWatcher() {
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


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Subimos los datos del usuario a la base de datos Firebase
                checkEmailAndPassword();
            }
        });
    }

    private void getFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_rigth);
        fragmentTransaction.replace(parentFramelayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    //Verificacion de las entradas del refistro
    private void checkInputs() {
        if (!TextUtils.isEmpty(email.getText())) {
            if (!TextUtils.isEmpty(fullName.getText())) {
                if (!TextUtils.isEmpty(password.getText()) && password.length() >= 6) {
                    if (!TextUtils.isEmpty(confirmPassword.getText())) {
                        btnRegister.setEnabled(true);
                        btnRegister.setTextColor(Color.GREEN);
                    } else {
                        btnRegister.setEnabled(false);
                        btnRegister.setTextColor(Color.argb(50, 255, 255, 255));
                    }
                } else {
                    btnRegister.setEnabled(false);
                    btnRegister.setTextColor(Color.argb(50, 255, 255, 255));
                }
            } else {
                btnRegister.setEnabled(false);
                btnRegister.setTextColor(Color.argb(50, 255, 255, 255));
            }
        } else {
            btnRegister.setEnabled(false);
            btnRegister.setTextColor(Color.argb(50, 255, 255, 255));
        }
    }

    //Verifacion del correo y contraseña que sean validos
    private void checkEmailAndPassword() {
        Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_error);
        drawable.setBounds(0,0, drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());

        if (email.getText().toString().matches(emailPattern)) {
            if (password.getText().toString().equals(confirmPassword.getText().toString())) {

                progressBar.setVisibility(View.VISIBLE);
                btnRegister.setEnabled(false);
                btnRegister.setTextColor(Color.argb(50, 255, 255, 255));


                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //Guardando los datos en la base de datos
                                    Map<String, Object> userData = new HashMap<>();
                                    userData.put("fullname", fullName.getText().toString());

                                    firebaseFirestore.collection("USERS")
                                            .add(userData)
                                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                                    if (task.isSuccessful()){
                                                        mainIntent();

                                                    }else {
                                                        progressBar.setVisibility(View.GONE);
                                                        btnRegister.setEnabled(true);
                                                        btnRegister.setTextColor(Color.GREEN);
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(getContext(), "Ocurrio un error no se pudo guardar los datos del usuario " + error, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    btnRegister.setEnabled(true);
                                    btnRegister.setTextColor(Color.GREEN);
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getContext(), "Ocurrio un error no se pudo guardar los datos del usuario " + error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            } else {
                confirmPassword.setError("Las contraseñas no coenciden...revise por favor", drawable);
            }

        } else {
            email.setError("Correo electronico invalido", drawable);
        }
    }

    private void mainIntent(){
        Toast.makeText(getActivity(), "Registro realizado con exito...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

}