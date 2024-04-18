package com.github.amitsk.sunrise.errors;

import com.nike.backstopper.apierror.ApiError;
import com.nike.backstopper.apierror.ApiErrorBase;
import com.nike.backstopper.apierror.ApiErrorWithMetadata;

import java.util.Map;
import java.util.UUID;

public enum SunriseApiError implements ApiError {
    GENERIC_SERVICE_ERROR(10, "An error occurred while fulfilling the request", 500),
    // Mirrors GENERIC_SERVICE_ERROR for the caller, but will show up in the logs with a different name
    GENERIC_BAD_REQUEST(20, "Invalid Values passed for Lat or Lon", 400);

    private final ApiError delegate;

    SunriseApiError(ApiError delegate) { this.delegate = delegate; }

    SunriseApiError(ApiError delegate, Map<String, Object> additionalMetadata) {
        this(new ApiErrorWithMetadata(delegate, additionalMetadata));
    }

    SunriseApiError(int errorCode, String message, int httpStatusCode) {
        this(errorCode, message, httpStatusCode, null);
    }

    SunriseApiError(int errorCode, String message, int httpStatusCode, Map<String, Object> metadata) {
        this(new ApiErrorBase(
                "delegated-to-enum-name-" + UUID.randomUUID().toString(), errorCode, message, httpStatusCode,
                metadata
        ));
    }

    @Override
    public String getName() { return this.name(); }

    @Override
    public String getErrorCode() { return delegate.getErrorCode(); }

    @Override
    public String getMessage() { return delegate.getMessage(); }

    @Override
    public int getHttpStatusCode() { return delegate.getHttpStatusCode(); }

    @Override
    public Map<String, Object> getMetadata() { return delegate.getMetadata(); }

}