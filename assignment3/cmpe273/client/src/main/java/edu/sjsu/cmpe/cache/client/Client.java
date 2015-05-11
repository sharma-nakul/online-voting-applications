package edu.sjsu.cmpe.cache.client;
import com.google.common.hash.Hashing;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Client {
    static RendezvousHash  rh = new RendezvousHash();
    public static void main(String[] args) throws Exception {
        System.out.println("Starting Cache Client...");
        DistributedCacheService cacheA = new DistributedCacheService("http://localhost:3000");
        DistributedCacheService cacheB = new DistributedCacheService("http://localhost:3001");
        DistributedCacheService cacheC = new DistributedCacheService("http://localhost:3002");

        ArrayList<CacheServiceInterface> servers = new ArrayList<CacheServiceInterface>();

        servers.add(cacheA);
        servers.add(cacheB);
        servers.add(cacheC);

        int bucket=0;
        String[] values = new String[] {"a","b","c","d","e","f","g","h","i","j"};
        //code for putting values

        for(int k = 1; k <= 10; k++) {
            bucket = Hashing.consistentHash(Hashing.md5().hashString(Integer.toString(k)), servers.size());
            System.out.println( values[k-1]  + " ==> Server" + (bucket+1));
            servers.get(bucket).put(k,values[k-1]);
        }

        //code for getting values
        for(int k = 1; k <= 10; k++) {
            bucket = Hashing.consistentHash(Hashing.md5().hashString(Integer.toString(k)), servers.size());
            System.out.println( "Server" + (bucket+1) + " ==> " + values[k-1]);
            //System.out.println(servers.get(bucket).get(k));
        }

        System.out.println( "----------------------------------------------------");
        rh.add(cacheA);
        rh.add(cacheB);
        rh.add(cacheC);

        rPut(1, "a");
        rPut(2,"b");
        rPut(3, "c");
        rPut(4, "d");
        rPut(5, "e");
        rPut(6, "f");
        rPut(7, "g");
        rPut(8, "h");
        rPut(9, "i");
        rPut(10, "j");

        for (int i =1; i<=10; i++)
        {
            rGet(i);
        }

        System.out.println("Existing Cache Client...");
    }

    public static void rGet(long key)
    {

        DistributedCacheService node = rh.getBucket(Long.valueOf(key).toString());
        String value = node.get(key);
        System.out.println(node.cacheServerUrl + " ==> " + value);
        //System.out.println(value);


    }

    public static void rPut(int key,String value)
    {
        DistributedCacheService node = rh.getBucket(Long.valueOf(key).toString());
        node.put(key,value);
        System.out.println(value  + " ==> " + node.cacheServerUrl);
    }

}
