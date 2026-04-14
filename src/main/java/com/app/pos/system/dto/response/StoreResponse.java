package com.app.pos.system.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StoreResponse {
    private Long storeId;
    private String name;
    private String address;
    private Boolean active;
}
