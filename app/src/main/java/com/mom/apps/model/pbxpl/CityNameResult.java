package com.mom.apps.model.pbxpl;


import com.mom.apps.model.CityName;

/**
 * Created by akanksha
 */
public class CityNameResult {

    public String cityName;

    public CityNameResult(){}
    public CityNameResult(String name){

        this.cityName   = name;
    }



    public String getName() {
        return cityName;
    }

    public String toString(){
        return cityName;
    }


    public CityName getCity (){
        CityName cityname = new CityName();

        cityname.name = cityName;

        return cityname;
    }
}
