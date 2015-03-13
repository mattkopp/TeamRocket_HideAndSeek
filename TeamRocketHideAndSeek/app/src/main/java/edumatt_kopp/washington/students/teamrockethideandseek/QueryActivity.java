package edumatt_kopp.washington.students.teamrockethideandseek;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.net.UnknownHostException;


public class QueryActivity extends Activity {

    protected TextView queryLat;
    protected TextView queryLong;
    protected TextView queryTime;

    public String passLat;
    public String passLong;
    public String passTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        queryLat = (TextView) findViewById(R.id.latitude_query);
        queryLong = (TextView) findViewById(R.id.longitude_query);
        queryTime = (TextView) findViewById(R.id.time_query);

        QueryLocation querylocation = new QueryLocation();
        querylocation.execute();
    }



    private class QueryLocation extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                MongoClientURI uri = new MongoClientURI("mongodb://jessie:james@ds049631.mongolab.com:49631/teamrocket");
                MongoClient client = new MongoClient(uri);
                DB db = client.getDB(uri.getDatabase());

                DBCollection MyLatLong = db.getCollection("teamrocket");

                DBCursor cursor = MyLatLong.find().sort(new BasicDBObject("$natural", -1));


                passLat = String.valueOf(cursor.one().get("Latitude"));
                passLong = String.valueOf(cursor.one().get("Longitude"));
                passTime = String.valueOf(cursor.one().get("Time"));
                cursor.close();
                client.close();

                return "who cares?";
            }catch (UnknownHostException e) {
                return "Unknown Host Exception";}
        }

        @Override
        protected void onPostExecute(String result){
            // TEST THIS TO FIND ERROR - if passLat != <>
            queryLat.setText(passLat);
            queryLong.setText(passLong);
            queryTime.setText(passTime);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_query, menu);
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
    //lab 4B
    public void onQueryClick(View view) {
        Intent intent = new Intent(this, HideandSeekMap.class);
        startActivity(intent);
    }
}
