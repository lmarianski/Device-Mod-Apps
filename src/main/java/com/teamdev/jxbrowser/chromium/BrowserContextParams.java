package com.teamdev.jxbrowser.chromium;

import com.teamdev.jxbrowser.chromium.internal.Environment;
import java.io.File;

public final class BrowserContextParams
{
  private final String a;
  private String b;
  private String c;
  private String d;
  private ProxyConfig e;
  private StorageType f;
  
  public BrowserContextParams(String dataDir)
  {
    this(dataDir, BrowserPreferences.getDefaultAcceptLanguage());
  }
  public BrowserContextParams(String dataDir, String acceptLanguage)
  {
    if ((dataDir == null) || (dataDir.isEmpty())) {
      throw new IllegalArgumentException("The dataDir parameter cannot be null or empty.");
    }
    a = dataDir;
    f = StorageType.DISK;
    
    setCacheDir(dataDir + File.separator + "Cache");
    setMemoryDir(dataDir + File.separator + "Memory");
    
    setAcceptLanguage(acceptLanguage);
  }
  


  public final void setProxyConfig(AutoDetectProxyConfig proxyConfig)
  {
    e = proxyConfig;
  }
  


  public final void setProxyConfig(CustomProxyConfig proxyConfig)
  {
    e = proxyConfig;
  }
  


  public final void setProxyConfig(DirectProxyConfig proxyConfig)
  {
    e = proxyConfig;
  }
  



  public final StorageType getStorageType()
  {
    return f;
  }
  





  public final void setStorageType(StorageType storageType)
  {
    if (storageType == null) {
      throw new IllegalArgumentException("The storageType parameter cannot be null.");
    }
    f = storageType;
  }
  


  public final String getCacheDir()
  {
    return b;
  }
  







  public final void setCacheDir(String cacheDir)
  {
    if ((cacheDir == null) || (cacheDir.isEmpty())) {
      throw new IllegalArgumentException("The cacheDir parameter cannot be null or empty.");
    }
    b = cacheDir;
  }
  




  public final String getMemoryDir()
  {
    return c;
  }
  









  public final void setMemoryDir(String memoryDir)
  {
    if ((!Environment.isWindows()) && ((memoryDir == null) || (memoryDir.isEmpty()))) {
      throw new IllegalArgumentException("The memoryDir parameter cannot be null or empty.");
    }
    c = memoryDir;
  }
  


  public final String getAcceptLanguage()
  {
    return d;
  }
  








  public final void setAcceptLanguage(String acceptLanguage)
  {
    if (acceptLanguage == null || acceptLanguage == "") {
      throw new IllegalArgumentException("The acceptLanguage parameter cannot be null or empty.");
    }
    
    d = acceptLanguage;
  }
  



  public final ProxyConfig getProxyConfig()
  {
    return e;
  }
  



  public final void setProxyConfig(URLProxyConfig proxyConfig)
  {
    e = proxyConfig;
  }
  


  public final String getDataDir()
  {
    return a;
  }
}