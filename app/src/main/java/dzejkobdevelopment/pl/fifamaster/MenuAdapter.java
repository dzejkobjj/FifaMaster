package dzejkobdevelopment.pl.fifamaster;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Dzejkob on 03.09.2017.
 */

public class MenuAdapter extends BaseAdapter {

    private final Context context;
    private final MainMenuItem[] menuItems;

    public MenuAdapter(Context context, MainMenuItem[] menuItems){
        this.context = context;
        this.menuItems = menuItems;
    }

    @Override
    public int getCount(){
        return  menuItems.length;
    }

    @Override
    public long getItemId(int position){
        return menuItems[position].getId();
    }

    @Override
    public Object getItem(int position){
        return menuItems[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MainMenuItem menuItem = menuItems[position];

        if(convertView == null){
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.linearlayout_menuitem, null);
        }

        final ImageView imageView = (ImageView)convertView.findViewById(R.id.imageview_menuitem_icon);
        final TextView textView = (TextView)convertView.findViewById(R.id.textview_menuitem_title);

        imageView.setImageResource(menuItem.getImageUrl());
        textView.setText(context.getString(menuItem.getTitle()));

        return convertView;
    }

}
