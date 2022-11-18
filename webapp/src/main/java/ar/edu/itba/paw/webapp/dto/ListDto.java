package ar.edu.itba.paw.webapp.dto;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

public class ListDto<T> {
    private List<T> list;
    private int totalPages;

    private URI prev;
    private URI next;

    public static <T> ListDto<T> from(List<T> list, int totalPages, int page, UriInfo uriInfo) {
        ListDto<T> dto = new ListDto<>();

        dto.list = list;
        dto.totalPages = totalPages;

        if (page > 0) {
            dto.prev = uriInfo.getRequestUriBuilder().replaceQueryParam("page", page - 1).build();
        }

        if (page < totalPages - 1) {
            dto.next = uriInfo.getRequestUriBuilder().replaceQueryParam("page", page + 1).build();
        }

        return dto;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public URI getPrev() {
        return prev;
    }

    public void setPrev(URI prev) {
        this.prev = prev;
    }

    public URI getNext() {
        return next;
    }

    public void setNext(URI next) {
        this.next = next;
    }
}