package com.redhat.challenge.discount;

import com.redhat.challenge.discount.model.DiscountCode;
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

  // We've changed the actual cache configuration for two reasons:
  // 1. To be able to use correctly cache.putIfAbsent in create method (without this configuration, we had a warning
  // message).
  // 2. To demonstrate a small way to manage concurrency. (Bonus Track #3)
  // More about how to manage concurrency and lock in data grid in these links:
  // https://access.redhat.com/documentation/en-us/red_hat_data_grid/7.1/html/administration_and_configuration_guide/locking
  // https://access.redhat.com/documentation/en-us/red_hat_data_grid/8.0/html/data_grid_developer_guide/locking_concurrency
  private static final String CACHE_CONFIG = "<infinispan>" +
          "<cache-container>" +
          "<distributed-cache name=\"%s\" mode=\"SYNC\">" +
          "<encoding media-type=\"application/x-protostream\"/>" +
          // the default value for locking isolation
          "<locking isolation=\"READ_COMMITTED\"/>" +
          // Specifies how the RemoteCache is enlisted in the Transaction. If NONE is used, the RemoteCache won't be
          // transactional.
          // NON_XA: The cache is enlisted as Synchronization.
          "<transaction mode=\"NON_XA\"/>" +
          "</distributed-cache>" +
          "</cache-container>" +
          "</infinispan>";

  @Inject
  protected RemoteCacheManager cacheManager;

  void onStart(@Observes StartupEvent ev) {
    LOGGER.info("Create or get cache named discounts with the default configuration");
    // Inject the cache manager and use the administration API to create the cache.
    // You can also use the operator or the WebConsole to create the cache "discounts"
    String cacheConfig = String.format(CACHE_CONFIG, DiscountCode.DISCOUNT_CODE_CACHE);
    // Use XMLStringConfiguration. Grab a look to the simple tutorial about "creating caches on the fly" in the
    // Infinispan Simple Tutorials repository.
//    cacheManager.administration().getOrCreateCache(DiscountCode.DISCOUNT_CODE_CACHE,
//            new XMLStringConfiguration(cacheConfig));
  }
}
