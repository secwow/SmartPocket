package com.example.user.smartpocket;

/**
 * Created by User on 21.05.2016.
 */

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.os.Bundle;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Register extends Activity {

    EditText name, password, email;
    String Name, Password, Email;
    Context ctx=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        name = (EditText) findViewById(R.id.register_name);
        password = (EditText) findViewById(R.id.register_password);
        email = (EditText) findViewById(R.id.register_email);
    }

    public void register_register(View v){
        Name = name.getText().toString();
        Password = password.getText().toString();
        Email = email.getText().toString();
        BackGround b = new BackGround();
        b.execute(Name, Password, Email);
    }

    class BackGround extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String password = params[1];
            String email = params[2];
            String data="";
            int tmp;

            try {
                //URL к которому подключаемся
                URL url = new URL("http://flyingsnow.ru.xsph.ru/register.php");
                //POST-параметры которые мы передаём
                String urlParams = "Name="+name+"&Password="+password+"&Email="+email;
                //Создаём HTTP подключение
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();// Открываем подлкючение по указанному адресу
                httpURLConnection.setDoOutput(true);// Получаем разрешение для отправки POST запросов
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
            if(s.equals("")){
                s="Data saved successfully.";
            }
            Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
        }
    }

}