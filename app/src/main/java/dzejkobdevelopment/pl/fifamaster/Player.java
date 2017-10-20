package dzejkobdevelopment.pl.fifamaster;

/**
 * Created by Dzejkob on 26.09.2017.
 */

public class Player {
    public String name;
    public int won, draw, lost, scored, conceded;

    public Player(String name, int won, int draw, int lost, int scored, int conceded){
        this.name = name;
        this.won = won;
        this.draw = draw;
        this.lost = lost;
        this.scored = scored;
        this.conceded = conceded;
    }
    public Player(){

    }
}
