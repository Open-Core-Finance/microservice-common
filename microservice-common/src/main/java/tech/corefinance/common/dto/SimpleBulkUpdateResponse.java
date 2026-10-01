package tech.corefinance.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleBulkUpdateResponse implements BulkUpdateResponse {
    private List<BulkUpdateResponseItem> items;
}
