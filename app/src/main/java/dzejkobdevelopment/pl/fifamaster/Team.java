package dzejkobdevelopment.pl.fifamaster;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dzejkob on 06.09.2017.
 */

public class Team {
    private String id;
    private LinearLayout layout;
    private int numberOfPlayers;
    private List<EditText> players;
    private Context context;

    public Team(String id, LinearLayout layout, Context context){
        this.id = id;
        this.layout = layout;
        this.context = context;
        this.numberOfPlayers = 0;
        players = new ArrayList<>();
        addNewPlayer();
        //addNewPlayer();
    }

    public void addNewPlayer(){
        EditText editText = new EditText(context);
        editText.setMaxLines(1);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(25)});
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(params);
        layout.addView(editText);
        players.add(editText);
        numberOfPlayers++;

        if(numberOfPlayers == 1)
            editText.setHint(R.string.player1);
        else if(numberOfPlayers == 2)
            editText.setHint(R.string.player2);
        else if(numberOfPlayers == 3)
            editText.setHint(R.string.player3);
        else if(numberOfPlayers == 4)
            editText.setHint(R.string.player4);
    }
    public String getId() {
        return id;
    }

    public LinearLayout getLayout() {
        return layout;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public EditText getPlayer(int i) {
        return players.get(i);
    }

    public List<EditText> getEditTextList(){
        return players;
    }

}
