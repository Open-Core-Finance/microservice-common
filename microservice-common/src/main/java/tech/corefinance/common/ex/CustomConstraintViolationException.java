package tech.corefinance.common.ex;

import jakarta.validation.ValidationException;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = false)
@Data
public class CustomConstraintViolationException extends ValidationException {
    private final Set<CustomConstraintViolation> constraintViolations;

    public CustomConstraintViolationException(String message, Set<? extends CustomConstraintViolation> constraintViolations) {
        super(message);

        if (constraintViolations == null) {
            this.constraintViolations = null;
        } else {
            this.constraintViolations = new HashSet<>(constraintViolations);
        }
    }

    public CustomConstraintViolationException(Set<? extends CustomConstraintViolation> constraintViolations) {
        this(constraintViolations != null ? toString(constraintViolations) : null, constraintViolations);
    }

    public CustomConstraintViolationException(Collection<String> constraintViolations) {
        this(constraintViolations.stream().map(CustomConstraintViolation::new).collect(Collectors.toSet()));
    }

    private static String toString(Set<? extends CustomConstraintViolation> constraintViolations) {
        return constraintViolations.stream().map(cv -> cv == null ? "null" : cv.getMessageKey()).collect(Collectors.joining(", "));
    }
}
