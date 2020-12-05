package ean.ecom.eanmartadmin.multisection.aboutshop;

/**
 * Created by Shailendra (WackyCodes) on 05/12/2020 22:56
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */

public class VersionModel {
    private String versionName;
    private boolean isChecked;

    public VersionModel(String versionName, boolean isChecked) {
        this.versionName = versionName;
        this.isChecked = isChecked;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
