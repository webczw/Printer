package com.communication.printer.domain;

import java.io.Serializable;

/**
 * @author Wilber
 * 品牌数据实体
 */
public class BrandVO implements Serializable {
    private Long brandId;
    private String brandName;

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    @Override
    public String toString() {
        return "BrandVO{" +
                "brandId=" + brandId +
                ", brandName='" + brandName + '\'' +
                '}';
    }
}
