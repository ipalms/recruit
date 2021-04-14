package com.geek.geekstudio.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装消息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private String toId;
    private String formId;
    private String word;
    private String time;
}
