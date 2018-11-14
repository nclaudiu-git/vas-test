package com.hpe.data;

import com.hpe.utils.Result;

import java.util.Objects;

/**
 * A key-value pair in the JSON.
 */
public class KeyValue<T> {
    private Field field;
    private Result<T> result;

    public KeyValue(Field field, Result<T> result) {
        this.field = field;
        this.result = result;
    }

    public Field getField() {
        return field;
    }

    public Result<T> getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyValue<?> keyValue = (KeyValue<?>) o;
        return field == keyValue.field &&
                Objects.equals(result, keyValue.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, result);
    }

    @Override
    public String toString() {
        return "KeyValue<field=" + field + ", result=" + result + ">";
    }
}
