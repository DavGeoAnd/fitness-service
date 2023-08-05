package com.davgeoand.api.monitor.event.type;

import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Audit extends Event {
    private String requestPath;
    private String status;
    private String method;
    private String response;
    private Float requestDuration;
    private String traceId;

    @Override
    public Point toPoint() {
        return Point.measurement("audit")
                .addTag("requestPath", requestPath)
                .addTag("status", status)
                .addTag("method", method)
                .addField("response", response)
                .addField("requestDuration", requestDuration)
                .addField("traceId", traceId)
                .time(time, WritePrecision.MS);
    }
}
