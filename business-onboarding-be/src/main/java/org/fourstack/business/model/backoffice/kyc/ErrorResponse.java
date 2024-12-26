package org.fourstack.business.model.backoffice.kyc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
  private String requestMsg;
  private String errorCode;
  private String errorMsg;
  private String errorDetail;
}
