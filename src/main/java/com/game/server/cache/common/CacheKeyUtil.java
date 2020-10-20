package com.game.server.cache.common;

public class CacheKeyUtil
{
  public static String getKey(int nameSpace, String key)
  {
    key = nameSpace + "_" + key;
    return key;
  }
}