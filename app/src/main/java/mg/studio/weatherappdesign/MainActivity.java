package mg.studio.weatherappdesign;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    public void refreshClick(View view) {
        boolean isAvailable=false;
        try{
            ConnectivityManager connManager = (ConnectivityManager) this
                    .getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=connManager.getActiveNetworkInfo();
          isAvailable=networkInfo.isAvailable();
        }catch(Exception e)
        {
            e.printStackTrace();
        }

        if(isAvailable) {
            Date date = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("E");
            String temp = ft.format(date), week_day = "SUNDAY";
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int value = cal.get(Calendar.DAY_OF_WEEK);
            switch (value) {
                case 1:
                    week_day = "SUNDAY";
                    break;
                case 2:
                    week_day = "MONDAY";
                    break;
                case 3:
                    week_day = "TUESDAY";
                    break;
                case 4:
                    week_day = "WEDNESDAY";
                    break;
                case 5:
                    week_day = "THURSDAY";
                    break;
                case 6:
                    week_day = "FRIDAY";
                    break;
                case 7:
                    week_day = "SATURDAY";
                    break;

                default:
                    break;
            }

            ((TextView) findViewById(R.id.weekday)).setText(week_day);

            SimpleDateFormat time = new SimpleDateFormat("MM/dd/yyyy");
            ((TextView) findViewById(R.id.tv_date)).setText(time.format(date));

            new DownloadUpdate().execute();

        }else {
             Toast.makeText(MainActivity.this,"当前网络连接不可用\nCurrent network connection is unavailable",Toast.LENGTH_LONG).show();
        }

    }


    private class DownloadUpdate extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {


//            HttpURLConnection urlConnection = null;
            BufferedReader reader;

            try {
                String city = java.net.URLEncoder.encode("重庆", "utf-8");
                String stringUrl = String.format("https://www.sojson.com/open/api/weather/json.shtml?city=%s",city);

                URL url = new URL(stringUrl);

                // Create the request to get the information from the server, and open the connection
                URLConnection urlConnection =  url.openConnection();
               // urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Mainly needed for debugging
                    buffer.append(line );
                }
                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String json) {
            //Update the temperature did
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONObject jsonObject1=jsonObject.getJSONObject("data");

            if(jsonObject.getString("status").equals("200")){        //success to get data
                ((TextView) findViewById(R.id.temperature_of_the_day)).setText(jsonObject1.getString("wendu"));
            }else{

                Toast.makeText(MainActivity.this,"获取数据失败\nFailed to get data",Toast.LENGTH_LONG).show();

            }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }
}
