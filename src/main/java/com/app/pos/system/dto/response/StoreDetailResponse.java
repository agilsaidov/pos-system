package com.app.pos.system.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter @Setter
public class StoreDetailResponse {
    private Long storeId;
    private String name;
    private String address;
    private String city;
    private String phone;
    private Boolean active;
    private OffsetDateTime openedAt;
    private Integer totalStaff;
}
