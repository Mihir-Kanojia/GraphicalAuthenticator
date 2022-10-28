package com.example.graphicalauthenticator.managers;

import android.app.Activity;
import android.content.Intent;

public class ActivitySwitchManager {

    Activity activity;
    Class newActivity;
    Intent mMenuIntent;

    public ActivitySwitchManager(Activity activity, Class newActivity) {
        this.activity = activity;
        this.newActivity = newActivity;
        mMenuIntent = new Intent(activity, newActivity);
    }

    public void openActivity() {
        mMenuIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(mMenuIntent);
        activity.finish();
    }

    public void openActivityWithoutFinish() {
        activity.startActivity(mMenuIntent);
    }

}
