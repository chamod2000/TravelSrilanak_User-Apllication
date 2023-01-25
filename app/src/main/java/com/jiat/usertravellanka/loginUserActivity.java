package com.jiat.usertravellanka;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;





public class loginUserActivity extends AppCompatActivity {

    private static final String TAG = "Login";
    private FirebaseAuth firebaseAuth;
    private SignInClient signInClient;
    public static String name;
    public static String email;
    private LoginButton fblogin;
    CallbackManager callbackManager;

    private final ActivityResultLauncher<IntentSenderRequest> signLauncher = registerForActivityResult(
            new ActivityResultContracts.StartIntentSenderForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    handleSignInResult(result.getData());
                }
            });

    private void handleSignInResult(Intent intent) {
        try {
            SignInCredential credential = signInClient.getSignInCredentialFromIntent(intent);
            String idToken = credential.getGoogleIdToken();
            fireBaseAuthWithGoogle(idToken);


        }catch (ApiException e){
            e.printStackTrace();
            Log.i(TAG,e.getMessage());
        }

    }

    private void fireBaseAuthWithGoogle(String idToken) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(idToken, null);
        Task<AuthResult> authResultTask = firebaseAuth.signInWithCredential(authCredential);
        authResultTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    updateUi(user);
                    Log.i(TAG,"fail");
                }
            }

        });
    }
    private void updateUi(FirebaseUser user) {

        if(user != null){
            Intent intent = new Intent(loginUserActivity.this,MainActivity.class);
             startActivity(intent);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        ImageView imageView = findViewById(R.id.imageView3);
        Glide.with(this).load(R.drawable.loginn).override(300,300).into(imageView);

        //configure google signIn client
        signInClient = Identity.getSignInClient(getApplicationContext());

        //initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btnGoogleSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singIn();
            }
        });



        callbackManager = CallbackManager.Factory.create();
        fblogin = findViewById(R.id.fblogin);
        fblogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String imageURL = "https://graph.facebook.com/" + loginResult.getAccessToken().getUserId() + "/picture?return_ssl_resources=1";
                handleFacebookAccessToken(loginResult.getAccessToken());
                FirebaseUser user = firebaseAuth.getCurrentUser();
                SignIn(user);

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(@NonNull FacebookException e) {
                Log.i(TAG,e.getMessage());
                Toast.makeText(loginUserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    startActivity(new Intent(loginUserActivity.this,MainActivity.class));

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(loginUserActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void SignIn(FirebaseUser user) {
        if(user != null){
            Intent intent = new Intent(loginUserActivity.this, MainActivity.class);
            startActivity(intent);
        }else{
            Log.d(TAG, "signInWithEmail:success");
           // Toast.makeText(loginUserActivity.this,"Sign-In Failed",Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onBackPressed() {
       // super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        updateUi(user);
    }

    private void singIn(){
        GetSignInIntentRequest signInIntentRequest = GetSignInIntentRequest.builder()
                .setServerClientId(getString(R.string.web_client_id)).build();

        Task<PendingIntent> signInIntent = signInClient.getSignInIntent(signInIntentRequest);
        signInIntent.addOnSuccessListener(new OnSuccessListener<PendingIntent>() {
            @Override
            public void onSuccess(PendingIntent pendingIntent) {
                lounchingSignIn(pendingIntent);
            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void lounchingSignIn(PendingIntent pendingIntent) {

        IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(pendingIntent).build();
        signLauncher.launch(intentSenderRequest);
    }

//    private void oneTapSingIn(){
//        BeginSignInRequest oneTapRequest =  BeginSignInRequest.builder()
//                .setGoogleIdTokenRequestOptions(
//                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                                .setSupported(true)
//                                .setServerClientId(getString(R.string.web_client_id))
//                                .setFilterByAuthorizedAccounts(true)
//                                .build()
//                ).build();
//        Task<BeginSignInResult> beginSignInRequestTask = signInClient.beginSignIn(oneTapRequest);
//        beginSignInRequestTask.addOnSuccessListener(new OnSuccessListener<BeginSignInResult>() {
//            @Override
//            public void onSuccess(BeginSignInResult beginSignInResult) {
//                lounchingSignIn(beginSignInResult.getPendingIntent());
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
//
//    }
}