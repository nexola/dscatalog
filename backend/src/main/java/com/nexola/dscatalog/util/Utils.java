package com.nexola.dscatalog.util;

import com.nexola.dscatalog.entities.Product;
import com.nexola.dscatalog.projections.ProductProjection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    public static List<Product> replace(List<ProductProjection> ordered, List<Product> unordered) {

        Map<Long, Product> map = new HashMap<>();
        for (Product product : unordered) {
            map.put(product.getId(), product);
        }

        List<Product> result = new ArrayList<>();
        for (ProductProjection projection : ordered) {
            result.add(map.get(projection.getId()));
        }

        return result;
    }

}
