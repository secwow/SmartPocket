package com.example.user.smartpocket;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by User on 21.05.2016.
 */
public class Main extends Activity {

    EditText name, password;
    String Name, Password;
    Context ctx=this;
    String NAME=null, PASSWORD=null, EMAIL=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        name = (EditText) findViewById(R.id.main_name);
        password = (EditText) findViewById(R.id.main_password);
    }

    public void main_register(View v){
        startActivity(new Intent(this,Register.class));
    }

    public void main_login(View v){
        Name = name.getText().toString();
        Password = password.getText().toString();
        BackGround b = new BackGround();
        b.execute(Name, Password);
    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String password = params[1];
            String data="";
            int tmp;

            try {
                URL url = new URL("http://flyingsnow.ru.xsph.ru/login.php");
                String urlParams = "Name="+name+"&Password="+password;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while((tmp=is.read())!=-1){
                    data+= (char)tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            String err=null;


                try {

                    JSONObject root = new JSONObject(s);
                    //Получаем JSON объект
                    JSONObject user_data = root.getJSONObject("users");
                    NAME = user_data.getString("Name");
                    PASSWORD = user_data.getString("Password");
                    EMAIL = user_data.getString("Email");
                } catch (JSONException e) {
                    e.printStackTrace();
                    err = "Exception: "+e.getMessage();
                }
                if(NAME==null || PASSWORD==null || EMAIL==null) {
                    err = "Ошибка авторизации.";
                    Toast.makeText(getApplicationContext(),err,Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent i = new Intent(ctx, Home.class);
                    i.putExtra("Name", NAME);
                    i.putExtra("Password", PASSWORD);
                    i.putExtra("Email", EMAIL);
                    i.putExtra("err", err);
                    startActivity(i);
                }

            }

    }
}