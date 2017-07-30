package com.mwb.dao.modle;

/**
 * Created by MengWeiBo on 2017-03-28
 */
public class PagingData {
    public static final int MAX_PAGE_SIZE = 2000;
    private int pageNumber;
    private int pageSize;

    public PagingData(int pageNumber, int pageSize) {
        if(pageSize < 1) {
            pageSize = 1;
        }

        if(pageSize > 2000) {
            pageSize = 2000;
        }

        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public int getStartRecordNumber() {
        int startRecordNumber = (this.pageNumber - 1) * this.pageSize;
        return startRecordNumber;
    }

    public int getPageNumber() {
        return this.pageNumber;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getFromIndex() {
        return (this.pageNumber - 1) * this.pageSize;
    }

    public int getToIndex() {
        return this.pageNumber * this.pageSize;
    }

    public int getToIndex(int maxIndex) {
        int toIndex = this.pageNumber * this.pageSize;
        toIndex = toIndex > maxIndex?maxIndex:toIndex;
        return toIndex;
    }
}
