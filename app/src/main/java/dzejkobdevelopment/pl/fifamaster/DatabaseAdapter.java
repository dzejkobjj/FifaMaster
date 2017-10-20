package dzejkobdevelopment.pl.fifamaster;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dzejkob on 20.09.2017.
 */

public class DatabaseAdapter {
    public static final int DB_VERSION = 2;
    public static final String DB_NAME = "FifaMaster.db";
    public static MatchTable matchTable;
    public static PlayerTable playerTable;
    public static MatchPlayerTable matchPlayerTable;

    private SQLiteDatabase db;
    private Context context;
    private DatabaseHelper dbHelper;

    public DatabaseAdapter(Context context){
        this.context = context;
    }

    public DatabaseAdapter open(){
        dbHelper = new DatabaseHelper(context, DB_NAME, null, DB_VERSION);
        try{
            db = dbHelper.getWritableDatabase();
        } catch (SQLException e){
            db = dbHelper.getReadableDatabase();
        }
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    public void addMatch(Match match){
        ContentValues newMatch = new ContentValues();
        newMatch.put(matchTable.KEY_HOME, match.getHomeGoals());
        newMatch.put(matchTable.KEY_AWAY, match.getAwayGoals());
        if(match.isPenalty()){
            newMatch.put(matchTable.KEY_PENALTY_HOME, match.getHomePenalties());
            newMatch.put(matchTable.KEY_PENALTY_AWAY, match.getAwayPenalties());
        }
        newMatch.put(matchTable.KEY_DATE, System.currentTimeMillis());
        long matchId = db.insert(matchTable.DB_MATCH_TABLE, null, newMatch);

        for(String player : match.getHomeTeam()){
            long playerId = getPlayerId(player);
            if(playerId == -1){
                playerId = addPlayer(player);
            }
            updatePlayer(playerId, getWinner(match), match.getHomeGoals(), match.getAwayGoals());
            addPlayerMatchLink(matchId, playerId, "home");
        }

        for(String player : match.getAwayTeam()){
            long playerId = getPlayerId(player);
            if(playerId == -1){
                playerId = addPlayer(player);
            }
            updatePlayer(playerId, getWinner(match)*(-1), match.getAwayGoals(), match.getHomeGoals());
            addPlayerMatchLink(matchId, playerId, "away");
        }

    }

    public long addPlayer(String name){
        ContentValues newPlayer = new ContentValues();
        newPlayer.put(playerTable.KEY_NAME, name);

        return db.insert(playerTable.DB_PLAYER_TABLE, null, newPlayer);
    }

    public void updatePlayer(long id, int won, int scored, int conceded){
        String column;
        if(won == 1)
            column = "won";
        else if (won == 0)
            column = "draw";
        else column = "lost";

        db.execSQL("UPDATE player SET " + column + "=" + column +
                "+1, conceded=conceded+" + conceded +
                ", scored=scored+" + scored +
                " WHERE " + playerTable.KEY_ID + "=" + id + ";");
    }

    private void updatePlayerAfterDeletingMatch(long id, int won, int scored, int conceded){
        String column;
        if(won == 1)
            column = "won";
        else if (won == 0)
            column = "draw";
        else column = "lost";

        db.execSQL("UPDATE player SET " + column + "=" + column +
                "-1, conceded=conceded-" + conceded +
                ", scored=scored-" + scored +
                " WHERE " + playerTable.KEY_ID + "=" + id + ";");
    }

    public void addPlayerMatchLink(long matchId, long playerId, String team){
        ContentValues newLink = new ContentValues();
        newLink.put(matchPlayerTable.KEY_MATCH, matchId);
        newLink.put(matchPlayerTable.KEY_PLAYER, playerId);
        newLink.put(matchPlayerTable.KEY_TEAM, team);

        db.insert(matchPlayerTable.DB_MATCH_PLAYER_TABLE, null, newLink);
    }

    private int getWinner(Match match){
        if(match.getHomeGoals() > match.getAwayGoals())
            return 1;
        else if (match.getHomeGoals() < match.getAwayGoals())
            return -1;
        else if(match.isPenalty()){
            if(match.getHomePenalties()>match.getAwayPenalties())
                return 1;
            else return -1;
        }
        else return 0;
    }
    public long getPlayerId(String name){
        String[] columns = {playerTable.KEY_ID};
        String where = playerTable.KEY_NAME + "='" + name + "'";

        Cursor cursor = db.query(playerTable.DB_PLAYER_TABLE, columns, where, null, null, null, null);

        if(cursor != null && cursor.moveToFirst())
            return cursor.getInt(playerTable.ID_COLUMN);
        else return -1;
    }

    public Cursor getAllMatches(){
        String[] columns = {matchTable.KEY_ID, matchTable.KEY_HOME, matchTable.KEY_AWAY};
        return db.query(matchTable.DB_MATCH_TABLE, columns, null, null, null, null, null);
    }

    public Match getMatch(int id){
        String[] columns = {matchTable.KEY_ID, matchTable.KEY_HOME, matchTable.KEY_AWAY, matchTable.KEY_PENALTY_HOME, matchTable.KEY_PENALTY_AWAY};
        String where = matchTable.KEY_ID + "=" + id;

        Cursor cursor = db.query(matchTable.DB_MATCH_TABLE, columns, where, null, null, null, null);
        Match match;
        if(cursor != null && cursor.moveToFirst()){
            match = new Match(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), getTeam(id, "home"), getTeam(id, "away"));
            if(!cursor.isNull(3)){
                match.setPenalties(cursor.getInt(3), cursor.getInt(4));
            }
            return match;
        }else return null;
    }

