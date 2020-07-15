package com.zoey.learning.seataorder.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TOrder implements Serializable {
    /*
     * 添加lombok 依赖 
      <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.4</version>
      </dependency>
    */
    private static final long serialVersionUID = 744745297325680206L;
    private Long id;
    private Long userId;
    private Long productId;
    private Integer count;
    private Double money;
    private Integer status;


}