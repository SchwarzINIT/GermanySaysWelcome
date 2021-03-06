package com.github.socialc0de.gsw.customClasses.api;

public class FaqCategory {

    private Integer id;
    private Translations translations;
    private String image;

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The translations
     */
    public Translations getTranslations() {
        return translations;
    }

    /**
     * @param translations The translations
     */
    public void setTranslations(Translations translations) {
        this.translations = translations;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
