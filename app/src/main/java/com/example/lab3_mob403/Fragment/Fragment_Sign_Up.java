package com.example.lab3_mob403.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.lab3_mob403.APIServer.ServerResponseAccount;
import com.example.lab3_mob403.Interface.AccountInterfaceAPI;
import com.example.lab3_mob403.Model.Account;
import com.example.lab3_mob403.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Fragment_Sign_Up extends Fragment {
    EditText edName, edEmail, edPassword;
    TextView tvLogin;
    Button btnSignUp;
    private String urlBase = "https://tucaomypham.000webhostapp.com/android_networking_mob403/";
    ProgressDialog progressDialog;
    private static final String TAG = Fragment_Sign_Up.class.getSimpleName();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__sign__up, container, false);

        initViewByID(view);

        initClickListener();

        initProgressBar();

        return view;
    }

    private void initProgressBar() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Vui lòng chờ ... ");
        progressDialog.setCancelable(false);
    }

    private void initClickListener() {
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new Fragment_Login());

            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edName.getText().toString();
                String password = edPassword.getText().toString();
                String email = edEmail.getText().toString();
                if (name.length() != 0 && password.length() != 0 && email.length() != 0) {
                    showProgress();
                    insertDataProcess(name, password, email);

                } else {
                    hideProgress();
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ dự liệu !", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view_tag, fragment);
        fragmentTransaction.commit();
    }

    private void initViewByID(View view) {
        edEmail = view.findViewById(R.id.ed_email);
        edName = view.findViewById(R.id.ed_name);
        edPassword = view.findViewById(R.id.ed_password);
        tvLogin = view.findViewById(R.id.tv_login);
        btnSignUp = view.findViewById(R.id.btn_signup);

    }

    private void showProgress() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void hideProgress() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void insertDataProcess(String name, String password, String email) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlBase)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AccountInterfaceAPI accountInterfaceAPI = retrofit.create(AccountInterfaceAPI.class);
        Account account = new Account(name, email, password);
        Call<ServerResponseAccount> call = accountInterfaceAPI.insertAccount(account.getName(), account.getEmail(), account.getEncrypted_password());
        call.enqueue(new Callback<ServerResponseAccount>() {
            @Override
            public void onResponse(Call<ServerResponseAccount> call, Response<ServerResponseAccount> response) {
                ServerResponseAccount serverResponseAccount = response.body();
                Toast.makeText(getContext(), serverResponseAccount.getMessage(), Toast.LENGTH_LONG).show();
                hideProgress();

            }

            @Override
            public void onFailure(Call<ServerResponseAccount> call, Throwable t) {
                hideProgress();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, t.getMessage());
            }
        });

    }
}