    public Cursor getAllPlayers(){
        String[] columns = {playerTable.KEY_NAME, playerTable.KEY_WON, playerTable.KEY_DRAW, playerTable.KEY_LOST, playerTable.KEY_SCORED, playerTable.KEY_CONCEDED};
        String order = "(100.0*" +playerTable.KEY_WON + "/" + "(" + playerTable.KEY_WON + "+" + playerTable.KEY_DRAW + "+" + playerTable.KEY_LOST + ")) DESC";
        String order2 = playerTable.KEY_NAME + " DESC";
        return db.query(playerTable.DB_PLAYER_TABLE, columns, null, null, null, null, order);
    }

    public boolean deleteMatch(int id){
        Match match = getMatch(id);

        if(match == null)
            return false;

        for(String name : match.getHomeTeam()){
            long playerId = getPlayerId(name);
            updatePlayerAfterDeletingMatch(playerId, getWinner(match), match.getHomeGoals(), match.getAwayGoals());
        }

        for(String name : match.getAwayTeam()){
            long playerId = getPlayerId(name);
            updatePlayerAfterDeletingMatch(playerId, getWinner(match)*(-1), match.getAwayGoals(), match.getHomeGoals());
        }

        db.execSQL("DELETE FROM " + matchPlayerTable.DB_MATCH_PLAYER_TABLE + " WHERE " + matchPlayerTable.KEY_MATCH + "=" + id);
        db.execSQL("DELETE FROM " + matchTable.DB_MATCH_TABLE + " WHERE " + matchTable.KEY_ID + "=" + id);

        return true;
    }

    public ArrayList<String> getTeam(int matchId, String team){
        String[] column = {matchPlayerTable.KEY_PLAYER};
        String where = matchPlayerTable.KEY_MATCH + "=" + matchId + " AND " + matchPlayerTable.KEY_TEAM + "='" + team +"'";
        Cursor cursor = db.query(matchPlayerTable.DB_MATCH_PLAYER_TABLE, column, where, null, null, null, null);

        ArrayList<String> players = new ArrayList<>();;
        if(cursor != null && cursor.moveToFirst()){
            String[] playerColumn = {playerTable.KEY_NAME};
            Cursor player;
            do{
                where = playerTable.KEY_ID + "=" + cursor.getInt(0);
                //Log.d("getTeam", where);
                player = db.query(playerTable.DB_PLAYER_TABLE, playerColumn, where, null, null, null, null);
                player.moveToFirst();
                players.add(player.getString(0));
            }while(cursor.moveToNext());
        }
        return players;
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(matchTable.DB_CREATE_MATCH_TABLE);
            db.execSQL(playerTable.DB_CREATE_PLAYER_TABLE);
            db.execSQL(matchPlayerTable.DB_CREATE_MATCH_PLAYER_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL(matchTable.DROP_MATCH_TABLE);
            db.execSQL(playerTable.DROP_PLAYER_TABLE);
            db.execSQL(matchPlayerTable.DROP_MATCH_PLAYER_TABLE);

            onCreate(db);
        }

        public ArrayList<Cursor> getData(String Query){
            //get writable database
            SQLiteDatabase sqlDB = this.getWritableDatabase();
            String[] columns = new String[] { "message" };
            //an array list of cursor to save two cursors one has results from the query
            //other cursor stores error message if any errors are triggered
            ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
            MatrixCursor Cursor2= new MatrixCursor(columns);
            alc.add(null);
            alc.add(null);

            try{
                String maxQuery = Query ;
                //execute the query results will be save in Cursor c
                Cursor c = sqlDB.rawQuery(maxQuery, null);

                //add value to cursor2
                Cursor2.addRow(new Object[] { "Success" });

                alc.set(1,Cursor2);
                if (null != c && c.getCount() > 0) {

                    alc.set(0,c);
                    c.moveToFirst();

                    return alc ;
                }
                return alc;
            } catch(SQLException sqlEx){
                Log.d("printing exception", sqlEx.getMessage());
                //if any exceptions are triggered save the error message to cursor an return the arraylist
                Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
                alc.set(1,Cursor2);
                return alc;
            } catch(Exception ex){
                Log.d("printing exception", ex.getMessage());

                //if any exceptions are triggered save the error message to cursor an return the arraylist
                Cursor2.addRow(new Object[] { ""+ex.getMessage() });
                alc.set(1,Cursor2);
                return alc;
            }
        }

    }
    private static class MatchTable{
        public static final String DB_MATCH_TABLE = "match";

        public static final String KEY_ID = "_id";
        public static final String ID_OPTIONS = "INTEGER PRIMARY KEY AUTOINCREMENT";
        public static final int ID_COLUMN = 0;

