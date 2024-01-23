package com.tcc.beautyplannerpro.activity.data;

public abstract class Result<T> {
    private Result() {}

    public static <T> Result<T> success(T data) {
        return new Success<>(data);
    }

    public static <T> Result<T> error(Exception exception) {
        return new Error<>(exception);
    }

    public static final class Success<T> extends Result<T> {
        private final T data;

        public Success(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }

        @Override
        public String toString() {
            return "Success[data=" + data + "]";
        }
    }

    public static final class Error<T> extends Result<T> {
        private final Exception exception;

        public Error(Exception exception) {
            this.exception = exception;
        }

        public Exception getException() {
            return exception;
        }

        @Override
        public String toString() {
            return "Error[exception=" + exception + "]";
        }
    }
}