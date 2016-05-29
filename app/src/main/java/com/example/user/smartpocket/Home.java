package com.example.user.smartpocket;


import android.app.Activity;
        import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Home extends Activity {
    Button btnOk;
    String name, password, email, Err;
    TextView nameTV, emailTV, passwordTV, err, currentBalance;
    int currentBal=0;
    String login,id,summa,date,type,category,comment;
    BackGround b = new BackGround();
    public void Billing(View v){
        Intent intent = new Intent(this, Billing.class);
        intent.putExtra("Name", name.toString());
        intent.putExtra("Email", email.toString());
        intent.putExtra("Password",password.toString());
        startActivity(intent);
    }
    public void GetBills(View v){
        try {
            b.execute(name);

        }
        catch (java.lang.IllegalStateException e)
        {
            b.cancel(false);
        }




    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        nameTV = (TextView) findViewById(R.id.home_name);
        emailTV = (TextView) findViewById(R.id.home_email);
        passwordTV = (TextView) findViewById(R.id.home_password);
        err = (TextView) findViewById(R.id.err);
        currentBalance = (TextView)findViewById(R.id.currentBalance);

        name = getIntent().getStringExtra("Name");
        password = getIntent().getStringExtra("Password");
        email = getIntent().getStringExtra("Email");
        Err = getIntent().getStringExtra("err");

        nameTV.setText("Здравствуйте, "+name);

        emailTV.setText(email);
        err.setText(Err);
        b.execute(name);
        btnOk = (Button) findViewById(R.id.button);
        View.OnClickListener oclBtnOk = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        };

    }
    public void exit(View v)
    {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
    }








    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            {
                String name = params[0];

                String data = "";
                int tmp;

                try {

                    //URL к которому подключаемся
                    URL url = new URL("http://flyingsnow.ru.xsph.ru/getbills.php");
                    //POST-параметры которые мы передаём
                    String urlParams = "Name=" + name;
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

        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("stroka", s);
            if (s.equals("")) {

                Log.d("Error display message", s);
            } else {
                try {
                    TableLayout stk = (TableLayout) findViewById(R.id.table);
                    TableRow tbrow0 = new TableRow(getApplicationContext());
                    TextView tv0 = new TextView(getApplicationContext());
                    tv0.setText("Тип ");
                    tv0.setTextColor(Color.YELLOW);
                    tv0.setTextSize(20);
                    tbrow0.addView(tv0);
                    TextView tv1 = new TextView(getApplicationContext());
                    tv1.setText("Сумма");
                    tv1.setTextColor(Color.YELLOW);
                    tv1.setTextSize(20);
                    tbrow0.addView(tv1);
                    TextView tv2 = new TextView(getApplicationContext());
                    tv2.setText(" Категория ");
                    tv2.setTextColor(Color.YELLOW);
                    tv2.setTextSize(20);
                    tbrow0.addView(tv2);
                    TextView tv3 = new TextView(getApplicationContext());
                    tv3.setText(" Коммент ");
                    tv3.setTextColor(Color.YELLOW);
                    tv3.setTextSize(20);
                    tbrow0.addView(tv3);
                    stk.addView(tbrow0);

                    JSONObject bill = new JSONObject(s);
                    JSONArray jsonMainArr = bill.getJSONArray("bills");
                    for (int i = 0; i < jsonMainArr.length(); i++) {
                        JSONObject childJSONObject = jsonMainArr.getJSONObject(i);
                        String login = childJSONObject.getString("Login");
                        String comment = childJSONObject.getString("Comment");
                        String summa = childJSONObject.getString("Summa");
                        String date = childJSONObject.getString("Date");
                        String type = childJSONObject.getString("Type");
                        String category = childJSONObject.getString("Category");
                        TableRow tbrow = new TableRow(getApplicationContext());
                        TextView t1v = new TextView(getApplicationContext());

                        if (type.equals("Расход")) {
                            t1v.setText("-");
                            t1v.setTextColor(Color.RED);
                            t1v.setGravity(Gravity.CENTER);
                            t1v.setTextSize(20);
                            tbrow.addView(t1v);
                            currentBal-=Integer.valueOf(summa).intValue();
                        }
                        if (type.equals("Доход")) {
                            t1v.setText("+");
                            t1v.setTextColor(Color.GREEN);
                            t1v.setGravity(Gravity.CENTER);
                            t1v.setTextSize(20);
                            tbrow.addView(t1v);
                            currentBal+=Integer.valueOf(summa).intValue();
                        }

                        TextView t2v = new TextView(getApplicationContext());
                        t2v.setText(summa);
                        t2v.setTextColor(Color.BLACK);

                        t2v.setGravity(Gravity.CENTER);
                        tbrow.addView(t2v);

                        TextView t3v = new TextView(getApplicationContext());
                        t3v.setText(category);
                        t3v.setTextColor(Color.BLACK);
                        t3v.setGravity(Gravity.CENTER);
                        tbrow.addView(t3v);

                        TextView t4v = new TextView(getApplicationContext());
                        t4v.setText(comment);
                        t4v.setTextColor(Color.BLACK);
                        t4v.setGravity(Gravity.CENTER);
                        tbrow.addView(t4v);
                        stk.addView(tbrow);
                        currentBalance.setText("Текущий баланс: " + currentBal);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }


    }


}