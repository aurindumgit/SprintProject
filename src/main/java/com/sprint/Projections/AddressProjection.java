package com.sprint.Projections;

import org.springframework.data.rest.core.config.Projection;

import com.sprint.Entities.Address;

@Projection(name = "addressProjection", types = Address.class)
public interface AddressProjection {

    Long getAddressId();
    String getAddress();
    String getAddress2();
    String getDistrict();
    String getPostalCode();
    String getPhone();

    CityProjection getCity();

    interface CityProjection {
        String getCity();
    }
}