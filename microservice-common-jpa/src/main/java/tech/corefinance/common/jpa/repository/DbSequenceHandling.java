package tech.corefinance.common.jpa.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface DbSequenceHandling {

    Long getCurrentSequenceValue(String sequenceName);

    Long getNextSequenceValue(String sequenceName);

    void restartSequence(long newVal, String sequenceName);

}
