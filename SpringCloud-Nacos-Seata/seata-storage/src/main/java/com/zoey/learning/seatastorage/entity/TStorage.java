package com.zoey.learning.seatastorage.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * T storage
 * Created in 2020.07.15
 *
 * @author Liaozihong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TStorage implements Serializable {
    private static final long serialVersionUID = 764098596267149848L;
    private Long id;
    private Long productId;
    private Long total;
    private Integer used;
    private Double residue;


}