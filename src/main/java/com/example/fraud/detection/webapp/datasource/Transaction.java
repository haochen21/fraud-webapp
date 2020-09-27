package com.example.fraud.detection.webapp.datasource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    private long transactionId;

    private long eventTime;

    private long payeeId;

    private long beneficiaryId;

    private BigDecimal paymentAmount;

    private PaymentType paymentType;

    public enum PaymentType  {

        WEIXINPAY("WEIXINPAY"), ALIPAY("ALIPAY");

        String representation;

        PaymentType(String representation) {
            this.representation = representation;
        }
    }
}
