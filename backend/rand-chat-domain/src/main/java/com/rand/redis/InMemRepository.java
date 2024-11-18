package com.rand.redis;
import org.springframework.data.geo.Point;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface InMemRepository {
    //create
    public void save(String key,Object value);
    public void save(String key, Object value, long ttl, TimeUnit timeUnit);
    public void saveLoc(String usrId, double lat ,double lon);
    public void increment(String key, int incrementVal,long ttl,TimeUnit timeUnit);

    public void hashSave(String key,String hashKey,Object value);
    public void hashSave(String key,String hashKey,Object value,long ttl, TimeUnit timeUnit);

    public void listRightPush(String key, Object value);
    public void listRightPush(String key, Object value,long ttl, TimeUnit timeUnit);

    public void setSave(String key , Object value);
    public void setSave(String key , Object value,long ttl, TimeUnit timeUnit);
    public void sortedSetSave(String key , String usrId,long timestamp);

    //Read
    public boolean scan(String key , String value);
    public Object getValue(String key);
    public Object getHashValue(String key,String hashKey);
    public Point getLoc(String usrId);
    public Set<String> sortedSetRangeByScore(String key ,long minScore,long maxScore);
    public Set<String> geoRadius( String usrId, double radiusInMeters);
    //    public T getListRange(String key,int start,int end);
    public Object getSetAllValue(String key);
    public double calculateDistance(String usrId1 , String usrId2);

//    //update
//    public void listUpdate(String key, int index, T value);


    //delete
    public void delete(String key);
    public void hashDelete(String key,String hashKey);
    public void setDelete(String key,String value);
    public void sortedSetRemove(String key , String value);


}
