package com.hammer67.mymall.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.hammer67.mymall.R;
import com.hammer67.mymall.activities.RegisterActivity;

public class RessetPasswordFragment extends Fragment {

    private EditText tvForgotPassword;
    private Button btnRessetPassword;
    private ImageButton btnBack;
    private FrameLayout parentFramelayout;
    ////////////////////////////////////
    private ProgressBar progressBar;
    private TextView emailIconText;
    private ImageView emailIcon;
    private ViewGroup emailIconContainer;

    private FirebaseAuth firebaseAuth;

    public RessetPasswordFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resset_password, container, false);

        tvForgotPassword = view.findViewById(R.id.etForgot);
        btnRessetPassword = view.findViewById(R.id.btnRessetPassword);
        btnBack = view.findViewById(R.id.btnBack);
        parentFramelayout = getActivity().findViewById(R.id.registerFrameLayout);
        emailIconContainer = view.findViewById(R.id.emailIconContainer);
        emailIcon = view.findViewById(R.id.email_icon);
        emailIconText = view.findViewById(R.id.email_icon_text);
        progressBar = view.findViewById(R.id.progressBar_horizontal);

        firebaseAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragment(new SingInFragment());
            }
        });

        btnRessetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TransitionManager.beginDelayedTransition(emailIconContainer);
                emailIconText.setVisibility(View.GONE);

                TransitionManager.beginDelayedTransition(emailIconContainer);
                emailIcon.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                btnRessetPassword.setEnabled(false);
                btnRessetPassword.setTextColor(Color.argb(50, 255, 255, 255));

                firebaseAuth.sendPasswordResetEmail(tvForgotPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "El correo de instruciones se envio correctamente, revise su bandeja de entrada", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getActivity(), RegisterActivity.class));
                                    getActivity().finish();


                                } else {
                                    String error = task.getException().getMessage();
                                    btnRessetPassword.setEnabled(true);
                                    btnRessetPassword.setTextColor(Color.rgb(255,255,255));

                                    emailIconText.setText(error);
                                    emailIconText.setTextColor(Color.RED);
                                    TransitionManager.beginDelayedTransition(emailIconContainer);
                                    emailIconText.setVisibility(View.VISIBLE);

                                }
                                progressBar.setVisibility(View.GONE);

                            }
                        });
            }
        });

        tvForgotPassword.addTextChangedListener(new TextWatcher() {
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
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_rigth);
        fragmentTransaction.replace(parentFramelayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs() {
        if (TextUtils.isEmpty(tvForgotPassword.getText())) {
            btnRessetPassword.setEnabled(false);
            btnRessetPassword.setTextColor(Color.GREEN);
        } else {
            btnRessetPassword.setEnabled(true);
            btnRessetPassword.setTextColor(Color.rgb(255, 255, 255));
        }
    }
}