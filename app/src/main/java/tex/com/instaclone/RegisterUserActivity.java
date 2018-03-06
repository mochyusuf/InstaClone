package tex.com.instaclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterUserActivity extends AppCompatActivity {
    EditText edit_email_register, edit_password_register;
    TextView createAccount;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        edit_email_register = (EditText)findViewById(R.id.edit_email_register);
        edit_password_register = (EditText)findViewById(R.id.edit_password_register);
        createAccount = (TextView)findViewById(R.id.text_create);

        mProgressDialog = new ProgressDialog(this);
        createAccount.setOnClickListener((new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mProgressDialog.setTitle("Create Account");
                mProgressDialog.setMessage("Please Wait");
                mProgressDialog.show();
                CreateUserAcount();
            }
        }));

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null){
                    ToHome();
                }
            }
        };

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop(){
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    public void ToHome(){
        Intent moveToHome = new Intent(RegisterUserActivity.this, InstaHome.class);
        moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(moveToHome);
    }

    public void CreateUserAcount(){
        String email,password;

        email = edit_email_register.getText().toString().trim();
        password = edit_password_register.getText().toString().trim();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(RegisterUserActivity.this, "Account Created Succesful", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                        ToHome();     }
                    else{
                        Toast.makeText(RegisterUserActivity.this, "Account Created Failed", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                    }
                }
            });
        }
    }
}
