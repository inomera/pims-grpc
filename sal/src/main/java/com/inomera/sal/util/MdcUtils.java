package com.inomera.sal.util;

import io.grpc.Metadata;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.UUID;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MdcUtils {
    public static final String MDC_LOG_TRACK_ID = "logTrackKey";

    public static final Metadata.Key<String> MDC_LOG_TRACK_KEY = Metadata.Key.of(MDC_LOG_TRACK_ID,
            Metadata.ASCII_STRING_MARSHALLER);

    /**
     * Retrieve log track key from grpc call metadata
     * @param metadata
     * @return
     */
    public static String getOrGenerateLogTrackKey(Metadata metadata) {
        final String logTrackKey = metadata.get(MDC_LOG_TRACK_KEY);
        if (StringUtils.isNotBlank(logTrackKey)) {
            return logTrackKey;
        }
        final String localLogTrackKey = getLogTrackKey();
        if (StringUtils.isNotBlank(localLogTrackKey)) {
            return localLogTrackKey;
        }
        return generateAndSetLogTrackKey();
    }

    public static String getOrGenerateLogTrackKey() {
        final var logTrackKey = getLogTrackKey();
        if (StringUtils.isNotBlank(logTrackKey)) {
            return logTrackKey;
        }
        return generateAndSetLogTrackKey();
    }

    public static String getLogTrackKey() {
        return MDC.get(MDC_LOG_TRACK_ID);
    }

    public static void setLogTrackKey(String logTrackKey) {
        MDC.put(MDC_LOG_TRACK_ID, logTrackKey);
    }

    public static String generateAndSetLogTrackKey() {
        final var generatedLogTrackKey = generateNewLogTrackKey();
        setLogTrackKey(generatedLogTrackKey);
        return generatedLogTrackKey;
    }

    public static String generateNewLogTrackKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static void removeLogTrackKey() {
        MDC.remove(MDC_LOG_TRACK_ID);
    }


}
