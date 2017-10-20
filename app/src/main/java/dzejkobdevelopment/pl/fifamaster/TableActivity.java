package dzejkobdevelopment.pl.fifamaster;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class TableActivity extends AppCompatActivity {
    private ListView table;
    private TableAdapter tableAdapter;
    DatabaseAdapter db;
    ArrayList<Player> players = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.table));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setHeader();

        table = (ListView)findViewById(R.id.tableView);
        FifaMaster fifaMaster = (FifaMaster)getApplicationContext();
        db = fifaMaster.getDb();

        fillListView();
    }

    private void fillListView(){
        db.open();
        Cursor players = db.getAllPlayers();
        if(players !=null)
            updatePlayerList(players);
        tableAdapter = new TableAdapter(this, this.players);
        table.setAdapter(tableAdapter);
    }

    private void updatePlayerList(Cursor cursor){
        if(cursor != null && cursor.moveToFirst()){
            Player player;
            do{
                player = new Player();
                player.name = cursor.getString(0);
                player.won = cursor.getInt(1);
                player.draw = cursor.getInt(2);
                player.lost = cursor.getInt(3);
                player.scored = cursor.getInt(4);
                player.conceded = cursor.getInt(5);
                players.add(player);
            }while(cursor.moveToNext());
        }
    }

    private void setHeader(){
        View header = findViewById(R.id.tableHeader);
        TextView tv = (TextView)header.findViewById(R.id.name);
        tv.setText("Name");
        tv = (TextView)header.findViewById(R.id.played);
        tv.setText("P");
        tv = (TextView)header.findViewById(R.id.won);
        tv.setText("W");
        tv = (TextView)header.findViewById(R.id.drawed);
        tv.setText("D");
        tv = (TextView)header.findViewById(R.id.lost);
        tv.setText("L");
        tv = (TextView)header.findViewById(R.id.scored);
        tv.setText("F");
        tv = (TextView)header.findViewById(R.id.conceded);
        tv.setText("A");
        tv = (TextView)header.findViewById(R.id.goalDifference);
        tv.setText("GD");
        tv = (TextView)header.findViewById(R.id.points);
        tv.setText("Pts");
        tv = (TextView)header.findViewById(R.id.winPercent);
        tv.setText("%");

    }

    @Override
    protected void onDestroy(){
        if(db !=null)
            db.close();
        super.onDestroy();
    }
}
