package tech.corefinance.product.common.model;

import lombok.Data;
import tech.corefinance.product.common.enums.WithdrawalLimitType;

@Data
public class WithdrawalLimit {

    private String currencyId;
    private String currencyName;

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
    private double value;
}
