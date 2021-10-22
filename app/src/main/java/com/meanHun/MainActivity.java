package com.meanHun;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lv_dsContact;
    ArrayList<Contact> ds_contact;
    ArrayAdapter<Contact> adapterContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
    }

    private void addControls() {
        lv_dsContact = findViewById(R.id.lv_contact);
        ds_contact = new ArrayList<>();
        adapterContact = new ArrayAdapter<Contact>(
                MainActivity.this, android.R.layout.simple_list_item_1,ds_contact
        );
        lv_dsContact.setAdapter(adapterContact);
        ContactTask contactTask = new ContactTask();
        contactTask.execute();
    }
    class ContactTask extends AsyncTask<Void,Void, ArrayList<Contact>> {
        @Override
        protected ArrayList<Contact> doInBackground(Void... voids) {
            ArrayList<Contact> ds = new ArrayList<>();
            try {
                //Khai bao duong dan cua CSDL
                URL url = new URL("https://www.w3schools.com/js/customers_mysql.php");
                //Mo luong doc du lieu tu url
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                //Khai bao doi tuong reader co the doc duoc noi dung chua UTF-8
                InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(),
                        StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuffer = new StringBuilder();
                String line= bufferedReader.readLine() ;
                while (line != null) {
                    stringBuffer.append(line);
                    line = bufferedReader.readLine();
                }
                inputStreamReader.close();
                JSONArray jsonArray = new JSONArray(stringBuffer.toString());
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Contact contact = new Contact();
                    if (jsonObject.has("Name")){
                        contact.setName(jsonObject.getString("Name"));
                    }
                    if (jsonObject.has("City")){
                        contact.setCity(jsonObject.getString("City"));
                    }
                    if (jsonObject.has("Country")){
                        contact.setCountry(jsonObject.getString("Country"));
                    }
                    ds.add(contact);
                }
            } catch (Exception e) {
                Log.e("Erro: ",e.toString());
            }
            return ds;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            adapterContact.clear();
        }

        @Override
        protected void onPostExecute(ArrayList<Contact> contacts) {
            super.onPostExecute(contacts);
            adapterContact.clear();
            adapterContact.addAll(contacts);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

    }
}