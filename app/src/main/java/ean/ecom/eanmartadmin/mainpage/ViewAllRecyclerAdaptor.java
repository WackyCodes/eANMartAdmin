package ean.ecom.eanmartadmin.mainpage;

import java.util.List;

import ean.ecom.eanmartadmin.mainpage.homesection.BannerAndCatModel;
import ean.ecom.eanmartadmin.mainpage.homesection.BannerItemAdaptor;

public class ViewAllRecyclerAdaptor extends BannerItemAdaptor{

    public ViewAllRecyclerAdaptor(String catOrCollectionID,
                                  int catIndex, List <BannerAndCatModel> bannerAndCatModelList,
                                  boolean isViewAll, int layoutIndex) {
        super( catOrCollectionID, catIndex, bannerAndCatModelList, isViewAll, layoutIndex );
    }




}