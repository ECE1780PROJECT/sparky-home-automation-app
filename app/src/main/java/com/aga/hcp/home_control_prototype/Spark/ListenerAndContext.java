package com.aga.hcp.home_control_prototype.Spark;

import android.content.Context;

import com.aga.hcp.home_control_prototype.OnTaskCompleted;

class ListenerAndContext {

    private final OnTaskCompleted listener;
    private final Context context;

    ListenerAndContext(OnTaskCompleted listener, Context context){
        this.listener = listener;
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public OnTaskCompleted getListener() {
        return listener;
    }
}
