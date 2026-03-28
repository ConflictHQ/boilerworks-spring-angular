package com.boilerworks.api.workflow.dto;

import java.util.Map;
import lombok.Data;

@Data
public class StartInstanceRequest {

  private String entityType;
  private String entityId;
  private Map<String, Object> contextData;
}
