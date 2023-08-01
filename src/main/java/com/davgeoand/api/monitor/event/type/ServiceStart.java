package com.davgeoand.api.monitor.event.type;

import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ServiceStart extends Event {
    private String buildVersion;
    private String gitBranch;
    private String gitCommitId;
    private long startTime;
    private long serviceStartDuration;

    @Override
    public Point toPoint() {
        return Point.measurement("serviceStart")
                .addField("buildVersion", buildVersion)
                .addField("gitBranch", gitBranch)
                .addField("gitCommitId", gitCommitId)
                .addField("startTime", startTime)
                .addField("serviceStartDuration", serviceStartDuration)
                .time(time, WritePrecision.MS);
    }
}
