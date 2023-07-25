/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.model.CA;

import com.utrace.model.Ent.ProductEnt;
import com.urvega.framework.redis.RedisClient;
import com.urvega.framework.util.ConvertUtil;
import com.urvega.framework.util.JSONUtil;
import com.urvega.framework.util.LogUtil;
import com.utrace.config.ConfigInfo;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Admin
 */
public class ProductCA {
    private static final Logger logger = LogUtil.getLogger(ProductCA.class);
    private static final String PRODUCT_KEY = "prd:%s";
    private static final String PRODUCT_MAP = "prm:%s";

    public static Long set(ProductEnt item) {
        Long result = 0L;

        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_PRODUCT);
            String key = String.format(PRODUCT_KEY, item.companyId);
            double score = System.currentTimeMillis(); // Sử dụng thời gian hiện tại làm điểm số
            result = client.zadd(key, score, JSONUtil.serialize(item));
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }

        return result;
    }

    public static List<ProductEnt> getListByCompanyId(int companyId) {
        List<ProductEnt> result = new ArrayList<>();

        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_PRODUCT);
            String key = String.format(PRODUCT_KEY, companyId);
            List<String> products = client.zrange(key, 0, -1);

            for (String productData : products) {
                ProductEnt product = JSONUtil.deserialize(productData, ProductEnt.class);
                result.add(product);
            }
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }

        return result;
    }

    public static boolean del(int companyId, String productId) {
        boolean result = false;

        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_PRODUCT);
            String key = String.format(PRODUCT_KEY, companyId);
            result = client.zrem(key, new String[]{productId}) > 0;
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }

        return result;
    }

    public static boolean setMap(String key, int companyId) {
        boolean result = false;

        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_MEMBER);
            String mapKey = String.format(PRODUCT_MAP, key);
            result = client.set(mapKey, ConvertUtil.toString(companyId));
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }

        return result;
    }

    public static int getMap(String key) {
        int result = 0;
        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_MEMBER);
            String mapKey = String.format(PRODUCT_MAP, key);
            result = ConvertUtil.toInt(client.get(mapKey));
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
            result = -1;
        }

        return result;
    }

    public static boolean delMap(String key) {
        boolean result = false;

        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_MEMBER);
            String mapKey = String.format(PRODUCT_MAP, key);
            result = client.del(mapKey) > 0;
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }

        return result;
    }

    public static ProductEnt get(int companyId, int productId) {
        ProductEnt result = null;

        try {
            List<ProductEnt> products = getListByCompanyId(companyId);

            for (ProductEnt product : products) {
                if (product.id == productId) {
                    result = product;
                    break;
                }
            }
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }

        return result;
    }
    /*
    public static boolean update(ProductEnt updatedProduct) {
        boolean result = false;
        ProductEnt product = get(updatedProduct.companyId, updatedProduct.id);

        if (product != null) {
            try {
                RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_MEMBER);
                String key = String.format(PRODUCT_KEY, updatedProduct.companyId);

                // Remove the product from the sorted set using the product's id
                String[] productJsonArray = product.toArray(new String[0]);
                client.zrem(key, productJsonArray);

                // Update the product's information
                product.name = updatedProduct.name;
                product.description = updatedProduct.description;
                product.content = updatedProduct.content;
                product.productionUnitCode = updatedProduct.productionUnitCode;
                product.acreage = updatedProduct.acreage;
                product.rawMaterialArea = updatedProduct.rawMaterialArea;
                product.photos = updatedProduct.photos;
                product.recipe = updatedProduct.recipe;
                product.recipePhotos = updatedProduct.recipePhotos;
                product.farmingPhotos = updatedProduct.farmingPhotos;
                product.video = updatedProduct.video;
                product.certification = updatedProduct.certification;
                product.status = updatedProduct.status;
                product.updatedAt = updatedProduct.updatedAt;

                // Add the updated product back to the sorted set
                double newScore = updatedProduct.updatedAt;
                client.zadd(key, newScore, JSONUtil.serialize(product));

                result = true;
            } catch (Exception e) {
                logger.error(LogUtil.stackTrace(e));
            }
        }

        return result;
    }
*/

//    public static boolean update(ProductEnt item) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
}
