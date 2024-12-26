package org.fourstack.business.model.backoffice.kyc;

import lombok.Data;
import org.fourstack.business.enums.OperationStatus;

import java.util.List;

@Data
public class KycBusinessResponse {
  private String txnId;
  private String timeStamp;
  private String objectId;
  private List<OuKycResponse> kycOuResponse;
  private OperationStatus kycStatus;
  private ErrorResponse errorResponse;
}
