package me.dong.exfacebookoauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    TextView mTextView;
    LoginButton mLoginButton;
    Button mBtnFbLoginCustom, mBtnFbLogout;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.tv);
        mBtnFbLoginCustom = (Button) findViewById(R.id.btn_fb_login_custom);
        mLoginButton = (LoginButton) findViewById(R.id.btn_fb_login);
        mBtnFbLogout = (Button) findViewById(R.id.btn_fb_logout);

        mBtnFbLoginCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginButton.performClick();
            }
        });
        mBtnFbLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();  // logout
            }
        });

        callbackManager = CallbackManager.Factory.create();
        mLoginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends", "user_birthday"));

        // LoginManager또는 LoginButton에 callback 등록
        mLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                final String token = accessToken.getToken();
                final String userId = accessToken.getUserId();

                Profile.fetchProfileForCurrentAccessToken();
                Profile profile = Profile.getCurrentProfile();
                final String userName = profile.getName();
                final String firstName = profile.getFirstName();
                final String middleName = profile.getMiddleName();
                final String lastName = profile.getLastName();

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.e(TAG, response.toString());

                                try {
                                    String email = object.getString("email");
                                    String birthday = object.getString("birthday");

                                    mTextView.setText(String.format("token: %s\nuserId: %s\nemail: %s\n" +
                                                    "birthday: %s\nuserName: %s, %s, %s, %s",
                                            token, userId, email, birthday, userName, firstName, middleName, lastName));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 로그인 결과 전달
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
