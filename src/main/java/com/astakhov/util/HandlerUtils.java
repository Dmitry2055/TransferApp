package com.astakhov.util;

import com.astakhov.dto.ErrorResponse;
import com.astakhov.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import static java.net.HttpURLConnection.*;

public class HandlerUtils {

    private static final Logger LOG = LoggerFactory.getLogger(HandlerUtils.class);

    public static void handleServiceException(final ServiceException e, final Request request, final Response response) {
        LOG.error(e.getMessage(), e);
        response.status(HTTP_BAD_REQUEST);
        ErrorResponse error = new ErrorResponse();
        error.setMessage("The operation could not be completed. Check what you are doing");
        response.body(ConverterUtils.toJson(error));
    }

    public static Object notFound(final Request request, final Response response) {
        response.status(HTTP_NOT_FOUND);
        ErrorResponse error = new ErrorResponse();
        error.setMessage("Not found");
        return ConverterUtils.toJson(error);
    }

    public static Object internalServerError(final Request request, final Response response) {
        response.status(HTTP_INTERNAL_ERROR);
        ErrorResponse error = new ErrorResponse();
        error.setMessage("An error occurred");
        return ConverterUtils.toJson(error);
    }
}
