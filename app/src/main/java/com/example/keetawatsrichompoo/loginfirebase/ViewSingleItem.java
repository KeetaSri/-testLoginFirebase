package com.example.keetawatsrichompoo.loginfirebase;

/**
 * Created by Keetawat Srichompoo on 15-Mar-18.
 */

public class ViewSingleItem {

    //Same name with the database
    private String Image_URL, Image_Title;

    public ViewSingleItem( String img_URL, String img_title ) {
        Image_URL = img_URL;
        Image_Title = img_title;
    }

    public ViewSingleItem() {

    }

    public String getImage_URL() {
        return Image_URL;
    }

    public String getImage_Title() {
        return Image_Title;
    }

    public void setImage_URL(String image_URL) {
        Image_URL = image_URL;
    }

    public void setImage_Title(String image_Title) {
        Image_Title = image_Title;
    }


}
