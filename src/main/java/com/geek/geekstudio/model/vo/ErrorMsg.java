package com.geek.geekstudio.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装出现的异常返回给前端 处理多条数据时存放异常的地方
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMsg {

    private int type;
    private String message;
    private String reason;

}
