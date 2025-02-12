package com.inomera.pims.ws.filter;

import com.inomera.pims.ws.utils.SensitiveDataReplacer;
import com.inomera.pims.ws.utils.ServletUtils;
import com.inomera.sal.util.MdcUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;


@Slf4j
@RequiredArgsConstructor
public class LoggingFilter extends OncePerRequestFilter {

    private static final String[] SEARCH_CHARS = new String[]{"\n", "\\r\\n", "\\"};
    private static final String[] REPLACE_CHARS = new String[]{",", "", ""};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        Instant requestTime = Instant.now();
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        try {
            final String logTrackKey = request.getHeader(MdcUtils.MDC_LOG_TRACK_ID);
            MdcUtils.setLogTrackKey(StringUtils.defaultIfBlank(logTrackKey, MdcUtils.generateNewLogTrackKey()));
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            Instant responseTime = Instant.now();
            try {
                handleLogging(wrappedRequest, wrappedResponse, requestTime, responseTime, request, response);
            } catch (Exception e) {
                LOG.warn("Error occurred while logging request and response", e);
            }
            wrappedResponse.copyBodyToResponse();
        }
    }

    private void handleLogging(ContentCachingRequestWrapper wrappedRequest,
                               ContentCachingResponseWrapper wrappedResponse,
                               Instant requestTime, Instant responseTime, HttpServletRequest request,
                               HttpServletResponse response) {
        final String url = request.getRequestURI();
        final String method = request.getMethod();
        int result = response.getStatus();
        final String clientIp = ServletUtils.getIpAddress();

        final String clearedMaskedRequest = getClearedMaskedRequestOrResponse(wrappedRequest.getContentAsByteArray());
        final String clearedMaskedResponse = getClearedMaskedRequestOrResponse(wrappedResponse.getContentAsByteArray());
        if (result >= 200 && result < 300) {
            final HttpLog log = new HttpLog.HttpLogBuilder()
                    .clientIp(clientIp)
                    .url(url)
                    .method(method)
                    .request(clearedMaskedRequest)
                    .response(clearedMaskedResponse)
                    .requestTime(requestTime.toEpochMilli())
                    .responseTime(responseTime.toEpochMilli())
                    .build();
            LOG.debug("success : {}", cleanLogText(log.toString()));
            return;
        }
        final HttpLog log = new HttpLog.HttpLogBuilder()
                .clientIp(clientIp)
                .url(url)
                .method(method)
                .request(clearedMaskedRequest)
                .response(clearedMaskedResponse)
                .requestTime(requestTime.toEpochMilli())
                .responseTime(responseTime.toEpochMilli())
                .build();
        LOG.debug("fail : {}", cleanLogText(log.toString()));
    }

    private String getClearedMaskedRequestOrResponse(byte[] wrappedRequestOrResponseByteArray) {
        final String cachedBody = new String(wrappedRequestOrResponseByteArray, StandardCharsets.UTF_8);
        final String maskedBody = SensitiveDataReplacer.maskPassword(cachedBody);
        return cleanLogText(maskedBody);
    }

    private String cleanLogText(String text) {
        final String logText = StringUtils.trim(text);
        return StringUtils.replaceEach(logText, SEARCH_CHARS, REPLACE_CHARS);
    }

    @Builder
    record HttpLog(String clientIp, String url, String method, String request, String response, Long requestTime,
                   Long responseTime) {
    }
}



