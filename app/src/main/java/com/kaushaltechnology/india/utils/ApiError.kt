package com.kaushaltechnology.india.utils

enum class ApiError(val code: Int, val message: String) {
    BAD_REQUEST(400, "Bad Request: Your request is invalid."),
    UNAUTHORIZED(401, "Unauthorized: Invalid API key."),
    FORBIDDEN(403, "Forbidden: You have reached your daily quota. Reset at 00:00 UTC."),
    TOO_MANY_REQUESTS(429, "Too Many Requests: Slow down! You're exceeding the rate limit."),
    NOT_FOUND(404, "Error: Resource Not Found"),
    INTERNAL_SERVER_ERROR(500, "Error: Internal Server Error"),
    SERVICE_UNAVAILABLE(
        503,
        "Service Unavailable: Server is down for maintenance. Try again later."
    ),
    UNKNOWN(-1, "Unknown error occurred. Please try again.");

    companion object {
        fun fromCode(code: Int): ApiError {
            return entries.find { it.code == code } ?: UNKNOWN
        }
    }
}

