package ean.ecom.eanmartadmin.multisection.shoplist;

import androidx.annotation.Nullable;

/**
 * Created by Shailendra (WackyCodes) on 06/12/2020 21:57
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public interface ShopListListener {
    void onLoadResponse( boolean isSuccess,@Nullable String responseMsg);
}
