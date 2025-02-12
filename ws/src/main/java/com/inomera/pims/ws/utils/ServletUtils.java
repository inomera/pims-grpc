package com.inomera.pims.ws.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import static org.apache.commons.lang3.StringUtils.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ServletUtils {

    public final static String CLIENT_IP_HEADER = "X-Forwarded-For";

    public static String getHeader(HttpServletRequest httpServletRequest, String headerName) {
        return httpServletRequest.getHeader(headerName);
    }

    public static String getIpAddress() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            return getIpAddress(request);
        }
        return EMPTY;
    }

    private static String getIpAddress(HttpServletRequest request) {
        final String remoteIp = request.getRemoteAddr();
        final String proxyIp = request.getHeader(CLIENT_IP_HEADER);
        if (StringUtils.isBlank(proxyIp)) {
            return remoteIp;
        }

        final String[] ipTokens = proxyIp.split(",");
        if (ipTokens.length > 1) {
            return defaultIfBlank(trim(ipTokens[ipTokens.length - 1]), remoteIp);
        }

        return defaultIfBlank(proxyIp, remoteIp);
    }
}
