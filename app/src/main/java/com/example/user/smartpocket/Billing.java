package com.example.user.smartpocket;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Billing extends Activity implements CompoundButton.OnCheckedChangeListener{
    private TextView switchStatus;
    private Switch mySwitch;



    public void Expenditure()
    {
        String[] objects = {"Интернет", "Канцелярия"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, objects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerForms = (Spinner) findViewById(R.id.spinner);
        spinnerForms.setAdapter(adapter);

        spinnerForms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void Income()
    {
        String[] objects = {"Зарплата", "Стипендия", "Подработка"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, objects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerForms = (Spinner) findViewById(R.id.spinner);
        spinnerForms.setAdapter(adapter);

        spinnerForms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                // Todo: write code
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Todo: write code
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);


        Expenditure();
        mySwitch = (Switch) findViewById(R.id.type);
        if (mySwitch  != null) {
            mySwitch.setOnCheckedChangeListener(this);
        }


    }


    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String password = params[1];
            String email = params[2];
            String data = "";
            int tmp;

            try {
                //URL к которому подключаемся
                URL url = new URL("http://flyingsnow.ru.xsph.ru/add.php");
                //POST-параметры которые мы передаём
                String urlParams = "Name=" + name + "&Password=" + password + "&Email=" + email;
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
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }

                is.close();

                httpURLConnection.disconnect();

                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("")) {
                s = "Registration is succecful";
               
            }

        }

    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        ChangeComboBox(isChecked);
    }
    private void ChangeComboBox(boolean status)
    {
        if(status)
        {
            Income();
        }
        else
        {
            Expenditure();
        }

    }
}
