package a5236.android_game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jared on 3/25/2018.
 */

public class PlayersAdapter extends ArrayAdapter<Player> {
    public PlayersAdapter(Context context, ArrayList<Player> players){
        super(context, 0, players);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Player player= getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_player, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.Name);
        TextView tvScore = (TextView) convertView.findViewById(R.id.Score);
        // Populate the data into the template view using the data object
        tvName.setText(player.getUsername());
        tvScore.setText(player.getPoints());
        // Return the completed view to render on screen
        return convertView;
    }
}