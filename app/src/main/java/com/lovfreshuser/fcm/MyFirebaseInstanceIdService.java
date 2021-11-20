package com.lovfreshuser.fcm;


import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    //this method will be called
    //when the token is generated
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        //now we will have the token
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.e("MyRefreshedToken", token);
    }


}
