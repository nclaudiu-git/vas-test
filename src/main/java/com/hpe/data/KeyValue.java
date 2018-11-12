package com.hpe.data;

import com.hpe.utils.Result;

import java.util.Objects;

/**
 * A key-value pair in the JSON.
 */
public class KeyValue<T> {
    private Field field;
    private Result<T> value;

    public KeyValue(Field field, Result<T> value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyValue<?> keyValue = (KeyValue<?>) o;
        return field == keyValue.field &&
                Objects.equals(value, keyValue.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, value);
    }

    @Override
    public String toString() {
        return "KeyValue<field=" + field + ", value=" + value + ">";
    }
}
