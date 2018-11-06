package com.wx.somecode;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;

/**
 * @author xinquan.huangxq
 */
public class MyCacheEventListener implements CacheEventListener {

    @Override
    public void notifyElementRemoved(Ehcache cache, Element element) throws CacheException {
        System.out.println("remove:" + element.getKey() + "@" + element.getValue());
    }

    @Override
    public void notifyElementPut(Ehcache cache, Element element) throws CacheException {
        System.out.println("put:" + element.getKey() + "@" + element.getValue());
    }

    @Override
    public void notifyElementUpdated(Ehcache cache, Element element) throws CacheException {
    }

    @Override
    public void notifyElementExpired(Ehcache cache, Element element) {
        System.out.println("expire:" + element.getKey() + "@" + element.getValue());
    }

    @Override
    public void notifyElementEvicted(Ehcache cache, Element element) {
        System.out.println("evict:" + element.getKey() + "@" + element.getValue());
    }

    @Override
    public void notifyRemoveAll(Ehcache cache) {
    }

    @Override
    public void dispose() {

    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return null;
    }
}
