package dzejkobdevelopment.pl.fifamaster;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Dzejkob on 25.09.2017.
 */

public class MatchAdapter extends ArrayAdapter<Match> {
    private Activity context;
    private List<Match> matches;
    public MatchAdapter(Activity context, List<Match> matches){
        super(context, R.layout.list_view_item, matches);
        this.context = context;
        this.matches = matches;
    }

    static class ViewHolder{
        public TextView homeTeam;
        public TextView awayTeam;
        public TextView score;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder;
        View rowView = convertView;
        if(rowView == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            rowView = layoutInflater.inflate(R.layout.list_view_item, null, true);
            viewHolder = new ViewHolder();
            viewHolder.homeTeam = (TextView) rowView.findViewById(R.id.homeTeam);
            viewHolder.awayTeam = (TextView) rowView.findViewById(R.id.awayTeam);
            viewHolder.score = (TextView) rowView.findViewById(R.id.score);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        Match match = matches.get(position);
        String team = "";
        for(String player : match.getHomeTeam()){
            team += player + ", ";
        }
        team = team.substring(0, team.length() -2);
        viewHolder.homeTeam.setText(team);
        team = "";

        for(String player : match.getAwayTeam()){
            team += player + ", ";
        }
        team = team.substring(0, team.length() -2);
        viewHolder.awayTeam.setText(team);

        String score = match.getHomeGoals() + ":" + match.getAwayGoals();
        viewHolder.score.setText(score);

        return rowView;
    }
}
