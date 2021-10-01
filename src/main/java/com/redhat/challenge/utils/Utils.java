package com.redhat.challenge.utils;

import com.redhat.challenge.discount.model.DiscountCode;
import com.redhat.challenge.discount.model.DiscountCodeType;

/**
 * Utility class
 */
public class Utils {

  private Utils() {
    // Utility class
  }

  public static String capitalize(String value) {
    return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
  }

  public static String discountStr(DiscountCode discountCode) {
    String discount;

    if (DiscountCodeType.PERCENT.equals(discountCode.getType())) {
      discount = discountCode.getAmount() + "% discount in ";
    } else {
      discount = discountCode.getAmount() + " euros of discount in ";
    }
    return discount;
  }
}
