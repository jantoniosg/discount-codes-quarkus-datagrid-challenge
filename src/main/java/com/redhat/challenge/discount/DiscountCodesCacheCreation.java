package com.redhat.challenge.discount;

import io.quarkus.runtime.StartupEvent;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.commons.configuration.XMLStringConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class DiscountCodesCacheCreation {

  private static final Logger LOGGER = LoggerFactory.getLogger("DiscountsCodeCacheCreation");

  public static final String DISCOUNTS_NAME = "discounts";

  private static final String CACHE_CONFIG = "<distributed-cache name=\"%s\">"
          + " <encoding media-type=\"application/x-protostream\"/>"
          + "</distributed-cache>";

  @Inject
  RemoteCacheManager cacheManager;

  void onStart(@Observes StartupEvent ev) {
    LOGGER.info("Create or get cache named discounts with the default configuration");
    // Inject the cache manager and use the administration API to create the cache.
    // You can also use the operator or the WebConsole to create the cache "discounts"
    String cacheConfig = String.format(CACHE_CONFIG, DISCOUNTS_NAME);
    // Use XMLStringConfiguration. Grab a look to the simple tutorial about "creating caches on the fly" in the
    // Infinispan Simple Tutorials repository.
    cacheManager.administration().getOrCreateCache(DISCOUNTS_NAME, new XMLStringConfiguration(cacheConfig));
  }
}
