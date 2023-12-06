package tech.corefinance.common.mongodb.support.model;

import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import tech.corefinance.common.dto.BasicUserDto;
import tech.corefinance.common.mongodb.model.IdSequentialModel;

import java.time.ZonedDateTime;

@Document("custom_sequential_id_data")
@Data
public class CustomSequentialIdData implements IdSequentialModel {

    @Id
    private Long id;
    private String name;
    @CreatedDate
    private ZonedDateTime createdDate;
    @LastModifiedDate
    private ZonedDateTime lastModifiedDate;
    @CreatedBy
    private BasicUserDto createdBy;
    @LastModifiedBy
    private BasicUserDto lastModifiedBy;
}
