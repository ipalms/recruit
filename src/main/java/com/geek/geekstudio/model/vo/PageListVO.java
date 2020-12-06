package com.geek.geekstudio.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
  *封装分页数据集合
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageListVO {

    private int total;
    private int currentPage;
    private int totalPage;
    private int rows;
    private List items;

}
