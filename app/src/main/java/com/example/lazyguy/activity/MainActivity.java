package com.example.lazyguy.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInstaller;
import android.se.omapi.Session;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lazyguy.R;
import com.example.lazyguy.adapter.HomeFragmentPagerAdapter;
import com.example.lazyguy.model.Datasheet;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;
    private ImageView imgavatar;
    private View headView;
    public ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar tbMain;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private TextView tvUserName, tvMail;
    private MenuItem itemLog, itemProfile;
    private Menu _menu;
    private GoogleSignInClient mGoogleSignInClient;

    String tkey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnhXa();

        //Cài đặt navigationview và drawer
        mNavigationView.setNavigationItemSelectedListener(this);
//        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
//        mDrawerLayout.addDrawerListener(mToggle);
//        mToggle.syncState();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Cài đặt 4 tabLayout với 4 fragment
        HomeFragmentPagerAdapter adapter = new HomeFragmentPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tbMain.setNavigationIcon(R.drawable.ic_menu);
        tbMain.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        //Đăng nhập đăng xuất tài khoản
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    tvUserName.setText("User name");
                    tvMail.setText("abc@gmail.com");
                    imgavatar.setImageResource(R.drawable.user);
                    itemLog.setVisible(false);
                    itemProfile.setTitle("Log in");
                } else {
                    String userName = firebaseAuth.getCurrentUser().getDisplayName();
                    String email = firebaseAuth.getCurrentUser().getEmail();
                    String imageUrl = firebaseAuth.getCurrentUser().getPhotoUrl().toString();
                    Picasso.get().load(imageUrl).into(imgavatar);
                    tvUserName.setText(userName);
                    tvMail.setText(email);
                    itemLog.setVisible(true);
                    itemProfile.setTitle("My profile");
                }
            }
        };

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Xét sự kiện avatar được nhấn vào
        imgavatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() == null) {
                    Toast.makeText(MainActivity.this, "You haven't sign in yet!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                }
            }
        });

        android.support.v7.preference.PreferenceManager
                .setDefaultValues(this, R.xml.preferences, false);
        android.support.v7.preference.PreferenceManager.setDefaultValues(this,
                R.xml.pref_general, false);
        android.support.v7.preference.PreferenceManager.setDefaultValues(this,
                R.xml.pref_notification, false);
        android.support.v7.preference.PreferenceManager.setDefaultValues(this,
                R.xml.pref_account, false);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String nickname = sharedPref.getString("edt_nickname", "");
        Toast.makeText(this, "Hi " + nickname, Toast.LENGTH_SHORT).show();

//        mDatabase.child("Datasheet").child("Beginner").child("Part 3").push().setValue(new Datasheet
//                ("", 5, 3, 1));

    }

    @Override
    protected void onStart() {
        //bắt sự kiện đăng nhập đăng xuất
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //bắt sự kiện click menu item trong drawer

        switch (menuItem.getItemId()) {
            case R.id.gym:
                startActivity(new Intent(MainActivity.this, GymLocationActivity.class));
                break;
            case R.id.remind:
                startActivity(new Intent(MainActivity.this, RemindActivity.class));
                break;
            case R.id.profile:
                if (mAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                }
                break;
            case R.id.logout:
                if (mAuth.getCurrentUser() != null) {
                    mAuth.signOut();

                    LoginManager.getInstance().logOut();
//                    FacebookSdk.sdkInitialize(getApplicationContext());
//                    if (AccessToken.getCurrentAccessToken() != null) {
//                        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
//                                .Callback() {
//                            @Override
//                            public void onCompleted(GraphResponse graphResponse) {
//                                SharedPreferences pref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
//                                SharedPreferences.Editor editor = pref.edit();
//                                editor.clear();
//                                editor.commit();
//                                LoginManager.getInstance().logOut();
//                            }
//                        }).executeAsync();
//                    }
//                    AccessToken.setCurrentAccessToken(null);

                    mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainActivity.this, "Log out", Toast.LENGTH_SHORT).show();
                        }
                    });
                    mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                }
                break;
            case R.id.res:
                startActivity(new Intent(MainActivity.this, ResponsibilityActivity.class));
                break;
            case R.id.aboutus:
                startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                break;
            case R.id.setting:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            case R.id.fb:
                String recipientList = "nqcchinhnguyenquoc@gmail.com, tekyteacher1@gmail.com";
                String []recipients = recipientList.split(",");
                String subject = "Feedback app LazyGuy";
                String message = "During the process of using your application, I have a problem ...";
                Intent mailIntent = new Intent(Intent.ACTION_SEND);
                mailIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
                mailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                mailIntent.putExtra(Intent.EXTRA_TEXT, message);
                mailIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(mailIntent, "Mail using"));
                break;
            case R.id.share:
                String shareSubject = "Share an useful app";
                String shareMessage = "Hey guy, take a look on LazyGuy App in CH Play ... https://play.google.com/store/apps/details?id=softin.my.fast.fitness";
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, "Share using"));
                break;
        }
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        //bắt sự kiện click title bar
//        if (mToggle.onOptionsItemSelected(item)){
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void AnhXa(){
        mDrawerLayout = findViewById(R.id.drawer);
        mNavigationView = findViewById(R.id.navigation_view);
        headView = mNavigationView.getHeaderView(0);
        imgavatar = headView.findViewById(R.id.imgAvatar);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.sliding_tabs);
        _menu = findViewById(R.id._menu);
        tvUserName = headView.findViewById(R.id.tvUserName);
        tvMail = headView.findViewById(R.id.tvMail);
        itemLog =   mNavigationView.getMenu().findItem(R.id.logout);
        itemProfile = mNavigationView.getMenu().findItem(R.id.profile);
        tbMain = findViewById(R.id.tbMain);
    }

}
