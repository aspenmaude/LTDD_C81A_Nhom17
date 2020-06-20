package com.example.UngDungDocTinTuc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Customadapter customadapter;
    ArrayList<Read> arrayread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview);
        arrayread = new ArrayList<Read>();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Readdata().execute("https://vnexpress.net/rss/the-gioi.rss");
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                intent.putExtra("link",arrayread.get(position).link);
                startActivity(intent);
            }
        });
    }

    class Readdata extends AsyncTask<String,Integer,String>{

        @Override // Hàm x? lý
        protected String doInBackground(String... params) {
            return ReadContent(params[0]);
        }

        @Override // Hàm tr? v?
        protected void onPostExecute(String s) {
            XMLDOMParser parser = new XMLDOMParser();
            Document document = parser.getDocument(s);
            NodeList nodeList = document.getElementsByTagName("item");
            NodeList nodeListdescription = document.getElementsByTagName("description");
            String images = "";
            String title = "";
            String link = "";

            for (int i = 0; i < nodeList.getLength(); i++){
                String cdata = nodeListdescription.item(i + 1).getTextContent();
                Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
                Matcher matcher = p.matcher(cdata);

                if (matcher.find()){
                    images = matcher.group(1);

                }

                Element element = (Element) nodeList.item(i);
                title = parser.getValue(element,"title");
                link = parser.getValue(element,"link");
                arrayread.add(new Read(title,link,images));
            }

            customadapter = new Customadapter(MainActivity.this,android.R.layout.simple_list_item_1,arrayread);
            listView.setAdapter(customadapter);
            super.onPostExecute(s);

        }
    }

    // Hàm ??c n?i dung
    private String ReadContent(String theUrl) {
        StringBuilder content = new StringBuilder();
        try {
            // T?o 1 url
            URL url = new URL(theUrl);

            // T?o 1 ???ng d?n k?t n?i URL
            URLConnection urlConnection = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // ??c n?i dung trong URL
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            //Log.d("content", content.toString());
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }



}
