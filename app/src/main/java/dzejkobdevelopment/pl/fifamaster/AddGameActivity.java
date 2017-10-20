package dzejkobdevelopment.pl.fifamaster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddGameActivity extends AppCompatActivity {
    Team homeTeam, awayTeam;
    boolean penVisible = false;
    DatabaseAdapter db;
    EditText homePenalties, awayPenalties;
    TextView doubledot;
    CheckBox penaltiesCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.add_game));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        homeTeam = new Team("homePenalties", (LinearLayout) findViewById(R.id.team1), this);
        awayTeam = new Team("awayPenalties", (LinearLayout) findViewById(R.id.team2), this);

        setViews();
        showPenaltiesBoxes(false);
    }

    public void addPlayer(View view){
        Team team;
        if(view.getId() == R.id.add1){
            team = homeTeam;
        }else
            team = awayTeam;

        team.addNewPlayer();
        if(team.getNumberOfPlayers()==4)
            view.setEnabled(false);
    }

    public void addGame(View view){
        String text;
        if(checkFields()){
            Match match = new Match();
            for(EditText editText : homeTeam.getEditTextList())
                match.addHomePlayers(editText.getText().toString());

            for(EditText editText : awayTeam.getEditTextList())
                match.addAwayPlayers(editText.getText().toString());

            int home, away;
            EditText editText = (EditText)findViewById(R.id.homeScoreText);
            home = Integer.parseInt(editText.getText().toString());
            editText = (EditText)findViewById(R.id.awayScoreText);
            away = Integer.parseInt(editText.getText().toString());

            match.setGoals(home, away);

            if(penaltiesCheckBox.isChecked()){
                int homePen,awayPen;
                homePen = Integer.parseInt(homePenalties.getText().toString());
                awayPen = Integer.parseInt(awayPenalties.getText().toString());
                match.setPenalties(homePen, awayPen);
            }

            FifaMaster fifaMaster = (FifaMaster)getApplicationContext();

            db = fifaMaster.getDb();
            db.open();
            db.addMatch(match);

            Toast.makeText(getApplicationContext(), "Dodano mecz!", Toast.LENGTH_SHORT).show();
        }else Toast.makeText(getApplicationContext(), R.string.set_fields_message, Toast.LENGTH_SHORT).show();
    }

    private boolean checkFields(){
        for(EditText editText : homeTeam.getEditTextList()){
            if(editText.getText().toString().equals(""))
                return false;
        }

        for(EditText editText : awayTeam.getEditTextList()){
            if(editText.getText().toString().equals(""))
                return false;
        }

        EditText score = (EditText)findViewById(R.id.homeScoreText);
        if(score.getText().toString().equals(""))
            return false;
        score = (EditText)findViewById(R.id.awayScoreText);
        if(score.getText().toString().equals(""))
            return false;

        if(penaltiesCheckBox.isChecked()){
            if(homePenalties.getText().toString().equals("") || awayPenalties.getText().toString().equals(""))
                return false;
        }

        return true;
    }

    @Override
    protected void onDestroy(){
        if(db !=null)
            db.close();
        super.onDestroy();
    }

    private void setViews(){
        homePenalties = (EditText)findViewById(R.id.homePenaltiesText);
        awayPenalties = (EditText)findViewById(R.id.awayPenaltiesText);
        doubledot = (TextView)findViewById(R.id.textView4);
        penaltiesCheckBox = (CheckBox)findViewById(R.id.penaltiesCheckBox);
    }

    private void showPenaltiesBoxes(boolean bool){
        if (!bool){
            homePenalties.setVisibility(View.INVISIBLE);
            awayPenalties.setVisibility(View.INVISIBLE);
            doubledot.setVisibility(View.INVISIBLE);
        }else{
            homePenalties.setVisibility(View.VISIBLE);
            awayPenalties.setVisibility(View.VISIBLE);
            doubledot.setVisibility(View.VISIBLE);
        }
    }

    public void penatlyCheckBoxClicked(View view){
        boolean bool = penaltiesCheckBox.isChecked();
        Log.d("he", "penatlyCheckBoxClicked: " + bool);

        showPenaltiesBoxes(bool);
    }

}
