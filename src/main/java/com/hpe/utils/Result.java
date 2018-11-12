package com.hpe.utils;

import java.util.Objects;
import java.util.Optional;

public class Result<T> {
    private Optional<T> value;
    private Optional<String> error;

    private Result(T value, String error) {
        this.value = Optional.ofNullable(value);
        this.error = Optional.ofNullable(error);
    }

    public static <U> Result<U> ok(U value) {
        return new Result<>(value, null);
    }

    public static <U> Result<U> error(String error) {
        return new Result<>(null, error);
    }

    public boolean isError() {
        return error.isPresent();
    }

    public T getValue() {
        return value.get();
    }

    public String getError() {
        return error.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result<?> result = (Result<?>) o;
        return Objects.equals(value, result.value) &&
                Objects.equals(error, result.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, error);
    }

    @Override
    public String toString() {
        return String.format("Result<value=%s error=%s>", isError() ? "n/a" : value.get(), isError() ? error : "n/a");
    }
}