package edumatt_kopp.washington.students.teamrockethideandseek;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Seek_activity extends Activity {

    protected TextView queryLat;
    protected TextView queryLong;
    protected TextView queryTime;

    protected Location mLastLocation;
    protected TextView mLatitudeText;
    protected TextView mLongitudeText;

    public String passLat;
    public String passLong;
    public String passTime;

    public String hideLat;
    public String hideLong;
    public String hideTime;

    public String seekLat;
    public String seekLong;
    public String seekTime;

    public Double yourguess;
    public Double lastguess;
    public Double hideloc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek_activity);

//        queryLat = (TextView) findViewById(R.id.latitude_query);
//        queryLong = (TextView) findViewById(R.id.longitude_query);
//        queryTime = (TextView) findViewById(R.id.time_query);

        mLatitudeText = (TextView) findViewById(R.id.latitude_text);
        mLongitudeText = (TextView) findViewById(R.id.longitude_text);

//        QueryLocation querylocation = new QueryLocation();
//    querylocation.execute();
}



//    private class QueryLocation extends AsyncTask<Void, Void, String> {
//        @Override
//        protected String doInBackground(Void... voids) {
//            try {
//                MongoClientURI uri = new MongoClientURI("mongodb://jessie:james@ds049631.mongolab.com:49631/teamrocket");
//                MongoClient client = new MongoClient(uri);
//                DB db = client.getDB(uri.getDatabase());
//
//                DBCollection MyLatLong = db.getCollection("Seeker");
//
//                DBCursor cursor = MyLatLong.find().sort(new BasicDBObject("$natural", -2));
//
//
//                passLat = String.valueOf(cursor.one().get("Latitude"));
//                passLong = String.valueOf(cursor.one().get("Longitude"));
//                passTime = String.valueOf(cursor.one().get("Time"));
//                cursor.close();
//                client.close();
//
//                return "who cares?";
//            }catch (UnknownHostException e) {
//                return "Unknown Host Exception";}
//        }

//        @Override
//        protected void onPostExecute(String result){
//            // TEST THIS TO FIND ERROR - if passLat != <>
//            queryLat.setText(passLat);
//            queryLong.setText(passLong);
//            queryTime.setText(passTime);
//        }
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_seek_activity, menu);
        return true;
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

    private class PostLocationHide extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... voids) {
            try {
                //MongoClientURI uri = new MongoClientURI("mongodb://mattkopp:UWTacoma504@ds045001.mongolab.com:45001/locationlatlong");
                MongoClientURI uri = new MongoClientURI("mongodb://jessie:james@ds049631.mongolab.com:49631/teamrocket");

                MongoClient client = new MongoClient(uri);
                DB db = client.getDB(uri.getDatabase());

                DBCollection MyLatLong = db.getCollection("Hider");
                DBCursor hidecur = MyLatLong.find().sort(new BasicDBObject("$natural", -1));

                hideLat = String.valueOf(hidecur.one().get("Latitude"));
                hideLong = String.valueOf(hidecur.one().get("Longitude"));
                hideTime = String.valueOf(hidecur.one().get("Time"));
                hidecur.close();
                client.close();

                return "who cares?";
            }catch (UnknownHostException e) {
                return "Unknown Host Exception";}
        }

    }

    private class PostLocationSeek extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... voids) {
            try {
                //MongoClientURI uri = new MongoClientURI("mongodb://mattkopp:UWTacoma504@ds045001.mongolab.com:45001/locationlatlong");
                MongoClientURI uri = new MongoClientURI("mongodb://jessie:james@ds049631.mongolab.com:49631/teamrocket");

                MongoClient client = new MongoClient(uri);
                DB db = client.getDB(uri.getDatabase());

                DBCollection MyLatLong = db.getCollection("Seeker");

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
                return "Unknown Host Exception";
            }
        }

    }

    private class QuerylocationSeek extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... voids) {
            try {
                //MongoClientURI uri = new MongoClientURI("mongodb://mattkopp:UWTacoma504@ds045001.mongolab.com:45001/locationlatlong");
                MongoClientURI uri = new MongoClientURI("mongodb://jessie:james@ds049631.mongolab.com:49631/teamrocket");

                MongoClient client = new MongoClient(uri);
                DB db = client.getDB(uri.getDatabase());

                DBCollection MyLatLong = db.getCollection("Seeker");
                DBCursor seekcur = MyLatLong.find().sort(new BasicDBObject("$natural", -2));

                passLat = String.valueOf(seekcur.one().get("Latitude"));
                passLong = String.valueOf(seekcur.one().get("Longitude"));
                passTime = String.valueOf(seekcur.one().get("Time"));

                seekcur.next();

                seekLat = String.valueOf(seekcur.one().get("Latitude"));
                seekLong = String.valueOf(seekcur.one().get("Longitude"));
                seekTime = String.valueOf(seekcur.one().get("Time"));

                seekcur.close();
                client.close();

                return "who cares?";
            }catch (UnknownHostException e) {
                return "Unknown Host Exception";}
        }

    }

    public void ontakeaguessClick(View view) {

        PostLocationSeek postlocationseek = new PostLocationSeek();
        postlocationseek.execute();
    }

    public void oncheckguessClick(View view) {

        QuerylocationSeek querylocationseek = new QuerylocationSeek();
        querylocationseek.execute();
        Location yourguess = new Location(" ");
            yourguess.setLatitude(Double.valueOf(seekLat));
            yourguess.setLongitude(Double.valueOf(seekLong));
        Location lastguess = new Location(" ");
            lastguess.setLatitude(Double.valueOf(passLat));
            lastguess.setLongitude(Double.valueOf(passLong));
        Location hideloc = new Location(" ");
            hideloc.setLatitude(Double.valueOf(hideLat));
            hideloc.setLongitude(Double.valueOf(hideLong));
        if (yourguess.distanceTo(hideloc) > lastguess.distanceTo(hideloc)) {
            Toast.makeText(this, "You are getting colder", Toast.LENGTH_SHORT).show();
        } else if (yourguess.distanceTo(hideloc) > lastguess.distanceTo(hideloc)) {
            Toast.makeText(this, "You are getting warmer", Toast.LENGTH_SHORT).show();
        } else if (yourguess.distanceTo(hideloc) < 5) {
            Toast.makeText(this, "You found the bastard!!!", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "WTF are you doing?!?", Toast.LENGTH_SHORT).show();
    }
//    Toast.makeText(this, "You are now seeking", Toast.LENGTH_SHORT).show();
}
