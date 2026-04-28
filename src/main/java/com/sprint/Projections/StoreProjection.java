package com.sprint.Projections;

import java.sql.Timestamp;

import org.springframework.data.rest.core.config.Projection;

import com.sprint.Entities.Store;

@Projection(name = "storeProjection", types = Store.class)
public interface StoreProjection {

    Long getStoreId();
    Timestamp getLastUpdate();

    AddressView getAddress();
    StaffView getManagerStaff();

    interface AddressView {
        Long getAddressId();
        String getAddress();
        String getDistrict();

        CityView getCity();

        interface CityView {
            String getCity();
        }
    }

    interface StaffView {
        Long getStaffId();
        String getFirstName();
        String getLastName();
    }
}