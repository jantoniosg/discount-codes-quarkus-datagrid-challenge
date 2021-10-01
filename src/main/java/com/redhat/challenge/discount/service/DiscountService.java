package com.redhat.challenge.discount.service;

import com.redhat.challenge.discount.model.DiscountCode;
import com.redhat.challenge.discount.model.DiscountCodeType;
import com.redhat.challenge.utils.Utils;
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

import static com.redhat.challenge.discount.DiscountCodesCacheCreation.DISCOUNTS_NAME;

@ApplicationScoped
public class DiscountService {

  private static final Logger LOGGER = LoggerFactory.getLogger(DiscountService.class.getName());

  @Inject
  @Remote(DISCOUNTS_NAME)
  RemoteCache<String, DiscountCode> cache;

  public String create(DiscountCode discountCode) {

    if (!cache.containsKey(discountCode.getName())) {
      discountCode.setUsed(0);

      cache.put(discountCode.getName(), discountCode, discountCode.getLifespan(), TimeUnit.SECONDS);

      return discountCode.getName();
    }
    return discountCode.getName();
  }

  public String consume(String name) {
    DiscountCode discountCode = cache.get(name);

    if (discountCode == null) {
      return "The discount code  " + name + " does not exists";
    }

    discountCode.setUsed(discountCode.getUsed() + 1);

    cache.putAsync(name, discountCode, discountCode.getLifespan(), TimeUnit.SECONDS);

    String discount = Utils.discountStr(discountCode);
    String enterpriseCapitalize = Utils.capitalize(discountCode.getEnterprise());

    return "· " + discountCode.getName().toUpperCase() + ", " + discountCode.getUsed() + ", " +
            discountCode.getEnterprise() + ", " + discountCode.getAmount() + ", " + discountCode.getType()
            + " (" + discount + enterpriseCapitalize + " used " + discountCode.getUsed() + " times)";
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
      throw new IllegalStateException("Characters store is null. Try restarting the application");
    }
    QueryFactory queryFactory = Search.getQueryFactory(cache);

    String query = "FROM dc_monitoring.DiscountCode d"
            + " WHERE d.type = '" + type.toString() + "'";

    return queryFactory.<DiscountCode>create(query).execute().list();
  }

}
