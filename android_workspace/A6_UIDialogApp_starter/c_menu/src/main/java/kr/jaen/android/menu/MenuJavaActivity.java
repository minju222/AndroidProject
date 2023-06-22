package kr.jaen.android.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MenuJavaActivity extends AppCompatActivity {

    private static final int GROUP_ID = 0;
    private static final int ITEM_INSERT = Menu.FIRST;
    private static final int ITEM_DELETE = Menu.FIRST + 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_java);

        Button btn = findViewById(R.id.btn);
        //context menu 필수
        registerForContextMenu(btn);

        // toolbar 뒤로가기 버튼 만들기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // sub title
        getSupportActionBar().setTitle("메인 타이틀");
        getSupportActionBar().setSubtitle("서브 타이틀");
    }

    // Menu 버튼을 눌렀을 때 실행되는 Callback 메서드
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // 메인메뉴 추가
        menu.add(GROUP_ID, ITEM_INSERT, 0, "추가하기")
                .setIcon(android.R.drawable.ic_menu_add)
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(GROUP_ID, ITEM_DELETE, 0, "삭제하기");

        // 서브메뉴 추가
        SubMenu subMenu = menu.addSubMenu("서브메뉴");
        subMenu.add("메뉴1");
        subMenu.add("메뉴2");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case ITEM_INSERT:
                Toast.makeText(this, "첫번째 항목 선택", Toast.LENGTH_SHORT).show();
                break;

            case ITEM_DELETE:
                Toast.makeText(this, "두번째 항목 선택", Toast.LENGTH_SHORT).show();
                break;

            case android.R.id.home:  // Toolbar의 뒤로가기를 눌렀을 때 동작
                finish();
                return true;
        }

        Toast.makeText(this, "메뉴 이름: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    // registerForContextMenu 메서드로 등록된 뷰를 Long Press 하면 실행되는 Callback 메서드
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(GROUP_ID, ITEM_DELETE, 0, "삭제하기");

        // 서브메뉴 추가
        SubMenu subMenu = menu.addSubMenu("서브메뉴");
        subMenu.add("메뉴1");
        subMenu.add("메뉴2");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        Toast.makeText(this, "Context 항목 선택", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "메뉴 이름: " + item.getTitle(), Toast.LENGTH_SHORT).show();

        /*ListView view = null;
        ContextMenu.ContextMenuInfo menuInfo = item.getMenuInfo();
        AdapterView.AdapterContextMenuInfo aptInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
        String selItem = view.getAdapter().getItem(aptInfo.position).toString();*/

        return super.onContextItemSelected(item);
    }
}