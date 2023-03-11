package com.money.entity;

import com.money.mb.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author money
 * @since 2023-02-27
 */
@Getter
@Setter
@Schema(description = "")
public class Provinces extends BaseEntity {

    private String districtId;

    private String province;

    private String city;

    private String cityGeocode;

    private String district;

    private String districtGeocode;

    private String lon;

    private String lat;

}
