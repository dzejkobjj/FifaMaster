package dzejkobdevelopment.pl.fifamaster;

import java.util.ArrayList;

/**
 * Created by Dzejkob on 13.09.2017.
 */

public class Match {
    private int id;
    private int homeGoals;
    private int awayGoals;
    private int homePenalties = -1;
    private int awayPenalties = -1;
    private ArrayList<String> homeTeam;
    private ArrayList<String> awayTeam;

    public Match(){

    }
    public Match(int id, int homeGoals, int awayGoals, ArrayList homeTeam, ArrayList awayTeam){
        this.id = id;
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
        this.awayTeam = awayTeam;
        this.homeTeam = homeTeam;
    }

    public void addHomePlayers(String text){
        if(homeTeam == null) homeTeam = new ArrayList<>();

        homeTeam.add(text);
    }

    public void addAwayPlayers(String text){
        if(awayTeam == null) awayTeam = new ArrayList<>();

        awayTeam.add(text);
    }

    public void setGoals(int home, int away){
        homeGoals = home;
        awayGoals = away;
    }
    int getHomeGoals(){
        return homeGoals;
    }

    int getAwayGoals(){
        return awayGoals;
    }

    public ArrayList<String> getHomeTeam() {
        return homeTeam;
    }

    public ArrayList<String> getAwayTeam() {
        return awayTeam;
    }

    public void setHomeTeam(ArrayList<String> homeTeam) {
        this.homeTeam = homeTeam;
    }

    public void setAwayTeam(ArrayList<String> awayTeam) {
        this.awayTeam = awayTeam;
    }

    public void setPenalties(int home, int away){
        this.homePenalties = home;
        this.awayPenalties = away;
    }

    public boolean isPenalty(){
        if(homePenalties != -1 && awayPenalties != -1)
            return true;
        else return false;
    }

    public int getHomePenalties(){
        return homePenalties;
    }

    public int getAwayPenalties(){
        return awayPenalties;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
