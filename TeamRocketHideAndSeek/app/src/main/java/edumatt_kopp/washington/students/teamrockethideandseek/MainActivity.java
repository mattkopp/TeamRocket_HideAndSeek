package edumatt_kopp.washington.students.teamrockethideandseek;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    protected GoogleApiClient mGoogleApiClient;

    protected Location mLastLocation;
    protected TextView mLatitudeText;
    protected TextView mLongitudeText;

    private ShareActionProvider mShareActionProvider;
    protected String mShare;

    protected static final String TAG = "Finding Location...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLatitudeText = (TextView) findViewById(R.id.latitude_text);
        mLongitudeText = (TextView) findViewById(R.id.longitude_text);

        buildGoogleApiClient();
    }
    //Lab 4A
    public void onPostClick(View view) {
        PostLocation postlocation = new PostLocation();
        postlocation.execute();
        Toast.makeText(this, "Coordinates Submitted!", Toast.LENGTH_SHORT).show();
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));

            mShare = "I'm at " + String.valueOf(mLastLocation.getLatitude())
                    + " degrees latitude and " + String.valueOf(mLastLocation.getLongitude())
                    + " degrees longitude.";
        } else {
            Toast.makeText(this, "No location detected!", Toast.LENGTH_LONG).show();
            mShare = "No location!";
        }
    }



    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection Failed: ConnectionResult.GetErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //return true;
        mShareActionProvider = (ShareActionProvider) menu.findItem(R.id.share).getActionProvider();
        mShareActionProvider.setShareIntent(getDefaultShareIntent());
        return true;
    }

    private Intent getDefaultShareIntent(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, mShare);
        return intent;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //lab 4A
    private class PostLocation extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... voids) {
            try {
                //MongoClientURI uri = new MongoClientURI("mongodb://mattkopp:UWTacoma504@ds045001.mongolab.com:45001/locationlatlong");
                MongoClientURI uri = new MongoClientURI("mongodb://jessie:james@ds049631.mongolab.com:49631/teamrocket");

                MongoClient client = new MongoClient(uri);
                DB db = client.getDB(uri.getDatabase());

                DBCollection MyLatLong = db.getCollection("teamrocket");

                SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
                String now = time.format(new Date());

                if (mLastLocation != null) {
                    BasicDBObject LastLocation = new BasicDBObject();
                    LastLocation.put("Latitude", String.valueOf(mLastLocation.getLatitude()));
                    LastLocation.put("Longitude", String.valueOf(mLastLocation.getLongitude()));
                    LastLocation.put("Time", String.valueOf(now));
                    MyLatLong.insert(LastLocation);
                    client.close();
                    return "Coordinates submitted!";
                } else {
                    client.close();
                    return "No location detected, no location submitted!";
                }


            } catch (UnknownHostException e) {
                return "Unknown Host Exception";}
        }

    }

    //lab 4B
    public void onQueryClick(View view){
        Intent intent = new Intent(this, QueryActivity.class);
        startActivity(intent);
    }
}

