package rand.api.domain.common.repository;

import java.util.concurrent.TimeUnit;

public interface InMemRepository {
    //create
    public void save(String key,Object value);
    public void save(String key, Object value, long ttl, TimeUnit timeUnit);
    public void increment(String key, int incrementVal,long ttl,TimeUnit timeUnit);

    public void hashSave(String key,String hashKey,Object value);
    public void hashSave(String key,String hashKey,Object value,long ttl, TimeUnit timeUnit);

    public void listRightPush(String key, Object value);
    public void listRightPush(String key, Object value,long ttl, TimeUnit timeUnit);

    public void setSave(String key , Object value);
    public void setSave(String key , Object value,long ttl, TimeUnit timeUnit);


    //Read

    public Object getValue(String key);
    public Object getHashValue(String key,String hashKey);
//    public T getListRange(String key,int start,int end);
    public Object getSetAllValue(String key);


//    //update
//    public void listUpdate(String key, int index, T value);


    //delete
    public void delete(String key);
    public void hashDelete(String key,String hashKey);
    public void setDelete(String key,String value);

}
