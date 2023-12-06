package tech.corefinance.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PageDto<T> extends GeneralApiResponse<List<T>> {

    /**
     * Generated!
     */
    private static final long serialVersionUID = -9141677225193449525L;

    private List<OrderDto> orders;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;

    public PageDto() {}

    public PageDto(String statusCode, int status, Page<T> page) {
        super(statusCode, status, page.getContent());
        setPageData(page);
    }

    public PageDto(Page<T> page) {
        super(page.getContent());
        setPageData(page);
    }

    @Schema(hidden = true)
    public void setPageData(Page<T> page) {
        setResult(page.getContent());
        this.orders = page.getSort().stream().map(OrderDto::fromOrder).toList();
        this.pageNumber = page.getNumber();
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }

    public boolean isEmpty() {
        return getResult().isEmpty();
    }

    public int getNumberOfElements() {
        return getResult().size();
    }

    public static <T> PageDto<T> createSuccessResponse(Page<T> page) {
        return new PageDto<T>("OK", STATUS_SUCCESS, page);
    }

    public boolean isHasNext() {
        return pageNumber < totalPages - 1;
    }

    public boolean isHasPrevious() {
        return pageNumber > 0;
    }

    public boolean isFirst() {
        return this.pageNumber <= 0;
    }

    public boolean isLast() {
        return this.pageNumber >= totalPages - 1;
    }
}
