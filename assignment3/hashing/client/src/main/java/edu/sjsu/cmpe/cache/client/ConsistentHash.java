package edu.sjsu.cmpe.cache.client;

import com.google.common.hash.Hashing;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHash{

    private final SortedMap<Integer, CacheServiceInterface> circle = new TreeMap<Integer, CacheServiceInterface>();

    public void add(DistributedCacheService node) {
            int key = getHash(node.cacheServerUrl);
            circle.put(key,node);
    }

    public CacheServiceInterface getBucket(String key) {
        if (circle.isEmpty()) {
            return null;
        }
        int hash = getHash(key);
        if (!circle.containsKey(hash)) {
            SortedMap<Integer,CacheServiceInterface> tailMap = circle.tailMap(hash);
            hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        }
        return circle.get(hash);
    }

    public int getHash(String key)
    {
        int number;
        //try {
            /*MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(key.getBytes());
            number = new BigInteger(1, messageDigest).intValue(); */
            number =  Hashing.murmur3_128().newHasher().putString(key).hash().asInt();
            return number;
        //}
        //catch (NoSuchAlgorithmException e) {
          //  throw new RuntimeException(e);
        //}

    }

}