/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.network.platform;

/**
 *
 * @author psammand
 */
public class NetworkPlatformFactoryMgr
{

  private static NetworkPlatformFactoryMgr instance = null;

  public static NetworkPlatformFactoryMgr Inst()
  {
    if (instance == null)
    {
      instance = new NetworkPlatformFactoryMgr();
    }

    return instance;
  }

  INetworkPlatformFactory networkPlatformFactory = null;

  public INetworkPlatformFactory getNetworkPlatformFactory()
  {
    return networkPlatformFactory;
  }

  public void setNetworkPlatformFactory(INetworkPlatformFactory networkPlatformFactory)
  {
    this.networkPlatformFactory = networkPlatformFactory;
  }
}
