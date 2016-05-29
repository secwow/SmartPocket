package com.example.user.smartpocket;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Billing extends Activity implements CompoundButton.OnCheckedChangeListener{
    private TextView switchStatus, errorMessage;
    private Switch mySwitch;

    private String Name,Comment,Date,Spinner, Type,Category,Sum, password, email;

    Switch aSwitch;
    Spinner aSpinner;
    EditText aSum, aComment, aDate;

    BackGround b = new BackGround();
    public void SearchElementsActivity()
    {


        checkSum(aSum.getText().toString());
        checkDate(aDate.getText().toString());
        Comment = aComment.getText().toString();
        Category = aSpinner.getSelectedItem().toString();




    }
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


    public void checkSum(String userSum)
    {
      if(userSum!=null && !userSum.isEmpty())
      {
          Sum = userSum;
          aSum.setBackgroundResource(R.drawable.succesedittextbox);

      }
        else
      {
          errorMessage.setText("Сумма не может быть пустой");
          aSum.setBackgroundResource(R.drawable.erroredittextbox);
      }
    }

    public void checkDate(String userDate)
    {

        Pattern p = Pattern.compile("[0-9]{4}-(0[1-9]|1[012])-(0[1-9]|1[0-9]|2[0-9]|3[01])");
        Matcher m = p.matcher(userDate);
        if(m.matches())
        {
            Date = aDate.getText().toString();
            aDate.setBackgroundResource(R.drawable.succesedittextbox);
        }
        else
        {
            aDate.setBackgroundResource(R.drawable.erroredittextbox);
            errorMessage.setText("Неверно введена дата");
        }
    }

    // Берём данные из элементов активити и добавляем их в поток.
    public void AddOper(View v) {
        SearchElementsActivity();

        if(Sum !=null && Date!=null)
        {
            b.execute(Name, Sum, Date, Type, Category, Comment);
        }


    }

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        aDate.setText(sdf.format(myCalendar.getTime()));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        // Установка значений по умолчанию.
        Expenditure();

        mySwitch = (Switch) findViewById(R.id.type);
        // Добавляем событие SWITCH, что бы менял Spinner
        if (mySwitch  != null) {
            mySwitch.setOnCheckedChangeListener(this);
        }

        // Получаем элементы с layout
        aSum= (EditText) findViewById(R.id.sum);
        aComment = (EditText) findViewById(R.id.comment);
        aDate = (EditText) findViewById(R.id.date);
        aSwitch = (Switch) findViewById(R.id.type);
        aSpinner = (Spinner)findViewById(R.id.spinner);
        errorMessage = (TextView)findViewById(R.id.Message);
        Type = "Расход";

        // Получаем логин с предыдущей страницы
        Name = getIntent().getStringExtra("Name");
        password = getIntent().getStringExtra("Password");
        email = getIntent().getStringExtra("Email");
        //Добавляем полю aDate событие при клике (открытие календаря)
        aDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Billing.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {



                String name = params[0];
                String summary = params[1];
                String date = params[2];
                String type = params[3];
                String category = params[4];
                String comment = params[5];
                String data = "";
                int tmp;

                try {
                    //URL к которому подключаемся
                    URL url = new URL("http://flyingsnow.ru.xsph.ru/add.php");
                    //POST-параметры которые мы передаём
                    String urlParams = "Name=" + name + "&Sum=" + summary + "&Date=" + date+ "&Type=" + type + "&Category=" + category+"&Comment=" + comment;

                    Log.d("msg",urlParams);
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
                s = "Платёж успешно добавлен";
                Log.d("Succes",s);
                Intent intent = new Intent(Billing.this, Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Name", Name);
                intent.putExtra("Password", password);
                intent.putExtra("Email", email);
                startActivity(intent);
            }
            else
            {


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
            Type = "Доход";
        }
        else
        {

            Expenditure();
            Type = "Расход";
        }

    }
}
