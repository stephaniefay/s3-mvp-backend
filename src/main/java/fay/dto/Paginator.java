package fay.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RegisterForReflection
public class Paginator {

    private Integer page;
    private Integer size;

    public Paginator(Integer page, Integer size) {
        this.page = page == null ? 1 : page < 1 ? 1 : page;
        this.size = size == null ? 20 : size < 1 ? 20 : Math.max(size, 50);
    }

    public int getFirstIndex() {
        return (page - 1) * size;
    }
}
