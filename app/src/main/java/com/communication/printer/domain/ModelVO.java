package com.communication.printer.domain;

import java.io.Serializable;

/**
 * @author Wilber
 * 型号数据实体
 */
public class ModelVO implements Serializable {
    private Long modelId;
    private String modelName;

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @Override
    public String toString() {
        return "ModelVO{" +
                "modelId=" + modelId +
                ", modelName='" + modelName + '\'' +
                '}';
    }
}
