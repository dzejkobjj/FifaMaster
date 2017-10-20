package dzejkobdevelopment.pl.fifamaster;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Dzejkob on 26.09.2017.
 */

public class TableAdapter extends ArrayAdapter<Player> {
    private Activity context;
    private List<Player> players;
    public TableAdapter(Activity context, List<Player> players){
        super(context, R.layout.table_item, players);
        this.context = context;
        this.players = players;
    }

    static class ViewHolder{
        public TextView position, name, played, won, drawed, lost, scored, conceded, goalDiff, points, winperc;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder;
        View rowView = convertView;

        if(rowView==null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            rowView = layoutInflater.inflate(R.layout.table_item, null, true);
            viewHolder = new ViewHolder();

            viewHolder.position = (TextView)rowView.findViewById(R.id.position);
            viewHolder.name = (TextView)rowView.findViewById(R.id.name);
            viewHolder.played = (TextView)rowView.findViewById(R.id.played);
            viewHolder.won = (TextView)rowView.findViewById(R.id.won);
            viewHolder.drawed = (TextView)rowView.findViewById(R.id.drawed);
            viewHolder.lost = (TextView)rowView.findViewById(R.id.lost);
            viewHolder.scored = (TextView)rowView.findViewById(R.id.scored);
            viewHolder.conceded = (TextView)rowView.findViewById(R.id.conceded);
            viewHolder.goalDiff = (TextView)rowView.findViewById(R.id.goalDifference);
            viewHolder.points = (TextView)rowView.findViewById(R.id.points);
            viewHolder.winperc = (TextView)rowView.findViewById(R.id.winPercent);

            rowView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) rowView.getTag();
        }

        Player player = players.get(position);
        viewHolder.position.setText(Integer.toString(position+1));
        viewHolder.name.setText(player.name);
        viewHolder.played.setText(Integer.toString(player.draw + player.lost + player.won));
        viewHolder.won.setText(Integer.toString(player.won));
        viewHolder.drawed.setText(Integer.toString(player.draw));
        viewHolder.lost.setText(Integer.toString(player.lost));
        viewHolder.scored.setText(Integer.toString(player.scored));
        viewHolder.conceded.setText(Integer.toString(player.conceded));
        viewHolder.goalDiff.setText(Integer.toString(player.scored-player.conceded));
        viewHolder.points.setText(Integer.toString(player.won*3 + player.draw));

        float percent = (((float)player.won)*100.0f)/((float)(player.draw+player.lost+player.won));
        DecimalFormat df = new DecimalFormat("#.#");
        viewHolder.winperc.setText(df.format(percent));

        return rowView;
    }
}
