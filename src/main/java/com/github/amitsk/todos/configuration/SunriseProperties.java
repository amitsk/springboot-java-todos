package com.github.amitsk.todos.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("sunrise")
public class SunriseProperties {
    private SunriseApi sunriseApi;

    public SunriseApi getSunriseApi() {
        return sunriseApi;
    }

    public void setSunriseApi(SunriseApi sunriseApi) {
        this.sunriseApi = sunriseApi;
    }


    public static class SunriseApi {
        private  String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
