package com.davgeoand.api.monitor.metric;

import io.javalin.micrometer.MicrometerPlugin;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.binder.jvm.*;
import io.micrometer.core.instrument.binder.logging.LogbackMetrics;
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceMetrics {
    public static MicrometerPlugin getMicrometerPlugin() {
        new ClassLoaderMetrics().bindTo(Metrics.globalRegistry);
        new JvmCompilationMetrics().bindTo(Metrics.globalRegistry);
        new JvmGcMetrics().bindTo(Metrics.globalRegistry);
        new JvmHeapPressureMetrics().bindTo(Metrics.globalRegistry);
        new JvmInfoMetrics().bindTo(Metrics.globalRegistry);
        new JvmMemoryMetrics().bindTo(Metrics.globalRegistry);
        new JvmThreadMetrics().bindTo(Metrics.globalRegistry);
        new LogbackMetrics().bindTo(Metrics.globalRegistry);
        new FileDescriptorMetrics().bindTo(Metrics.globalRegistry);
        new ProcessorMetrics().bindTo(Metrics.globalRegistry);
        new UptimeMetrics().bindTo(Metrics.globalRegistry);
        return MicrometerPlugin.Companion.create(micrometerConfig -> micrometerConfig.registry = Metrics.globalRegistry);
    }
}
