package dzejkobdevelopment.pl.fifamaster;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private ListView historyView;
    private MatchAdapter matchAdapter;
    DatabaseAdapter db;
    ArrayList<Match> matches = new ArrayList<Match>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.history));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        historyView = (ListView)findViewById(R.id.historyView);
        registerForContextMenu(historyView);

        FifaMaster fifaMaster = (FifaMaster)getApplicationContext();
        db = fifaMaster.getDb();

        fillListView();
    }

    private void fillListView(){
        db.open();
        Cursor matches = db.getAllMatches();
        if(matches != null)
            updateMatchList(matches);
        matchAdapter = new MatchAdapter(this, this.matches);
        historyView.setAdapter(matchAdapter);

    }

    private void updateMatchList(Cursor cursor){
        if(cursor != null && cursor.moveToFirst()){
            Match match;
            do{
                match = new Match();
                match.setId(cursor.getInt(0));
                match.setHomeTeam(db.getTeam(cursor.getInt(0), "home"));
                match.setAwayTeam(db.getTeam(cursor.getInt(0), "away"));
                match.setGoals(cursor.getInt(1), cursor.getInt(2));
                matches.add(match);
            }while(cursor.moveToNext());
        }

    }

    @Override
    protected void onDestroy(){
        if(db !=null)
            db.close();
        super.onDestroy();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.historyView) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.add(Menu.NONE, 0, 0, R.string.delete);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        Match match = matches.get(info.position);

        if(!db.deleteMatch(match.getId()))
            Log.d("USUWAMY HEHE", "onContextItemSelected: coś poszło nie tak z tym kasowaniem");
        else
            matchAdapter.notifyDataSetChanged();

        return true;
    }
}
