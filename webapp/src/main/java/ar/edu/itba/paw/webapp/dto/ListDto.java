package ar.edu.itba.paw.webapp.dto;

import javax.ws.rs.core.GenericEntity;
import java.util.List;

public class ListDto<T> {
    private List<T> list;
    private int totalPages;

    public ListDto() {}

    public ListDto(List<T> list, int totalPages) {
        this.list = list;
        this.totalPages = totalPages;
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
}