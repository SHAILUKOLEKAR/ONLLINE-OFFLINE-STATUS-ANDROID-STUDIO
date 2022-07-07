package com.shailu.onlineoffline;

class News
{
    private String News_Description,News_Title,Image;

    public News() {
    }

    public News(String news_Description, String news_Title, String image) {
        News_Description = news_Description;
        News_Title = news_Title;
        Image = image;
    }

    public String getNews_Description() {
        return News_Description;
    }

    public void setNews_Description(String news_Description) {
        News_Description = news_Description;
    }

    public String getNews_Title() {
        return News_Title;
    }

    public void setNews_Title(String news_Title) {
        News_Title = news_Title;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
