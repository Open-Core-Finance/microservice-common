package tech.corefinance.common.dto;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Comparator;

@Component
public class SimpleVersionComparator implements Comparator<SimpleVersion>, Serializable {

    private static final long serialVersionUID = -5215835515735729771L;

    @Override
    public int compare(SimpleVersion version1, SimpleVersion version2) {
        int result = version1.getMajor() - version2.getMajor();
        if (result == 0) {
            return version1.getMinor() - version2.getMinor();
        }
        return result;
    }

}
