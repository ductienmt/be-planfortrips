package com.be_planfortrips.exceptions;

import lombok.Getter;

/**
 * Lớp này đại diện cho một ngoại lệ tùy chỉnh trong ứng dụng.
 * Nó kế thừa từ RuntimeException, có nghĩa là nó là một ngoại lệ không kiểm tra.
 * Nó chứa một đối tượng ErrorType để cung cấp thêm chi tiết về ngoại lệ.
 */
@Getter
public class AppException extends RuntimeException{

    /**
     * Loại lỗi đã gây ra ngoại lệ.
     */
    private ErrorType errorType;
    private Object dataError;

    /**
     * Xây dựng một AppException mới với loại lỗi đã chỉ định.
     *
     * @param errorType Loại lỗi đã gây ra ngoại lệ.
     */

    public AppException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
    public AppException(ErrorType errorType, Object dataError) {
            super(errorType.getMessage());
            this.errorType = errorType;
            this.dataError = dataError;
    }

}
