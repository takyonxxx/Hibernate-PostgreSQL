package com.biliyor.entity;

import java.sql.Date;

import javax.persistence.Embeddable;

@Embeddable
public class Address {

    private String city;   
    private Date date;

    public Address() {
    }

    public Address(String city, Date date) {        
        this.city = city;       
        this.date = date;
    }  

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }   

    public Date getDate() {
		return date;
	}

	public void setDate(Date sdate) {
		this.date = sdate;
	}

	@Override
    public String toString() {
        return "Address{" +                
                "city='" + city + '\'' +    
                ",date='" + date + '\'' +
                '}';
    }
}
