package com.jinmy.onlinejudge.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "onlinejudge")
public class Config {
    private String root_dir;
    private String src_dir;
    private String data_dir;
    private String exc_dir;
    private String out_dir;
    private Judge judge;
    private Compile compile;
    private String judger_dir;
    private String uid;
    private String gid;

    @Data
    public static class Compile {
        private C c;
        private Cpp cpp;
        private Java java;
        private Python2 python2;
        private Python3 python3;

        @Data
        public static class C extends LanguageConfigBase {
        }

        @Data
        public static class Cpp extends LanguageConfigBase {
        }

        @Data
        public static class Java extends LanguageConfigBase {
        }

        @Data
        public static class Python2 extends LanguageConfigBase {
        }

        @Data
        public static class Python3 extends LanguageConfigBase {
        }
    }

    @Data
    public static class Judge {
        private C c;
        private Cpp cpp;
        private Java java;
        private Python2 python2;
        private Python3 python3;

        @Data
        public static class C extends LanguageConfigBase {
        }

        @Data
        public static class Cpp extends LanguageConfigBase {
        }

        @Data
        public static class Java extends LanguageConfigBase {
        }

        @Data
        public static class Python2 extends LanguageConfigBase {
        }

        @Data
        public static class Python3 extends LanguageConfigBase {
        }
    }
}

@Data
class LanguageConfigBase {
    private String args;
}
