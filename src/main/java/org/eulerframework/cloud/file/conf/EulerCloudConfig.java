package org.eulerframework.cloud.file.conf;

import org.springframework.context.annotation.Configuration;

@Configuration
public class EulerCloudConfig {
    private String runtimePath;

    public String getRuntimePath() {
        return runtimePath;
    }

    public void setRuntimePath(String runtimePath) {
        this.runtimePath = runtimePath;
    }
}
