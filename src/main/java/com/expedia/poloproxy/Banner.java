package com.expedia.poloproxy;

import org.apache.commons.io.IOUtils;

import java.util.logging.Logger;

@SuppressWarnings({"PMD.UseUtilityClass"})
public class Banner {

    private final static Logger LOG = Logger.getLogger(Banner.class.getName());

    protected static void printToLog() {
        try {
            final ClassLoader loader = Banner.class.getClassLoader();
            final String bannerText = IOUtils.toString(loader.getResourceAsStream("banner.txt"));
            LOG.info(bannerText);
        } catch (Exception e) {
            LOG.throwing(Banner.class.getName(), "LOG", e);
        }
    }
}
