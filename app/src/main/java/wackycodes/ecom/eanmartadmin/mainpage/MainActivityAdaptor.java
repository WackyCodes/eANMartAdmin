package wackycodes.ecom.eanmartadmin.mainpage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import wackycodes.ecom.eanmartadmin.R;
import wackycodes.ecom.eanmartadmin.addnewitem.AddNewShopActivity;
import wackycodes.ecom.eanmartadmin.secondpage.SecondActivity;

import static wackycodes.ecom.eanmartadmin.MainActivity.mainPageList;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.REQUEST_TO_ADD_SHOP;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.REQUEST_TO_EDIT_SHOP;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.REQUEST_TO_VIEW_HOME;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.REQUEST_TO_VIEW_SHOP;

public class MainActivityAdaptor extends BaseAdapter {

    public MainActivityAdaptor() {
    }

    @Override
    public int getCount() {
        return mainPageList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.home_grid_view_item, null );
        ImageView itemImage = view.findViewById( R.id.image );
        TextView itemName =  view.findViewById( R.id.name );

        itemImage.setImageResource( mainPageList.get( position ).getImage() );
        itemName.setText( mainPageList.get( position ).getName() );

        view.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnClick( parent.getContext(), mainPageList.get( position ).getID() );
            }
        } );

        return view;

    }

    private void setOnClick(Context context, int ID){
        switch (ID){
            case REQUEST_TO_ADD_SHOP:
                Intent addShopIntent = new Intent( context, AddNewShopActivity.class );
                context.startActivity( addShopIntent );
                break;
            case REQUEST_TO_EDIT_SHOP:
            case REQUEST_TO_VIEW_SHOP:
            case REQUEST_TO_VIEW_HOME:
                Intent viewHomeIntent = new Intent( context, SecondActivity.class );
                context.startActivity( viewHomeIntent );
                break;
            default:
                Toast.makeText( context, "Code not found!", Toast.LENGTH_SHORT ).show();
                break;
        }
    }

}
