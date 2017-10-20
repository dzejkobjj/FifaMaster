package dzejkobdevelopment.pl.fifamaster;
//TODO dodać usuwanie meczu
//TODO dodać automatyczne uzupełnianie nazwy gracza
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    MainMenuItem[] menuItems = {
            new MainMenuItem(R.string.add_game, R.drawable.ball, 1),
            new MainMenuItem(R.string.table, R.drawable.table, 2),
            new MainMenuItem(R.string.history, R.drawable.list, 3),
            new MainMenuItem(R.string.db_manager, R.drawable.cup, 4)};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GridView gridView = (GridView)findViewById(R.id.menuGridView);
        MenuAdapter menuAdapter = new MenuAdapter(this, menuItems);
        gridView.setAdapter(menuAdapter);

        FifaMaster fifaMaster = (FifaMaster)getApplicationContext();


        final Context context = (Context) this;
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(id==1) {
                    Intent intent = new Intent(context, AddGameActivity.class);
                    startActivity(intent);
                } else if(id==2){
                    Intent intent = new Intent(context, TableActivity.class);
                    startActivity(intent);
                }else if(id==3){
                    Intent intent = new Intent(context, HistoryActivity.class);
                    startActivity(intent);
                }else if(id==4){
                    Intent intent = new Intent(context, AndroidDatabaseManager.class);
                    startActivity(intent);
                }
            }
        });
        }

}
