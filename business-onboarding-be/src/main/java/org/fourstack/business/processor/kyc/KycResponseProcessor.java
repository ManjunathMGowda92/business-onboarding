package org.fourstack.business.processor.kyc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class KycResponseProcessor {
  private static final Logger logger = LoggerFactory.getLogger(KycResponseProcessor.class);

  public void processKycResponse(String message) {
    logger.info("Consumed the KYC response from back-office : {}", message);

    // transform the message
    // validate the message for status from BO
    // validate the message for fields and formats
    // validate the message for existence of business
    // Activate the business - by checking the version is activated or not.
    // send the kyc activation response to AI's and broadcast for other AI's.
  }
}
