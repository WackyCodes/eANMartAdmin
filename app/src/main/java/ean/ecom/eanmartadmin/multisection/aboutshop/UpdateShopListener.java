package ean.ecom.eanmartadmin.multisection.aboutshop;

/**
 * Created by Shailendra (WackyCodes) on 05/12/2020 23:18
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */

public interface UpdateShopListener {

    void showToast(String msg);
    void showDialog();
    void dismissDialog();

    interface UpdateListener{
        void onUpdateResponseAddMember(boolean isSuccess);
        void onUpdateResponseVersion(boolean isSuccess);
        void onUpdateResponseOther(boolean isSuccess);
    }

    interface LoadListListener{
        void onResponse(boolean isSuccess);
    }


}
