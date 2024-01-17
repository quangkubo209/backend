package org.testGoogleAds;

import com.google.ads.googleads.lib.GoogleAdsClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.testGoogleAds.Validate.ValidateFile;
import org.testGoogleAds.Mutator.HandleMutator;

import com.google.ads.googleads.v15.services.UserListOperation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@MapperScan("org.testGoogleAds.mapper")
@SpringBootApplication
public class GoogleAdsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoogleAdsApplication.class, args);

    }
}
