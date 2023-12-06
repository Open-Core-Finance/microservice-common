package tech.corefinance.product.model;

import lombok.Data;
import tech.corefinance.product.enums.WithdrawalLimitType;

@Data
public class WithdrawalLimit {

    /**
     * Limit type.
     */
    private WithdrawalLimitType type;
    /**
     * Channel ID null mean apply for all channel.
     */
    private String channelId;
    /**
     * Limit amount.
     */
    private double amount;
}
