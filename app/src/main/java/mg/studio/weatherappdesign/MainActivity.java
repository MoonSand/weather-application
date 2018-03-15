package mg.studio.weatherappdesign;


import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
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


        Date date=new Date();
        SimpleDateFormat ft = new SimpleDateFormat("E");
        String temp=ft.format(date),week_day="SUNDAY";
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        int value=cal.get(Calendar.DAY_OF_WEEK);
        switch(value)
        {
            case 1: week_day="SUNDAY";break;
            case 2: week_day="MONDAY";break;
            case 3: week_day="TUESDAY";break;
            case 4: week_day="WEDNESDAY";break;
            case 5: week_day="THURSDAY";break;
            case 6: week_day="FRIDAY";break;
            case 7: week_day="SATURDAY";break;

            default: break;
        }

        ((TextView) findViewById(R.id.weekday)).setText(week_day);

        SimpleDateFormat time= new SimpleDateFormat("MM/dd/yyyy");
        ((TextView) findViewById(R.id.tv_date)).setText(time.format(date));

        new DownloadUpdate().execute();


    }


    private class DownloadUpdate extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = "http://mpianatra.com/Courses/info.txt";
            HttpURLConnection urlConnection = null;
            BufferedReader reader;

            try {
                URL url = new URL(stringUrl);

                // Create the request to get the information from the server, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Mainly needed for debugging
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                //The temperature
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
        protected void onPostExecute(String temperature) {
            //Update the temperature displayed
            ((TextView) findViewById(R.id.temperature_of_the_day)).setText(temperature);
        }
    }
}
