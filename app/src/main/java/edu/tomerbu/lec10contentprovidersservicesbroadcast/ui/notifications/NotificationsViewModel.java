package edu.tomerbu.lec10contentprovidersservicesbroadcast.ui.notifications;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;

import edu.tomerbu.lec10contentprovidersservicesbroadcast.ContactsService;
import edu.tomerbu.lec10contentprovidersservicesbroadcast.MContact;

//When you need a context in your ViewModel -> extend AndroidViewModel (instead of the plain ViewModel)
//AndroidViewModel handles context in a life-cycle aware way. (No memory leaks)

//AndroidViewModel has a method: getApplication ->context
public class NotificationsViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;

    //1) BroadcastReceiver: callback when there is a new broadcast
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            ArrayList<MContact> contacts =
                    (ArrayList<MContact>) intent.getSerializableExtra("data");
            //update the UI:
            mText.setValue(contacts.toString());
        }
    };

    public NotificationsViewModel(@NonNull Application application) {
        super(application);
        mText = new MutableLiveData<>();
        //intent = new Intent
        //instead of startActivity(intent) -> startService(intent)
        Intent intent = new Intent(getApplication(), ContactsService.class);
        getApplication().startService(intent);

        //listen to broadcasts:
        //1) BroadcastReceiver: Listener
        //2) LocalBroadcastManager bm
        LocalBroadcastManager bm = LocalBroadcastManager.getInstance(getApplication());

        //3) bm.registerReceiver(intent of "contacts")
        bm.registerReceiver(mReceiver,  new IntentFilter("contacts"));

    }

    public LiveData<String> getText() {
        return mText;
    }
}