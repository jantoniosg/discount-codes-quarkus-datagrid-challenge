package com.redhat.challenge.utils;

import com.redhat.challenge.discount.model.DiscountCode;
import org.infinispan.client.hotrod.MetadataValue;
import org.infinispan.client.hotrod.RemoteCache;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Utility class
 */
public class Utils {

  private Utils() {
    // Utility class
  }

  public static long calculateNewLifespan(String name, RemoteCache<String, DiscountCode> cache) {
    MetadataValue<DiscountCode> metadata = cache.getWithMetadata(name);
    long currentLifespan = metadata.getLifespan();

    Date createdDate = new Date(metadata.getCreated());
    Date currentDate = new Date(System.currentTimeMillis());

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    int diff = 0;

    try {
      String dateString1 = format.format(createdDate);
      Date date1 = format.parse(dateString1);
      String dateString2 = format.format(currentDate);
      Date date2 = format.parse(dateString2);

      long diffInMillies = Math.abs(date2.getTime() - date1.getTime());
      diff = (int) TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);

    } catch (ParseException e) {
      throw new IllegalStateException(e);
    }

    return currentLifespan - diff;
  }
}
