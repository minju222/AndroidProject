package kr.jaen.android.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MenuXMLActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_xml);
    }

    // Menu 버튼을 눌렀을 때 실행되는 Callback 메서드
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*
        Menu버튼이 눌러지면 XML에서 정의한 Menu 리소스에 대한 포인터 값을
        menu변수로 받아 화면에 보여주는 역할을 수행한다.
         */
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_xml, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_first:
                Toast.makeText(this, "첫번째 항목 선택", Toast.LENGTH_SHORT).show();
                break;

            case R.id.item_second:
                Toast.makeText(this, "두번째 항목 선택", Toast.LENGTH_SHORT).show();
                break;

            case R.id.item_third:
                Toast.makeText(this, "세번째 항목 선택", Toast.LENGTH_SHORT).show();
                break;

            case R.id.item_submenu_first:
                Toast.makeText(this, "서브메뉴 첫번째 항목 선택", Toast.LENGTH_SHORT).show();
                break;
        }

        Toast.makeText(this, "메뉴 이름: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }
}