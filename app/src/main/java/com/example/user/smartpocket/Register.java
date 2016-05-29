package com.example.user.smartpocket;

/**
 * Created by User on 21.05.2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends Activity {

    EditText name, password, email;
    TextView error;
    String Name, Password, Email, Error;
    Context ctx=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        name = (EditText) findViewById(R.id.register_name);
        password = (EditText) findViewById(R.id.register_password);
        email = (EditText) findViewById(R.id.register_email);
        error = (TextView)findViewById(R.id.errorMessage);
    }

    public  void checkLogin(String userNameString){

        Pattern p = Pattern.compile("^[A-Za-z]([.A-Za-z0-9-]{1,18})([A-Za-z0-9])$");
        Matcher m = p.matcher(userNameString);
        if(m.matches())
        {
            Name = name.getText().toString();
            name.setBackgroundResource(R.drawable.succesedittextbox);
        }
        else
        {
            name.setBackgroundResource(R.drawable.erroredittextbox);
            error.setText("Логин должен состоять только из латинских букв.");
        }
    }


    public void checkEmail(String userEmail)
    {
        Pattern p = Pattern.compile("[a-zA-Z]{1}[a-zA-Z\\d\\u002E\\u005F]+@([a-zA-Z]+\\u002E){1,2}((net)|(com)|(org)|(ru))");
        Matcher m = p.matcher(userEmail);
        if(m.matches())
        {
           Email = email.getText().toString();
            email.setBackgroundResource(R.drawable.succesedittextbox);
        }
        else
        {
            email.setBackgroundResource(R.drawable.erroredittextbox);
            error.setText("Email должен состоять из шаблона логин@почта");
        }
    }
    public void checkPassword(String userPassword)
    {
        if(password.getText().toString().length()>=6)
        {
            Password = password.getText().toString();
            password.setBackgroundResource(R.drawable.succesedittextbox);
        }
        else
        {
            password.setBackgroundResource(R.drawable.erroredittextbox);
            error.setText("Пароль не может быть короче 6 символов");
        }
    }
    public void register_register(View v){
        checkPassword(password.getText().toString());
        checkLogin(name.getText().toString());
        checkEmail(email.getText().toString());


        if(Name!=null && Email !=null && Password!=null)
        {
            BackGround b = new BackGround();
            b.execute(Name,Password,Email);
        }



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
                // Создаём поток
                OutputStream os = httpURLConnection.getOutputStream();
                //Записываем в поток структуру POST-запроса
                os.write(urlParams.getBytes());
                os.flush();
                os.close();
                // Возвращаем всё что мы записали в поток
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
                s="Регистрация успешно завершена";
                error.setText(s);
                error.setTextColor(Color.GREEN);
                Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
                Intent i = new Intent(ctx, Main.class);
                startActivity(i);
            }

        }
    }

}