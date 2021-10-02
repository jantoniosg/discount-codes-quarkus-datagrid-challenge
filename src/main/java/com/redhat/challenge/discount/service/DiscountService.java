package com.redhat.challenge.discount.service;

import com.redhat.challenge.discount.model.DiscountCode;
import com.redhat.challenge.discount.model.DiscountCodeType;
import io.quarkus.infinispan.client.Remote;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.Search;
import org.infinispan.query.dsl.QueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class DiscountService {

  private static final Logger LOGGER = LoggerFactory.getLogger(DiscountService.class.getName());

  @Inject
  @Remote(DiscountCode.DISCOUNT_CODE_CACHE)
  protected RemoteCache<String, DiscountCode> cache;

  public String create(DiscountCode discountCode, long lifespan) {
    cache.putIfAbsent(discountCode.getName(), discountCode, lifespan, TimeUnit.SECONDS);
    return discountCode.getName();
  }

  public DiscountCode consume(String name) {
    DiscountCode discountCode = cache.get(name);

    if (discountCode == null) {
      LOGGER.error("Unable to search... ");
      throw new IllegalStateException("DiscountCode does not exists.");
    }

    discountCode.setUsed(discountCode.getUsed() + 1);

    // This provokes that lifespan (expiration) disappears
    cache.put(name, discountCode);

    return discountCode;
  }

  /**
   * Performs a simple full-text query on type
   *
   * @param type the type to search
   * @return discount codes
   */
  public List<DiscountCode> getByType(DiscountCodeType type) {
    if (cache == null) {
      LOGGER.error("Unable to search... ");
      throw new IllegalStateException("DiscountCodes is null. Try restarting the application");
    }
    QueryFactory queryFactory = Search.getQueryFactory(cache);

    String query = "from developer_games.DiscountCode where type = '" + type.toString() + "'";

    return queryFactory.<DiscountCode>create(query).execute().list();
  }

}
