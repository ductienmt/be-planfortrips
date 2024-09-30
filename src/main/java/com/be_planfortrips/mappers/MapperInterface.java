package com.be_planfortrips.mappers;

/**
 * Đây là một interface tổng quát dùng để ánh xạ giữa DTOs, Entities và Responses.
 *
 * @param <R> Kiểu của đối tượng Response.
 * @param <E> Kiểu của đối tượng Entity.
 * @param <D> Kiểu của đối tượng DTO.
 */
public interface MapperInterface<R,E,D> {

    /**
     * Chuyển đổi một đối tượng DTO thành một đối tượng Entity.
     *
     * @param d Đối tượng DTO cần chuyển đổi.
     * @return Đối tượng Entity đã chuyển đổi.
     */
    E toEntity(D d);

    /**
     * Chuyển đổi một đối tượng Entity thành một đối tượng Response.
     *
     * @param e Đối tượng Entity cần chuyển đổi.
     * @return Đối tượng Response đã chuyển đổi.
     */
    R toResponse(E e);

    /**
     * Cập nhật một đối tượng Entity hiện có bằng cách sử dụng dữ liệu từ một đối tượng DTO.
     *
     * @param d Đối tượng DTO chứa dữ liệu mới.
     * @param e Đối tượng Entity hiện có cần được cập nhật.
     */
    void updateEntityFromDto(D d, E e);
}