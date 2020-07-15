package com.zoey.learning.commons.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Api result
 * Created in 2020.07.15
 *
 * @param <T> the type parameter
 * @author Liaozihong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResult {
    private Integer code;
    private String msg;
    private Object data;

    public ApiResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}