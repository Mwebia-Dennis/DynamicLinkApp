package com.penguinstech.dynamiclink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn).setOnClickListener(v -> {

            generateDynamicLink(new User("DENNIS",  "dfdf", "Gitonga", "software developer and data scientist","Nairobi", "Kenya",
                    "jhgjkhkjh","project manager","ggg@mail.com","https://firebasestorage.googleapis.com/v0/b/chattie-23e2d.appspot.com/o/profile%20images%2FNpj4XTd5DQPGFFOEBEe2xR1xkKA3.jpg?alt=media&token=a218a7de-8641-42ce-81ca-156a8d9b99f1","jhgjkhkjh",
                    "developer","jhgjkhkjh","jhgjkhkjh","jhgjkhkjh","jhgjkhkjh","jhgjkhkjh", "sfsfds"));
        });

        Uri data = getIntent().getData();
        if(data != null) {
            Toast.makeText(this, data.toString(), Toast.LENGTH_SHORT).show();
            Log.i("data", data.toString());
            String name = data.getQueryParameter("name");
            String age = data.getQueryParameter("age");
            String description = data.getQueryParameter("description");
            TextView details_et = findViewById(R.id.details_et);
            details_et.setText("Name = "+name+"\n age= "+age+"\n decription = "+description);
        }

        receiveDynamicLink();
        
    }

    private void generateDynamicLink(User user) {

        try {

            Log.i("encoded data", getEncodedUser(user));
            String deepLink = "http://www.penguins.com/profile?user="+getEncodedUser(user);
            String redirectLink = "http://localhost/web-dev/demo-form/test.php?user="+getEncodedUser(user);

            String link = getLink(deepLink, "com.penguinstech.dynamiclink", redirectLink);
            Log.i("link", link);
            FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLongLink(Uri.parse(link))
                    .buildShortDynamicLink()
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            if(shortLink != null){
//                        Uri flowchartLink = task.getResult().getPreviewLink();
                                EditText url_et = findViewById(R.id.url_et);
                                url_et.setText(shortLink.toString());

                            }
                        } else {
                            // Error
                            Toast.makeText(this, "could not generate the url", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> {

                        Toast.makeText(this, "could not generate the url", Toast.LENGTH_SHORT).show();
                    });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong, check your url", Toast.LENGTH_SHORT).show();
        }


    }

    private String getLink(String deepLink, String androidId, String redirectLink) throws Exception {
        String link = "https://penguinstech.page.link/";
        if(deepLink == null) {
            throw new Exception("invalid deep-link url.");
        }else {
            link +="?link="+deepLink;
            if(androidId != null && !androidId.equals("")){
                //redirect to playstore
                link += "&apn="+androidId;
            }

            //redirect to web page if link is opened on ios or ipad
            link += "&ifl="+redirectLink;
            link += "&ipfl="+redirectLink;
            //redirect to web page if on other platform other than ios and android
            link += "&ofl="+redirectLink;
        }

        return link;
    }


    private void receiveDynamicLink(){
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, pendingDynamicLinkData -> {
                    // Get deep link from result (may be null if no link is found)
                    Uri deepLink = null;
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.getLink();
                        Log.i("deeplink", deepLink.toString());
                        Toast.makeText(this, deepLink.toString(), Toast.LENGTH_SHORT).show();
                    }


                    // Handle the deep link. For example, open the linked
                    // content, or apply promotional credit to the user's
                    // account.
                    // ...

                    // ...
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("dynamic link", "getDynamicLink:onFailure", e);
                    }
                });
    }
    
    private String getEncodedUser(User user) throws JSONException, UnsupportedEncodingException {

        JSONObject obj,socials;
        obj = new JSONObject();
        socials = new JSONObject();
        obj.put("firstname", user.firstname);
        obj.put("about", user.about);
        obj.put("surname", user.surname);
        obj.put("city", user.city);
        obj.put("country", user.country);
        obj.put("uid", user.uid);
        obj.put("cname", user.cname);
        obj.put("role", user.role);
        obj.put("primaryEmail", user.primaryEmail);
        obj.put("profile", user.profile);
        obj.put("search", user.search);
        obj.put("userindustry", user.userindustry);
        obj.put("socials", user.profile);

        socials.put("facebook", user.facebook);
        socials.put("youtube", user.youtube);
        socials.put("website", user.website);
        socials.put("insta", user.insta);
        socials.put("linkedIn", user.linkedIn);
        socials.put("twitter", user.twitter);

        obj.put("socials", socials);

        byte[] encoded = Base64.encode(obj.toString().getBytes("CP1252"), Base64.DEFAULT);
        return new String(encoded, "CP1252");
    }


}