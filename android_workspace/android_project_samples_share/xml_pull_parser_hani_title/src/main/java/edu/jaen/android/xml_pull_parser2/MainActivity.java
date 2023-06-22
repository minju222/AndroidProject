package edu.jaen.android.xml_pull_parser2;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.jaen.android.xml_pull_parser2.databinding.RowBinding;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity_SCSA";
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.result);

        new MyAsyncTask().execute("https://www.hani.co.kr/rss/");
    }

    class MyAsyncTask extends AsyncTask<String, String, List<HaniItem>> {
        List<HaniItem> list = new ArrayList<>();

        @Override
        protected List<HaniItem> doInBackground(String... arg) {
            try {
                InputStream input = new URL(arg[0]).openConnection().getInputStream();
                Log.d(TAG, "connection ok....");

                parsing(new BufferedReader(new InputStreamReader(input)));
            } catch (Exception e) {
                e.printStackTrace();
            }

            return list;
        }

        protected void onPostExecute(List<HaniItem> result) {
            MyAdapter adapter = new MyAdapter();
            adapter.setList(result);
            listView.setAdapter(adapter);
        }

        XmlPullParser parser = Xml.newPullParser();
        private void parsing(Reader reader) throws Exception {
            parser.setInput(reader);
            Log.d(TAG, "parsing: " + parser.getNamespace());
            int eventType = parser.getEventType();
            HaniItem item = null;
            long id = 0;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name = null;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase("item")) {
                            item = new HaniItem();
                            item.id = ++id;
                        } else if (item != null) {
                            if (name.equalsIgnoreCase("title")) {
                                item.title = parser.nextText();
                            } else if (name.equalsIgnoreCase("link")) {
                                item.link = parser.nextText();
                            } else if (name.equalsIgnoreCase("description")) {
                                item.description = parser.nextText();
                            } else if (name.equalsIgnoreCase("pubDate")) {
                                item.pubDate = new Date(parser.nextText());
                            } else if("dc".equalsIgnoreCase(parser.getPrefix())){   // namespace
                                if (name.equalsIgnoreCase("subject")) {
                                    item.subject = parser.nextText();
                                }else if (name.equalsIgnoreCase("category")) {
                                    item.category = parser.nextText();
                                }
                            }
//                            else if (name.equalsIgnoreCase("subject")) { // namespace를 무시해도 이슈없음.
//                                item.subject = parser.nextText();
//                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase("item") && item != null) {
                            list.add(item);
                        }
                        break;
                }
                eventType = parser.next();
            }
        }
    }


    class MyAdapter extends BaseAdapter {
        List<HaniItem> list;

        boolean TTSSupoorted = false;

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;

            if (convertView == null) {
                RowBinding binding = RowBinding.inflate(LayoutInflater.from(MainActivity.this));
//                    RowLinearBinding binding = RowLinearBinding.inflate(LayoutInflater.from(MainActivity.this));
                convertView = binding.getRoot();

                holder = new ViewHolder();
                holder.title = binding.title;
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            HaniItem item = list.get(position);

            holder.title.setText(item.title);

            holder.title.setOnClickListener(v -> {
                Toast.makeText(MainActivity.this, item.title+"clicked", Toast.LENGTH_SHORT).show();
            });

            return convertView;
        }

        class ViewHolder {
            TextView title;
        }

        public void setList(List<HaniItem> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return list.get(i).id;
        }
    }
}
