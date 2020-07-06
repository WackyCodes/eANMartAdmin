package wackycodes.ecom.eanmartadmin.secondpage;

public class BannerAndCatModel {

    private String layoutId;
    private String imageLink;
    //...
    private String clickID;
    private int clickType;
    private String name;
    private String extraText;

    public BannerAndCatModel(String layoutId, String imageLink, String clickID, int clickType, String name, String extraText) {
        this.layoutId = layoutId;
        this.imageLink = imageLink;
        this.clickID = clickID;
        this.clickType = clickType;
        this.name = name;
        this.extraText = extraText;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(String layoutId) {
        this.layoutId = layoutId;
    }

    public String getClickID() {
        return clickID;
    }

    public void setClickID(String clickID) {
        this.clickID = clickID;
    }

    public int getClickType() {
        return clickType;
    }

    public void setClickType(int clickType) {
        this.clickType = clickType;
    }

    public String getExtraText() {
        return extraText;
    }

    public void setExtraText(String extraText) {
        this.extraText = extraText;
    }
}
