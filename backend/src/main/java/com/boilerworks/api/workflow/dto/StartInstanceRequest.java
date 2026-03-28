package com.boilerworks.api.workflow.dto;

import lombok.Data;

import java.util.Map;

@Data
public class StartInstanceRequest {

    private String entityType;
    private String entityId;
    private Map<String, Object> contextData;
}
