package com.example.a1008.test;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    private String url = "jdbc:mysql://[IP]/{TABLE}";
    private String user = "[USERNAME]";
    private String passwd = "[PASSWORD]";

    private ListView list;
    private String [] Name;
    private String [] Addr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new MyTask().execute();

        list = (ListView) findViewById(R.id.list_data);
    }

    private class MyTask extends AsyncTask<Void,Void,Void>{
        private int i = 0;
        @Override
        protected Void doInBackground(Void... voids) {

            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(url,user,passwd);

                Statement st = conn.createStatement();
                String sql = "[SQL QUERY COMMANDLINE]";

                final ResultSet rs = st.executeQuery(sql);

                //Get data size
                while(rs.next()){
                    i++;
                }
                //Set Array size
                Name = new String[i];
                Addr = new String[i];
                i=0;

                //Move to first data
                rs.first();

                //Store data to array
                while(rs.next()){
                    Name[i] = rs.getString(1);
                    Addr[i] = rs.getString(2);
                    i++;
                }

                conn.close();

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void Result){
            MyAdapter adapter = new MyAdapter(MainActivity.this);
            list.setAdapter(adapter);
            super.onPostExecute(Result);
        }
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        public MyAdapter(Context c){
            inflater = LayoutInflater.from(c);
        }
        @Override
        public int getCount() {
            return Name.length;
        }

        @Override
        public Object getItem(int i) {
            return Name[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.layout_adapter,null);
            TextView aName,aAddress;

            aName = (TextView) view.findViewById(R.id.text_name);
            aAddress = (TextView) view.findViewById(R.id.text_addr);

            aName.setText(Name[i]);
            aAddress.setText(Addr[i]);
            return view;
        }
    }
}
