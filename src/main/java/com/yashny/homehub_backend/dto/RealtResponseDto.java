package com.yashny.homehub_backend.dto;

import com.yashny.homehub_backend.entities.Realt;

import java.util.List;

public class RealtResponseDto {
    private List<Realt> realts;
    private long totalCount;

    public RealtResponseDto(List<Realt> realts, long totalCount) {
        this.realts = realts;
        this.totalCount = totalCount;
    }

    public List<Realt> getRealts() {
        return realts;
    }

    public void setRealts(List<Realt> realts) {
        this.realts = realts;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
