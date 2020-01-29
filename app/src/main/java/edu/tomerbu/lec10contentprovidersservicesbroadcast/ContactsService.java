package edu.tomerbu.lec10contentprovidersservicesbroadcast;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * //1) intent service runs in the background
 * //2) services are not UI Components: (NO layout, NO View)
 * //3) services may be used by any activity / all activities.
 * <p>
 * //very similar to asynctask (but it has  context, it can run as app)
 */
public class ContactsService extends IntentService {
    //required empty constructor
    public ContactsService() {
        super("ContactsService");
    }


    //when the service is called:
    @Override
    protected void onHandleIntent(Intent intent) {
        //access the content provider:
        //1) URI -> address of the provider.
        Uri uri = ContactsContract.Contacts.CONTENT_URI; //address of the content provider:
        //2) ContentResolver -> android object that helps us consume content providers.
        ContentResolver contentResolver = getContentResolver();
        //3) ContentResolver.query() -> a request

        //in java we had a ResultSet - > in android it's the cursor object
        //TODO: close the cursor:

        //only name, id:
        String[] projection = {ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID};
        //projection = the column name you want -> null = all the columns)

        //SELECT * FROM TABLE WHERE DISPLAY_NAME = 'Moshe'
        String selectionExample = ContactsContract.Contacts.DISPLAY_NAME + "=?";
        String[] argsExample = {"Moshe"};
        ArrayList<MContact> mContacts = new ArrayList<>();
        try (Cursor cursor = contentResolver.query(uri, projection, null, null, ContactsContract.Contacts.DISPLAY_NAME)) {
            //4) read the results (similar to SQL)
            if (cursor == null) return;
            int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            int idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            //loop over the cursor:
            while (cursor.moveToNext()) {
                //1) get the id of the contact
                //2) get the name of the contact
                String id = cursor.getString(idIndex);
                String name = cursor.getString(nameIndex);
                //3) accumulate all the data in a list:
                List<String> emails = getEmails(id);
                List<String> phones = getPhones(id);
                MContact c = new MContact(name, id, phones, emails);
                mContacts.add(c);
            }
        }
        //report back to the fragment:
        //BROADCAST:
        //1)
        LocalBroadcastManager bm = LocalBroadcastManager.getInstance(this);//service is also a context
        //2)
        Intent contactIntent = new Intent("contacts");
        contactIntent.putExtra("data", mContacts);
        //3)
        bm.sendBroadcast(contactIntent);
        //put extra (int, string, double, cant put extra Object)
        //in order to send Custom Object in the putExtra:
        //Your custom Object must implement an interface Serializable

    }

    //separate URI for the phones
    private List<String> getPhones(String id) {
        List<String> phones = new ArrayList<>();

        Uri phonesUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        //only get phones for this ID:
        String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?";
        String[] args = {id};

        try (Cursor cursor = getContentResolver().query(phonesUri, null, selection, args, null)) {
            if (cursor == null) return phones;
            //the index of the phone column:
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            while (cursor.moveToNext()) {
                String number = cursor.getString(phoneIndex);
                phones.add(number);
            }
        }
        return phones;
    }

    private List<String> getEmails(String id) {
        List<String> emails = new ArrayList<>();

        Uri emailUri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;

        //only get phones for this ID:
        String selection = ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?";
        String[] args = {id};

        try (Cursor cursor = getContentResolver().query(emailUri, null, selection, args, null)) {
            if (cursor == null) return emails;
            //the index of the phone column:
            int emailIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);

            while (cursor.moveToNext()) {
                String email = cursor.getString(emailIndex);
                emails.add(email);
            }
        }
        return emails;
    }


}
