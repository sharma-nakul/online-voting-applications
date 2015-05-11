package edu.sjsu.cmpe.cache.client;

import com.google.common.hash.Hashing;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class RendezvousHash {

    private final ConcurrentSkipListSet<String> nodeUrls = new ConcurrentSkipListSet<String>();
    //private final ArrayList<DistributedCacheService> nodes = new ArrayList<DistributedCacheService>();
    private final HashMap<String, DistributedCacheService> nodes = new HashMap<String, DistributedCacheService>();

    public void add(DistributedCacheService node) {
        nodes.put(node.cacheServerUrl, node);
        nodeUrls.add(node.cacheServerUrl);
    }

    public DistributedCacheService getBucket(String key) {
        long maxValue = Long.MIN_VALUE;
        DistributedCacheService maxNode = null;
        String maxNodeUrl = "";
        for (String nodeUrl : nodeUrls) {
            //System.out.println(nodeUrl);
            long nodesHash =  Hashing.murmur3_128().newHasher().putString(key).putString(nodeUrl).hash().asLong();
            if (nodesHash > maxValue) {
                maxNodeUrl = nodeUrl;
                maxValue = nodesHash;
            }
        }
        return nodes.get(maxNodeUrl);
    }

}