        public static final String KEY_HOME = "home";
        public static final String HOME_OPTIONS = "INTEGER NOT NULL";
        public static final int HOME_COLUMN = 1;

        public static final String KEY_AWAY = "away";
        public static final String AWAY_OPTIONS = "INTEGER NOT NULL";
        public static final int AWAY_COLUMN = 2;

        public static final String KEY_PENALTY_HOME = "home_penalty";
        public static final String PENALTY_HOME_OPTIONS = "INTEGER";

        public static final String KEY_PENALTY_AWAY = "away_penalty";
        public static final String PENALTY_AWAY_OPTIONS = "INTEGER";

        public static final String KEY_DATE = "date";
        public static final String DATE_OPTIONS = "INTEGER NOT NULL";

        public static final String DB_CREATE_MATCH_TABLE =
                "CREATE TABLE " + DB_MATCH_TABLE + "( " +
                        KEY_ID + " " + ID_OPTIONS + ", " +
                        KEY_HOME + " " + HOME_OPTIONS + ", " +
                        KEY_AWAY + " " + AWAY_OPTIONS + ", " +
                        KEY_PENALTY_HOME + " " + PENALTY_HOME_OPTIONS + ", " +
                        KEY_PENALTY_AWAY + " " + PENALTY_AWAY_OPTIONS + ", " +
                        KEY_DATE + " " + DATE_OPTIONS +
                        ");";
        public static final String DROP_MATCH_TABLE = "DROP TABLE IF EXISTS " + DB_MATCH_TABLE;
    }

    private static class PlayerTable{
        public static final String DB_PLAYER_TABLE = "player";

        public static final String KEY_ID = "_id";
        public static final String ID_OPTIONS = "INTEGER PRIMARY KEY AUTOINCREMENT";
        public static final int ID_COLUMN = 0;

        public static final String KEY_NAME = "name";
        public static final String NAME_OPTIONS = "TEXT NOT NULL UNIQUE";
        public static final int NAME_COLUMN = 1;

        public static final String KEY_WON = "won";
        public static final String WON_OPTIONS = "INTEGER DEFAULT 0";
        public static final int WON_COLUMN = 2;

        public static final String KEY_LOST = "lost";
        public static final String LOST_OPTIONS = "INTEGER DEFAULT 0";
        public static final int LOST_COLUMN = 3;

        public static final String KEY_DRAW = "draw";
        public static final String DRAW_OPTIONS = "INTEGER DEFAULT 0";
        public static final int DRAW_COLUMN = 4;

        public static final String KEY_CONCEDED = "conceded";
        public static final String CONCEDED_OPTIONS = "INTEGER DEFAULT 0";
        public static final int CONCEDED_COLUMN = 5;

        public static final String KEY_SCORED = "scored";
        public static final String SCORED_OPTIONS = "INTEGER DEFAULT 0";
        public static final int SCORED_COLUMN = 6;

        public static final String DB_CREATE_PLAYER_TABLE =
                "CREATE TABLE " + DB_PLAYER_TABLE + "( " +
                        KEY_ID + " " + ID_OPTIONS + ", " +
                        KEY_NAME + " " + NAME_OPTIONS + ", " +
                        KEY_WON + " " + WON_OPTIONS + ", " +
                        KEY_LOST + " " + LOST_OPTIONS + ", " +
                        KEY_DRAW + " " + DRAW_OPTIONS + ", " +
                        KEY_SCORED + " " + SCORED_OPTIONS + ", " +
                        KEY_CONCEDED + " " + CONCEDED_OPTIONS +
                        ");";
        public static final String DROP_PLAYER_TABLE = "DROP TABLE IF EXISTS " + DB_PLAYER_TABLE;

    }

    private static class MatchPlayerTable{
        public static final String DB_MATCH_PLAYER_TABLE = "match_player";

        public static final String KEY_MATCH = "matchid";
        public static final String MATCH_REFERENCE_TABLE = MatchTable.DB_MATCH_TABLE;

        public static final String KEY_TEAM = "team";
        public static final String TEAM_OPTIONS = " TEXT NOT NULL";

        public static final String KEY_PLAYER = "playerid";
        public static final String PLAYER_REFERENCE_TABLE = PlayerTable.DB_PLAYER_TABLE;

        public static final String DB_CREATE_MATCH_PLAYER_TABLE =
                "CREATE TABLE " + DB_MATCH_PLAYER_TABLE + "(" +
                        KEY_TEAM + " " + TEAM_OPTIONS + ", " +
                        KEY_MATCH + " REFERENCES " + MATCH_REFERENCE_TABLE + "(" + MatchTable.KEY_ID + "), " +
                        KEY_PLAYER + " REFERENCES " + PLAYER_REFERENCE_TABLE + "(" + PlayerTable.KEY_ID + "), " +
                        "PRIMARY KEY(" + KEY_MATCH + ", " + KEY_PLAYER + "));";
        public static final String DROP_MATCH_PLAYER_TABLE = "DROP TABLE IF EXISTS " + DB_MATCH_PLAYER_TABLE;
    }
}
