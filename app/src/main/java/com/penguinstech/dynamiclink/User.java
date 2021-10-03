package com.penguinstech.dynamiclink;

public class User {



    public String firstname,uid, surname,about, city, country, cname, role,primaryEmail, profile, search, userindustry,
        youtube, website, twitter, insta, linkedIn, facebook;

    public User(String firstname, String uid, String surname, String about,  String city,  String country, String  cname, String role, String primaryEmail,
                String profile,  String search,  String userindustry, String youtube,  String website,  String twitter,  String insta,  String linkedIn,
                String facebook){
        this.firstname = firstname;
        this.uid = uid;
        this.surname = surname;
        this.about = about;
        this.city = city;
        this.country = country;
        this.cname = cname;
        this.role = role;
        this.primaryEmail = primaryEmail;
        this.profile = profile;
        this.search = search;
        this.userindustry = userindustry;
        this.youtube = youtube;
        this.website = website;
        this.twitter = twitter;
        this.insta = insta;
        this.linkedIn = linkedIn;
        this.facebook = facebook;
    }
}
