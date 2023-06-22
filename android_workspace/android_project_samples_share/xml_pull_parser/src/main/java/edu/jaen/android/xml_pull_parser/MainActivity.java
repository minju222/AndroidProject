package edu.jaen.android.xml_pull_parser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity_SCSA";
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.result);

        //https://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=1168064000
//        new MyAsyncTask().execute("https://www.hani.co.kr/rss/");
//        new MyAsyncTask().execute("https://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=1168064000");
        new XMLParserTask().execute("result.xml");

    }

    class MyAsyncTask extends AsyncTask<String, String, List<String>> {
        List<String> list = new ArrayList<>();

        @Override
        protected List<String> doInBackground(String... arg) {
            try {
                InputStream input = new URL(arg[0]).openConnection().getInputStream();
                Log.d(TAG, "connection ok....");
                BufferedReader buffR = new BufferedReader(new InputStreamReader(input));
                String msg;
                while ((msg = buffR.readLine()) != null) {
                    list.add(msg);
//                    Log.d(TAG, "read: " + msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return list;
        }

        protected void onPostExecute(List<String> result) {
            listView.setAdapter(new ArrayAdapter<String>(MainActivity.this,
                    android.R.layout.simple_list_item_1, result));
        }

    }


    class XMLParserTask extends AsyncTask<String, String, List<Check>> {

        List<Check> list = new ArrayList<>();
        XmlPullParser parser = Xml.newPullParser();

        protected List<Check> doInBackground(String... arg) {
            try {

                parser.setInput(getAssets().open(arg[0]), null);
//				parser.setInput(new URL(arg[0]).openConnection()
//						.getInputStream(), null);
                int eventType = parser.getEventType();
                Check ch = null;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String name = null;
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            name = parser.getName();
                            if (name.equalsIgnoreCase("Check")) {
                                ch = new Check();
                                ch.setCode(parser.getAttributeValue(0));
                            } else if (ch != null) {
                                if (name.equalsIgnoreCase("Clean")) {
                                    ch.setClean(parser.nextText());
                                } else if (name.equalsIgnoreCase("Ready")) {
                                    ch.setReady(parser.nextText());
                                } else if (name.equalsIgnoreCase("Response")) {
                                    ch.setResponse(parser.nextText());
                                } else if (name.equalsIgnoreCase("Request")) {
                                    // if(parser.next()!= -1)
                                    ch.setRequest(parser.nextText());
                                }
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            name = parser.getName();
                            if (name.equalsIgnoreCase("Check") && ch != null) {
                                list.add(ch);
                            }
                            break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {
                Log.e("MyPullActivity", e.getMessage(), e);
                throw new RuntimeException(e);
            }
            return list;
        }

        protected void onPostExecute(List<Check> result) {
            listView.setAdapter(new ArrayAdapter<Check>(MainActivity.this,
                    android.R.layout.simple_list_item_1, result));
        }

    }
}