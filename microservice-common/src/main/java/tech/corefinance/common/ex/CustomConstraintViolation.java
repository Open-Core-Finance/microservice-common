package tech.corefinance.common.ex;

import lombok.*;

import java.util.Comparator;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "description")
public class CustomConstraintViolation implements Comparator<CustomConstraintViolation> {
    private final String messageKey;
    private String description;

    @Override
    public int compare(CustomConstraintViolation o1, CustomConstraintViolation o2) {
        return o1.messageKey.compareTo(o2.messageKey);
    }
}
