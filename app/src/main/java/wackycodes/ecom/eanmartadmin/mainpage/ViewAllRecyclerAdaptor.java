package wackycodes.ecom.eanmartadmin.mainpage;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import wackycodes.ecom.eanmartadmin.secondpage.BannerAndCatModel;
import wackycodes.ecom.eanmartadmin.secondpage.BannerItemAdaptor;

public class ViewAllRecyclerAdaptor extends BannerItemAdaptor{

    public ViewAllRecyclerAdaptor(String catOrCollectionID,
                                  int catIndex, List <BannerAndCatModel> bannerAndCatModelList,
                                  boolean isViewAll, int layoutIndex) {
        super( catOrCollectionID, catIndex, bannerAndCatModelList, isViewAll, layoutIndex );
    }




}