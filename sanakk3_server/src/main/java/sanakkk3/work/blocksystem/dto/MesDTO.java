package sanakkk3.work.blocksystem.dto;

import lombok.Data;

@Data
public class MesDTO<T> {
    private T data;
    private String code;
    private String message;
}
