package kr.jaen.android.spinner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = findViewById(R.id.spinner);

        //adapter: 다른곳에 있는 array의 데이터를 가지고와서 spinner에 한개씩 넣어줌
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this, R.array.planets, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


//		ArrayAdapter adapter = ArrayAdapter.createFromResource(
//				this, R.array.planets, android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }
}